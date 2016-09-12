package reflectionPattern.IO;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import reflectionPattern.IO.compositeAdapter.FactTypeCompositeAdapter;
import reflectionPattern.dataGeneration.*;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.persistency.FactTypeDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created by nagash on 12/09/16.
 */
public class FactTypeManager {


    public static void main(String ... params) throws Range.MinimumValueException, Range.InfSupValueException {
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new NullAppender());

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa");
        EntityManager em = emf.createEntityManager();


        FactTypeDAO dao = new FactTypeDAO(em);

        boolean loop = true;
        while(loop)
        {
            String chose;
            Scanner keyboard = new Scanner(System.in);
            System.out.print("\n\n");
            System.out.print("~ MANAGE FACT TYPE ~\n\n");
            long[] ids = list(em);
            System.out.print("\n\n");
            System.out.print("Usage: [command] [list-index]\n");
            System.out.print("n - new random FactType generation (no list-index required)\n");
            System.out.print("f - fixed FactType generation (no list-index required)\n");
            System.out.print("d - delete fact FactType\n");
            System.out.print("s - show FactType tree)\n");
            System.out.print("q - quit (no list-index required)\n");

            System.out.print("> ");

            String data = keyboard.nextLine();
            String[] pieces = data.split("\\s+");

            if(pieces.length < 1)
                return;

            Integer index;
            switch( pieces[0] )
            {
                case "q":  loop = false; break;

                case "f":  generateFixedTypeTree(); break;

                case "n": generateTypeTree(); break;

                case "d":
                    if(pieces.length < 2){ break; }
                    index = Integer.parseInt(pieces[1]); index--;
                    if(index != null && index>=0 && index<ids.length)
                        dao.delete(ids[index]);
                    break;

                case "s":if(pieces.length < 2) { break; }
                    index = Integer.parseInt(pieces[1]); index--;
                    if(index != null && index>=0 && index<ids.length)
                        OutputManager.printFactTypeTree( dao.findById(ids[index]));
                    break;

                default: break;
            }


        }

        em.close();
        emf.close();
    }



    public static long[] list(EntityManager em)
    {
        FactTypeDAO dao = new FactTypeDAO(em);


        EntityTransaction transact = em.getTransaction();
        transact.begin();
        List<FactType> factTypes = dao.findAllRoots(false);
        transact.commit();


        long ids[] = new long[factTypes.size()];
        int i =0;
        for (FactType ft : factTypes) {
            ids[i] = ft.getId();
            System.out.print((++i) + ") " + ft.toString() + "\n");
        }

        return ids;
    }


    public static void generateFixedTypeTree() {
        boolean stop = false;
        do{
            FactType factType = FactTypeGenerator.fixedFactType("FIXED");;
            stop = persistFactType(factType);

        }while(!answerYn("\n\nGo to main menu?"));
    }




    public static void generateTypeTree() throws  Range.InfSupValueException, Range.MinimumValueException {
        Scanner keyboard = new Scanner(System.in);
        FactTypeGenerator typeGenerator = new FactTypeGenerator();

        FactTypeGeneratorParam params = typeGenerator.getParam();
        params.setLeafOnlyAtLowerLevel(false);
        params.depthRange().setInfSup(7, 7);
        params.rootChildsRange().setInfSup(3, 3);
        params.compChildsRange().setSup(6);
        params.phenomsRange().setSup(1);
        params.unitsRange().setSup(1);

        do{
            FactType factType = typeGenerator.randomFactType();
            persistFactType(factType);

        }while(   !answerNy("\n\nGo to main menu?")    );
    }





    private static boolean persistFactType(FactType factType) {
        Scanner keyboard = new Scanner(System.in);

        OutputManager.printFactTypeTree(factType);
        if(  answerNy("\n\nPersist this FactType?")  )
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa");
            EntityManager em = emf.createEntityManager();
            EntityTransaction saveTransact = em.getTransaction();
            saveTransact.begin();
            em.persist(factType);
            saveTransact.commit();
            em.close();
            emf.close();

            return true;
        }
        else return false;
    }

    private static boolean answerNy(String message) {
        System.out.print(message + "\n[y|N] > ");
        Scanner keyboard = new Scanner(System.in);
        String chose = keyboard.nextLine();
        if(chose.equals("y") || chose.equals("Y"))
            return true;
        else return false;
    }
    private static boolean answerYn(String message) {
        System.out.print(message + "\n[Y|n] > ");
        Scanner keyboard = new Scanner(System.in);
        String chose = keyboard.nextLine();
        if(chose.equals("n") || chose.equals("N"))
            return false;
        else return true;
    }
}