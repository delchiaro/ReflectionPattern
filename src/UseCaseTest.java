import com.sun.istack.internal.NotNull;
import org.h2.store.fs.FileUtils;
import org.testng.internal.Nullable;
import reflectionPattern.dataGeneration.FactGenerator;
import reflectionPattern.persistency.PersistencyHelper;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import utility.composite.out.CompositeTree;

import javax.persistence.EntityTransaction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by nagash on 12/09/16.
 */
public class UseCaseTest {

    private final VerbouseMode verb;
    PersistencyHelper.Strategy helperStrategy = null;

    private boolean printNQueries = true;

    String outputFileName = "UseCaseTests.txt";

    PrintWriter fileWrt = null;
    public enum VerbouseMode { NONE, CONSOLLE, FILE }

    UseCaseTest(PersistencyHelper.Strategy helperStrategy) {
        this(helperStrategy, VerbouseMode.NONE);
    }
    UseCaseTest(PersistencyHelper.Strategy helperStrategy,VerbouseMode verbouseMode) {
        this.verb = verbouseMode;
        this.helperStrategy = helperStrategy;
        setOutputFileName(outputFileName);
    }
    UseCaseTest(PersistencyHelper.Strategy helperStrategy,VerbouseMode verbouseMode, boolean printNQueries) {
        this.verb = verbouseMode;
        this.printNQueries = printNQueries;
        this.helperStrategy = helperStrategy;
        setOutputFileName(outputFileName);
    }


    public void setOutputFileName(String fileName) {
        if(fileWrt != null )
            fileWrt.close();
        outputFileName = fileName;
        try {
            fileWrt = new PrintWriter(outputFileName, "UTF-8");
        }
        catch (FileNotFoundException e) { e.printStackTrace();}
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }
    }
    public String getOutputFileName() { return outputFileName; }


    public void test(@Nullable Long idType, boolean useALS)
    {
        Fact rootFact = UC1(idType);
        UC2(rootFact);
        UC3(rootFact.getId(), useALS);
    }



    public Fact UC1() {
        return UC1(null);
    }
    public Fact UC1( Long idType) {

        consolleOut(" ================= UC 1 =================\n");
        consolleOut("Apertura di un FactType, istanziazione dei Fact (vuoti) corrispondenti alla struttura dei FactType.\n");

        PersistencyHelper.silenceGlobalHibernateLogs();

        PersistencyHelper ph = new PersistencyHelper(helperStrategy, true).connect();

        // EntityTransaction loadTransact = ph.newTransaction();

        ph.timer().reset_start();

        // loadTransact.begin();

        CompositeType rootType = (CompositeType) ph.factTypeDAO().findById(idType);
        ph.factTypeDAO().fetchTypeEager_PhenomsUnits(rootType);

        //loadTransact.commit();

        FactGenerator f = new FactGenerator();
        Fact rootEmptyFact = f.generate(rootType);

        double loadTime = ph.timer().elapsedMs();
        long loadQueries = ph.statistics().getPrepareStatementCount();

        ph.close();



        consolleOut("\n\nLoaded FactType: \n");
        consolleOut(CompositeTree.getTree(rootType));

        consolleOut("Elapsed load time: \t" +  loadTime + "\n");
        consolleOut("Executed load queries: \t" +  loadQueries + "\n");


        String typeName = rootType.getTypeName();

            fileOut("\n"+typeName + "\nUC1: loaded type from db.", "\n\n\n" + typeName
                    + "\n" + (printNQueries ?  (loadQueries + "\t") : "")  + loadTime );


        return rootEmptyFact;

    }

    public  void UC2( Fact rootFact) {
        consolleOut("\n\n\n ================= UC 2 =================\n");
        consolleOut("Compilazione casuale di fact vuoto e salvataggio.\n");

        // U.C.2: il salvataggio di una visita medica compilata
        PersistencyHelper.silenceGlobalHibernateLogs();



        consolleOut("...Generating Fact with some random value...\n");

        FactGenerator.randomFill(rootFact);

        consolleOut("...Persisting the generated Fact...\n");


        PersistencyHelper ph = new PersistencyHelper(helperStrategy, true).connect();

        EntityTransaction saveTransact = ph.newTransaction();

        ph.timer().reset_start();

        saveTransact.begin();
        {
            ph.entityManager().persist(rootFact);
        }
        saveTransact.commit();

        double saveTime = ph.timer().elapsedMs();
        long   saveQueries = ph.statistics().getPrepareStatementCount();

        ph.entityManager().clear();
        ph.close();


        consolleOut("\nSaved Fact: \n");
        consolleOut(CompositeTree.getTree(rootFact));
        consolleOut("Elapsed save time: \t" +  saveTime + "\n");
        consolleOut("Executed save queries: \t" +  saveQueries + "\n");


        fileOut("\nUC2: compiled and saved Fact on DB.", "\n" + (printNQueries ? (saveQueries + "\t") : "") + saveTime  );

    }







    public  void UC3(boolean useALS) {
        UC3(null, useALS);
    }
    public  void UC3(@NotNull Long idFact, boolean useALS) {
        consolleOut("\n\n\n ================= UC 3 =================\n");
        consolleOut("Lettura Fact precedentemente salvato.\n");

        PersistencyHelper.silenceGlobalHibernateLogs();

        PersistencyHelper ph = new PersistencyHelper(helperStrategy, true).connect();


        Fact rootFact = null;
        List<Fact> factList = null;

        //EntityTransaction loadFactTransact = ph.newTransaction();

        ph.timer().reset_start();
        //loadFactTransact.begin();
        rootFact = ph.factDAO().findById(idFact);

        if(rootFact != null)
        {
            if(useALS && rootFact instanceof CompositeFact)
                factList = ph.factDAO().findAllDescendants((CompositeFact)rootFact);

            else if(rootFact instanceof CompositeFact )
                ph.factDAO().fetchEager_TypeUnitPhenom((CompositeFact) rootFact);
        }
        //loadFactTransact.commit();
        double loadFactTime = ph.timer().elapsedMs();
        long   loadFactQueries = ph.statistics().getPrepareStatementCount();
        ph.close();



        if(rootFact == null)
            consolleOut("\nTest failed: can't find a corresponding fact in DB \n");
        else
        {
            if(verb == VerbouseMode.CONSOLLE)
            {
                if(useALS && factList != null)
                {
                    consolleOut("\nLoaded Fact (with ALS): \n");
                    for(Fact f : factList)
                        consolleOut("\n - " + f.toString() );
                }
                else
                {
                    consolleOut("\nLoaded Fact: \n");
                    CompositeTree.printTree(rootFact);
                }
                consolleOut("\n");
            }

            // Remove the Fact created in UC2
            ph.connect();
            EntityTransaction del = ph.newTransaction();
            del.begin();
            ph.factDAO().delete(idFact);
            //ph.remove(rootFact);
            del.commit();
            ph.close();

        }



        consolleOut("Loading fact Elapsed time : \t" +  loadFactTime + "\n");
        consolleOut("Loading fact Executed queries: \t" +  loadFactQueries + "\n");


        fileOut("\nUC3: Loaded Fact from DB.", "\n"+ (printNQueries ? ( loadFactQueries + "\t") : "" ) + loadFactTime  );

        if(fileWrt !=null)
            fileWrt.close();
    }






    private void consolleOut(String s) {
        if(verb == VerbouseMode.CONSOLLE)
            System.out.print(s);
    }
    private void fileOut(String consolleLog, String fileOutString) {
        if(verb == VerbouseMode.FILE) {
            System.out.print(consolleLog);
            fileWrt.print(fileOutString);
        }
    }

}
