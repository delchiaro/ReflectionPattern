import com.sun.istack.internal.NotNull;
import org.testng.internal.Nullable;
import reflectionPattern.dataGeneration.*;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.knowledge.Phenomenon;
import reflectionPattern.modelExtension.MyAggregatePhenomenon;
import reflectionPattern.modelExtension.MySubPhenomeon;
import reflectionPattern.persistency.PersistencyHelper;
import utility.composite.out.CompositeTree;
import reflectionPattern.persistency.PersistencyHelper.Strategy;

import javax.persistence.EntityTransaction;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static reflectionPattern.dataGeneration.RandomUtils.timeMillis;
import static utility.io.UtilsIO.answerNy;

/**
 * Created by nagash on 12/09/16.
 */
public class FactTypeManager {


    private static final int DEFAULT_FIXED_TREE_DEPTH = 4;
    private static final int DEFAULT_FIXED_TREE_WIDTH = 2;
    private static final int DEFAULT_FIXED_TREE_N_PHEN = 2;

    private static Strategy s1 =  Strategy.singleTable;
    private static Strategy s2 =  Strategy.joinTable;

    private static boolean ALS = false;




    public static void main(String ... params) throws Range.MinimumValueException, Range.InfSupValueException
    {
        PersistencyHelper.silenceGlobalHibernateLogs();



        boolean SINGLE_TABLE = true;

        boolean loop = true;
        while(loop)
        {


            if(SINGLE_TABLE) {
                s1 = Strategy.singleTable;
                s2 = Strategy.joinTable;
            }
            else{
                s1 = Strategy.joinTable;
                s2 = Strategy.singleTable;
            }

            String chose;
            Scanner keyboard = new Scanner(System.in);
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.print("~ MANAGE FACT TYPE ~\t"  + (ALS==true ? "ALS-ON" : "ALS-OFF") +
                    "\t\tTest: " + (SINGLE_TABLE==true ? "single-table" : "join-table") + "\tPersistency: single/join-table" +"\n\n");

            long[] ids_1 = list(s1);
            long[] ids_2 = listSilently(s2);
            System.out.print("\n");
            System.out.print("Usage: [command] [list-index]\n");
            System.out.print("t  - test of performance      \tt [list-index]\n");
            System.out.print("ta - test all                 \tta  \n");

            System.out.print("a  - Ancestor List Strategy toggle (ALS)\t\n");
            System.out.print("j  - join-table enable/disable (join/single table switch)\t\n");


            System.out.print("g  - generate all useCase fixed\t\n");

            System.out.print("f  - fixed FactType generation\tf [depth] [width] [nUnits/nPhenoms]\n");
            System.out.print("fa - fixed + aggregate phens  \tf [depth] [width] [nUnits/nPhenoms]\n");
            System.out.print("fs - fixed + sub phens        \tf [depth] [width] [nUnits/nPhenoms]\n");
            System.out.print("n  - new random FactType generation\n");
            System.out.print("d  - delete fact FactType     \td [list-index]\n");
            System.out.print("s  - show FactType tree       \ts [list-index]\n");

            // System.out.print("r  - reset DB (delete all)\n");
            System.out.print("q  - quit\n");

            System.out.print("> ");

            String data = keyboard.nextLine();
            String[] pieces = data.split("\\s+");

            if(pieces.length < 1)
                return;

            Integer index;
            switch( pieces[0] )
            {
                case "q":  loop = false; break;

                case "a":  ALS = !ALS; break;
                case "j": SINGLE_TABLE=!SINGLE_TABLE; break;

                case "t":
                    if(pieces.length < 2){ break; }
                    performanceTest(s1, ids_1[Integer.parseInt(pieces[1])-1]);
                    break;

                case "ta":
                    testAll(ids_1, s1);
                    break;

                case "g":
                    generateMyUseCaseTypes();
                    break;

                case "fa":
                case "fs":
                case "f":
                    Integer depth = null;
                    Integer width = null;
                    Integer nPhenoms = null;
                    if(pieces.length > 1)
                        depth = Integer.parseInt(pieces[1]);
                    if(pieces.length > 2)
                        width = Integer.parseInt(pieces[2]);
                    if(pieces.length > 3)
                        nPhenoms = Integer.parseInt(pieces[3]);

                    Class subPhenClass = Phenomenon.class;
                    if(pieces[0].equals("fa")) subPhenClass = MyAggregatePhenomenon.class;
                    else if(pieces[0].equals("fs")) subPhenClass = MySubPhenomeon.class;

                    generateFixedTypeTreeVerbose(depth, width, nPhenoms, subPhenClass, s1, s2);
                    break;

                case "n": generateTypeTree(s1, s2); break;

                case "d":
                    if(pieces.length < 2){ break; }
                    index = Integer.parseInt(pieces[1]); index--;
                    if(index != null && index>=0) {
                        if(index<ids_1.length)
                            deleteType(ids_1[index], s1);
                        if(index<ids_2.length)
                            deleteType(ids_2[index], s2);
                    }
                    break;


//                case "r":
//                    EntityTransaction transact = ph.newTransaction();
//                    transact.begin();
//                    List<Fact> facts = ph.factDAO().findAllRoots(false);
//                    transact.commit();
//                    for(Fact f : facts)
//                        ph.factDAO().delete(f.getId());
//                    for(long i : ids)
//                        ph.factTypeDAO().delete(i);


                case "s":if(pieces.length < 2) { break; }
                    index = Integer.parseInt(pieces[1]); index--;
                    if(index != null && index>=0 && index<ids_1.length)
                        showType(ids_1[index], s1);
                    break;

                default: break;
            }




        }


    }









