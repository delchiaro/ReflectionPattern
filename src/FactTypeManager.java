import com.sun.istack.internal.NotNull;
import org.testng.internal.Nullable;
import reflectionPattern.dataGeneration.*;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.knowledge.Phenomenon;
import reflectionPattern.modelExtension.MyAggregatePhenomenon;
import reflectionPattern.modelExtension.MySubPhenomeon;
import reflectionPattern.persistency.PersistencyHelper;
import utility.composite.out.CompositeTree;

import javax.persistence.EntityTransaction;
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


    public static void main(String ... params) throws Range.MinimumValueException, Range.InfSupValueException {
        PersistencyHelper.silenceGlobalHibernateLogs();


        boolean ALS = false;
        boolean SINGLE_TABLE = false;

        boolean loop = true;
        while(loop)
        {
            PersistencyHelper ph_1;
            PersistencyHelper ph_2;

            if(SINGLE_TABLE) {
                ph_1 = new PersistencyHelper(PersistencyHelper.Strategy.singleTabl).connect();
                ph_2 = new PersistencyHelper(PersistencyHelper.Strategy.joinTable).connect();
            }
            else{
                ph_1 = new PersistencyHelper(PersistencyHelper.Strategy.joinTable).connect();
                ph_2 = new PersistencyHelper(PersistencyHelper.Strategy.singleTabl).connect();

            }

            String chose;
            Scanner keyboard = new Scanner(System.in);
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.print("~ MANAGE FACT TYPE ~\tNO-ALS-BRANCH"   +
                    "\tTest: " + (SINGLE_TABLE==true ? "single-table" : "join-table") + "\tPersistency: single/join-table" +"\n\n");

            long[] ids_1 = list(ph_1);
            long[] ids_2 = silentList(ph_2);
            System.out.print("\n");
            System.out.print("Usage: [command] [list-index]\n");
            System.out.print("t  - test of performance      \tt [list-index]\n");
            //System.out.print("a  - Ancestor List Strategy toggle (ALS)\t\n");
            System.out.print("j  - join-table enable/disable (join/single table switch)\t\n");
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

                //case "a":  ALS = !ALS; break;
                case "j": SINGLE_TABLE=!SINGLE_TABLE; break;

                case "t":
                    if(pieces.length < 2){ break; }
                    performanceTest(ph_1.getStrategy(), ids_1[Integer.parseInt(pieces[1])-1]);
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

                    generateFixedTypeTree(ph_1, ph_2, depth, width, nPhenoms, subPhenClass);
                    break;

                case "n": generateTypeTree(ph_1, ph_2); break;

                case "d":
                    if(pieces.length < 2){ break; }
                    index = Integer.parseInt(pieces[1]); index--;
                    if(index != null && index>=0) {
                        if(index<ids_1.length)
                             ph_1.factTypeDAO().delete(ids_1[index]);
                        if(index<ids_2.length)
                            ph_2.factTypeDAO().delete(ids_2[index]);
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
                        CompositeTree.printTree( ph_1.factTypeDAO().findById(ids_1[index]));
                    break;

                default: break;
            }


            ph_1.close();
            ph_2.close();

        }


    }



    public static long[] silentList(PersistencyHelper ph) {
        return list(ph, false);
    }

    public static long[] list(PersistencyHelper ph) {
        return list(ph, true);
    }

    public static long[] list(PersistencyHelper ph, boolean verbose)
    {

        EntityTransaction transact = ph.newTransaction();
        transact.begin();
        List<FactType> factTypes = ph.factTypeDAO().findAllRoots(false);
        transact.commit();


        long ids[] = new long[factTypes.size()];
        int i =0;
        for (FactType ft : factTypes) {
            ids[i] = ft.getId();
            if(verbose)
                System.out.print("  " + (++i) + ") " + ft.toString() + "\n");
        }

        return ids;
    }



    public static void generateFixedTypeTree( @NotNull  PersistencyHelper primary, @Nullable PersistencyHelper secondary, Integer depth, Integer width, Integer nUnitPhenom, Class subPhenClass) {
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

        persistFactType(factType, primary, secondary);
    }




    public static void generateTypeTree( @NotNull  PersistencyHelper primary, @Nullable PersistencyHelper secondary) throws  Range.InfSupValueException, Range.MinimumValueException {
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
            persistFactType(factType, primary, secondary);

        }while(   !answerNy("\n\nGo to main menu?")    );
    }





    private static boolean persistFactType(FactType factType, @NotNull  PersistencyHelper primary, @Nullable PersistencyHelper secondary) {
        Scanner keyboard = new Scanner(System.in);

        CompositeTree.printTree(factType);
        if(  answerNy("\n\nPersist this FactType?")  )
        {

            FactType clone = factType.clone();

            EntityTransaction saveTransact = primary.newTransaction();
            saveTransact.begin();
            primary.persist(factType);
            saveTransact.commit();

            if( secondary != null) {
                EntityTransaction saveTransact2 = secondary.newTransaction();
                saveTransact2.begin();
                secondary.persist(clone);
                saveTransact2.commit();
            }

            return true;
        }
        else return false;
    }





    private static void performanceTest(PersistencyHelper.Strategy helperStrategy, long idType)
    {
        UseCaseTest ucTest = new UseCaseTest(helperStrategy, false);
        ucTest.test(idType);
    }

}