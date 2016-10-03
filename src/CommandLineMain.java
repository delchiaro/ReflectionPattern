import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.persistency.PersistencyHelper;
import reflectionPattern.persistency.PersistencyHelper.Strategy;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by nagash on 30/09/16.
 */
public class CommandLineMain {

    static String outputDirectory = "";
    static boolean printNumberOfQuery = false;
    static String appendString = "";
    static boolean ALS = false;
    static boolean singleTable = false;
    static int iterations = 1;
    static Strategy strategy = Strategy.joinTable;

    private static void print(String s) {System.out.print(s);}
    private static void println(String s) {System.out.print(s+"\n");}


    public static void printParamList() {
        println("Test with the UseCase 1-2-3 all the FactType presents in the database reflection-ancestors-join-table");
        println("(or reflection-ancestors-single-table if using option s)\n");
        println("Usage:   command OUT_DIRECTORY [options] \n\n");
        println("    LONG       SHORT                  DESCRIPTION\n");
        println(" iter NUM   |  i NUM  -   Specify the number NUM of test to do (the first will be cold test, the others hot tests)");
        println(" als        |  a      -   Enable Ancestor List Strategy");
        println(" single     |  s      -   Use SingleTable instead of JoinTable");
        println(" noq                  -   Print the Number Of Query executed for each, in the output file");
        println(" append STR           -   Append the string STR at the end of each output file name");




    }
    public static void processParam(String... params)
    {
        outputDirectory = params[0];

        for(int i = 1; i < params.length; i++)
            switch(params[i])
            {

                case "noq":
                    printNumberOfQuery = true;                          break;


                case "append":
                    appendString = "_" + params[++i];                   break;


                case "als":
                case "a": ALS = true;                                   break;


                case "single":
                case "singleTable":
                case "singletable":
                case "single-table":
                case "s": strategy = Strategy.singleTable;              break;


                case "iter":
                case "i": iterations = Integer.parseInt(params[++i]);   break;
            }
    }

    public static void main(String... params)
    {

        if(params.length < 1)
        {
            printParamList();
            return;
        }

        processParam(params);
        long ids[] = listSilently(strategy);
        testAllCold(ids, strategy);
        for(int i=1; i < iterations; i++ )
            testAll(ids, strategy, i, false);

    }



    private static void testAllCold(long[] typeIds, Strategy s) {
        testAll(typeIds, s, 1, true);
    }

    private static void testAll(long[] typeIds, Strategy s, int iter, boolean coldExec) {

        if(coldExec && iter > 1)
            return;

        UseCaseTest ucTest = new UseCaseTest(s, UseCaseTest.OutputMode.FILE);
        PersistencyHelper.silenceGlobalHibernateLogs();


        //for(int i = 0; i < 4 ; i++)
        for(long idType : typeIds)
        {
            //    long idType = typeIds[i];
            Path path = Paths.get( outputDirectory + "/" +  ( s==Strategy.singleTable ? "singleT" : "joinT") + "_" +  ( ALS ? "AlsON" : "AlsOFF"));
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("Can't create directory. End.");
                return;
            }
            PersistencyHelper ph = new PersistencyHelper(s, false).connect();
            FactType t = ph.factTypeDAO().findById(idType);

            ucTest.setOutputFileName(path + "/out-"+  t.toString() + "_" +
                    (coldExec ? "cold" : "hot" + iter)  + appendString + ".txt");
            ucTest.setPrintNQueries(printNumberOfQuery);
            ucTest.test(idType, ALS);
        }

    }

    private static long[] listSilently(Strategy s) {
        return list(s, false);
    }

    private static long[] list(Strategy s, boolean verbose) {
        List<CompositeType> factTypes;

        PersistencyHelper ph = new PersistencyHelper(s, false).connect();
        factTypes = ph.factTypeDAO().findAllCompositeRoots();
        ph.close();

        long ids[] = new long[factTypes.size()];

        int i =0;
        for (FactType ft : factTypes) {
            ids[i] = ft.getId();
            if(verbose)
                System.out.print("  " + (i+1) + ") " + ft.toString() + "\n");
            i++;
        }

        return ids;
    }
}