    private static void generateMyUseCaseTypes() {
        int width = DEFAULT_FIXED_TREE_WIDTH;
        int nPhenoms = DEFAULT_FIXED_TREE_N_PHEN;

        for( int i = 5; i < 9; i++) {
            System.out.print("\nGenerating: " + "DEPTH:"+i + "_width:"+width + "___" + MySubPhenomeon.class.getSimpleName()+":" + nPhenoms );
            generateFixedTypeTreeSilently(i, width, nPhenoms, MySubPhenomeon.class, s1, s2);
            System.out.print("\t\tDONE!");
        }
        for( int i = 5; i < 9; i++) {
            System.out.print("\nGenerating: " + "DEPTH:" + i + "_width:" + width + MySubPhenomeon.class.getSimpleName() + ":" + nPhenoms);
            generateFixedTypeTreeSilently(i, width, nPhenoms, MyAggregatePhenomenon.class, s1, s2);
            System.out.print("\t\tDONE!");
        }
    }

    private static void testAll(long[] typeIds, Strategy s) {

        UseCaseTest ucTest = new UseCaseTest(s, UseCaseTest.VerbouseMode.FILE);
        for(int i = 0; i < 5 ; i++)
//        for(long idType : typeIds)
        {
            long idType = typeIds[i];
            Path path = Paths.get("/home/nagash/Desktop/UseCasetests/");
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("Can't create directory. Exiting.");
                return;
            }
            ucTest.setOutputFileName("/home/nagash/Desktop/UseCasetests/out-"+ idType + ".xls");
            ucTest.test(idType, ALS);
        }

    }





    public static void showType(long typeId, Strategy s) {
        PersistencyHelper ph = new PersistencyHelper(s, false).connect();
        FactType t = ph.factTypeDAO().findById(typeId);
        CompositeTree.printTree(t); // use this before close, otherwise will get error (lazy load of t)
        ph.close();
    }



    private static long[] listSilently(Strategy s) {
        return list(s, false);
    }
    private static long[] list(Strategy s) {
        return list(s, true);
    }
    private static long[] list(Strategy s, boolean verbose) {
        List<CompositeType> factTypes;

        PersistencyHelper ph = new PersistencyHelper(s, false).connect();
        EntityTransaction transact = ph.newTransaction();
        //transact.begin();
         factTypes = ph.factTypeDAO().findAllCompositeRoots();
        //transact.commit();
        ph.close();

        long ids[] = new long[factTypes.size()];

        int i =0;
        for (FactType ft : factTypes) {
            ids[i] = ft.getId();
            if(verbose)
                System.out.print("  " + (++i) + ") " + ft.toString() + "\n");
        }

        return ids;
    }





    private static void     generateFixedTypeTreeSilently  (Integer depth, Integer width, Integer nUnitPhenom, Class subPhenClass, @NotNull  Strategy s1, @Nullable Strategy s2) {
        FactType ft = generateFixedTypeTreeNoPersist(depth, width,nUnitPhenom, subPhenClass, s1, s2);
        persistFactTypeSilently(ft, s1, s2);
    }
    private static void     generateFixedTypeTreeVerbose   (Integer depth, Integer width, Integer nUnitPhenom, Class subPhenClass, @NotNull  Strategy s1, @Nullable Strategy s2) {
        FactType ft = generateFixedTypeTreeNoPersist(depth, width,nUnitPhenom, subPhenClass, s1, s2);
        persistFactType(ft, s1, s2);
    }
    private static FactType generateFixedTypeTreeNoPersist (Integer depth, Integer width, Integer nUnitPhenom, Class subPhenClass, @NotNull  Strategy s1, @Nullable Strategy s2) {

        if(depth == null)
            depth = DEFAULT_FIXED_TREE_DEPTH;
        if(width == null)
            width = DEFAULT_FIXED_TREE_WIDTH;
        if(nUnitPhenom == null)
            nUnitPhenom = DEFAULT_FIXED_TREE_N_PHEN;

        FactTypeGeneratorParam params = new FactTypeGeneratorParam();
        params.setSubPhenomenon(subPhenClass);
        params.setLeafOnlyAtLowerLevel(true);
        try
        {
            params.getDepthRange().setFixedInfSup(depth);
            params.getCompChildsRange().setFixedInfSup(width);
            params.getRootChildsRange().setFixedInfSup(width);
            params.getPhenomsRange().setFixedInfSup(nUnitPhenom);
            params.getUnitsRange().setFixedInfSup(nUnitPhenom);
        }
        catch (Range.MinimumValueException e) {   e.printStackTrace();  }


        FactTypeGenerator gen = new FactTypeGenerator(params);
        FactType factType =  gen.randomFactType();
        factType.setTypeName("COMPOSITE_DEPTH:"+depth+"__WIDTH:"+ width +"__"+ subPhenClass.getSimpleName() + ":"+nUnitPhenom  + "___" + timeMillis() );
        return factType;
    }

    private static void generateTypeTree(@NotNull  Strategy s1, @Nullable Strategy s2) throws  Range.InfSupValueException, Range.MinimumValueException {
        Scanner keyboard = new Scanner(System.in);
        FactTypeGenerator typeGenerator = new FactTypeGenerator();

        FactTypeGeneratorParam params = typeGenerator.getParam();
        params.setLeafOnlyAtLowerLevel(false);
        params.getDepthRange().setInfSup(7, 7);
        params.getRootChildsRange().setInfSup(3, 3);
        params.getCompChildsRange().setSup(6);
        params.getPhenomsRange().setSup(1);
        params.getUnitsRange().setSup(1);

        do{
            FactType factType = typeGenerator.randomFactType();
            persistFactType(factType, s1, s2);

        }while(   !answerNy("\n\nGo to main menu?")    );
    }




    private static void persistFactTypeSilently (FactType factType, @NotNull  Strategy s1, @Nullable Strategy s2) {
        FactType clone = factType.clone();
        PersistencyHelper ph1 = new PersistencyHelper(s1).connect();
        EntityTransaction t = ph1.newTransaction();
        t.begin();
        ph1.persist(factType);
        t.commit();
        ph1.close();

        if( s2 != null) {
            PersistencyHelper ph2 = new PersistencyHelper(s2).connect();
            EntityTransaction t1 = ph2.newTransaction();
            t1.begin();
            ph2.persist(clone);
            t1.commit();
            ph2.close();
        }
    }
    private static boolean persistFactType      (FactType factType, @NotNull  Strategy s1, @Nullable Strategy s2) {
        Scanner keyboard = new Scanner(System.in);

        CompositeTree.printTree(factType);
        if(  answerNy("\n\nPersist this FactType?")  ) {
            persistFactTypeSilently(factType, s1, s2);
            return true;
        }
        else return false;
    }





    private static void performanceTest (Strategy helperStrategy, long idType)
    {
        UseCaseTest ucTest = new UseCaseTest(helperStrategy, UseCaseTest.VerbouseMode.CONSOLLE);
        ucTest.test(idType, ALS);
    }


    private static void deleteType (long id, Strategy helperStrategy)
    {
        PersistencyHelper ph = new PersistencyHelper(helperStrategy, false).connect();
        ph.factTypeDAO().delete(id);
        ph.close();
    }
}