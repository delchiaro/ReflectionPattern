import com.sun.istack.internal.NotNull;
import org.testng.internal.Nullable;
import reflectionPattern.dataGeneration.FactGenerator;
import reflectionPattern.persistency.PersistencyHelper;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import utility.composite.out.CompositeTree;

import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;
import java.util.List;

/**
 * Created by nagash on 12/09/16.
 */
public class UseCaseTest {

    private final boolean VERBOUSE;
    PersistencyHelper.Strategy helperStrategy = null;



    UseCaseTest(PersistencyHelper.Strategy helperStrategy) {
        this(helperStrategy, false);
    }
    UseCaseTest(PersistencyHelper.Strategy helperStrategy, boolean verbouse) {
        this.VERBOUSE = verbouse;
        this.helperStrategy = helperStrategy;
    }


    public void test(@Nullable Long idType)
    {
        Fact rootFact = UC1(idType);
        UC2(rootFact);
        UC3(rootFact.getId(), false);
    }



    public Fact UC1() {
        return UC1(null);
    }
    public Fact UC1(@Nullable Long idType) {

        System.out.print(" ================= UC 1 =================\n");
        System.out.print("Apertura di un FactType, istanziazione dei Fact (vuoti) corrispondenti alla struttura dei FactType.\n");

        PersistencyHelper.silenceGlobalHibernateLogs();

        PersistencyHelper ph = new PersistencyHelper(helperStrategy, true).connect();


        List<CompositeType> rootTypes;
        CompositeType rootType;


        //     U.C.1: apertura di una nuova visita medica, istanziazione dei Fact (vuoti) corrispondenti alla struttura dei FactType.


        if(idType == null) {
            // TODO: same reflectionPattern.test but with Eager select (have to modify annotation in FactType, or make a new xml settings for FactType to load).
            if (VERBOUSE) System.out.print("...Lazy loading of all composite type root...\n");
            if (VERBOUSE) System.out.print("...Eager fetching of first composite type root...\n");
        }

        EntityTransaction loadTransact = ph.newTransaction();

        ph.timer().reset_start();

        loadTransact.begin();
        {
            if(idType == null) {
                rootTypes = ph.factTypeDAO().findAllCompositeRoots(false);
                rootType = rootTypes.get(0);
                ph.factTypeDAO().fetchCompositeEager(rootType);
            }
            else rootType = (CompositeType) ph.factTypeDAO().findById(idType);
        }
        loadTransact.commit();

        FactGenerator f = new FactGenerator();
        Fact rootEmptyFact = f.generate(rootType);

        double loadTime = ph.timer().elapsedMs();
        long loadQueries = ph.statistics().getPrepareStatementCount();

        ph.close();


        if(VERBOUSE) {
            System.out.print("\n\nLoaded FactType: \n");
            CompositeTree.printTree(rootType);
        }

        System.out.print("Elapsed load time: \t" +  loadTime + "\n");
        System.out.print("Executed load queries: \t" +  loadQueries + "\n");

        return rootEmptyFact;

    }

    public  void UC2( Fact rootFact) {
        System.out.print("\n\n\n ================= UC 2 =================\n");
        System.out.print("Compilazione casuale di fact vuoto e salvataggio.\n");

        // U.C.2: il salvataggio di una visita medica compilata
        PersistencyHelper.silenceGlobalHibernateLogs();

        PersistencyHelper ph = new PersistencyHelper(helperStrategy, true).connect();


        if(VERBOUSE) System.out.print("...Generating Fact with some random value...\n");

        FactGenerator.randomFill(rootFact);

        if(VERBOUSE) System.out.print("...Persisting the generated Fact...\n");

        EntityTransaction saveTransact = ph.newTransaction();

        ph.timer().reset_start();

        saveTransact.begin();
        {
            ph.persist(rootFact);
        }
        saveTransact.commit();

        double saveTime = ph.timer().elapsedMs();
        long   saveQueries = ph.statistics().getPrepareStatementCount();


        ph.close();

        if(VERBOUSE) {
            System.out.print("\nSaved Fact: \n");
            CompositeTree.printTree(rootFact);
        }

        System.out.print("Elapsed save time: \t" +  saveTime + "\n");
        System.out.print("Executed save queries: \t" +  saveQueries + "\n");


    }







    public  void UC3(boolean useALS) {
        UC3(null, useALS);
    }
    public  void UC3(@NotNull Long idFact, boolean useALS) {
        System.out.print("\n\n\n ================= UC 3 =================\n");
        System.out.print("Lettura Fact precedentemente salvato.\n");

        PersistencyHelper.silenceGlobalHibernateLogs();

        PersistencyHelper ph = new PersistencyHelper(helperStrategy, true).connect();


        Fact rootFact = null;
        List<Fact> factList = null;

        if(VERBOUSE) System.out.print("...Loading the first root fact associated with the choosen FactType...\n");

        EntityTransaction loadFactTransact = ph.newTransaction();
        ph.timer().reset_start();
        loadFactTransact.begin();

        rootFact = ph.factDAO().findById(idFact);
        if(rootFact != null)
        {
            if(useALS && rootFact instanceof CompositeFact);
                //factList = ph.factDAO().findAllDescendants((CompositeFact)rootFact);

            else if(rootFact instanceof CompositeFact )
                ph.factDAO().fetchCompositeEager((CompositeFact) rootFact);

            loadFactTransact.commit();
        }
        double loadFactTime = ph.timer().elapsedMs();
        long   loadFactQueries = ph.statistics().getPrepareStatementCount();




        if(rootFact == null)
            System.out.print("\nTest failed: can't find a corresponding fact in DB \n");
        else
        {
            if(VERBOUSE)
            {
                if(useALS && factList != null)
                {
                    System.out.print("\nLoaded Fact (with ALS): \n");
                    for(Fact f : factList)
                        System.out.print("\n - " + f.toString() );
                }
                else
                {
                    System.out.print("\nLoaded Fact: \n");
                    CompositeTree.printTree(rootFact);
                }
                System.out.print("\n");
            }

            // Remove the Fact created in UC2
            EntityTransaction del = ph.newTransaction();
            del.begin();
            ph.factDAO().delete(idFact);
            //ph.remove(rootFact);
            del.commit();

        }




        ph.close();

        System.out.print("Loading fact Elapsed time : \t" +  loadFactTime + "\n");
        System.out.print("Loading fact Executed queries: \t" +  loadFactQueries + "\n");
    }




}
