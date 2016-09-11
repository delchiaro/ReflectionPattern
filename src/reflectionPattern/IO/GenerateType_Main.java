package reflectionPattern.IO;

import reflectionPattern.IO.compositeAdapter.FactTypeCompositeAdapter;
import reflectionPattern.data.DataModelGenerator;
import reflectionPattern.model.knowledge.FactType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Scanner;

/**
 * Created by nagash on 10/09/16.
 */
public class GenerateTypeMain {

    public static void main(String args[]) throws DataModelGenerator.UnknownFactTypeException {
        Scanner keyboard = new Scanner(System.in);
        DataModelGenerator dmg = new DataModelGenerator();

        boolean stop = false;
        do{
            FactType factType = dmg.randomKnowledgeGenerator(5, 8, 8, 5, 3);
            System.out.print( OutputManager.adapterExplorer( new FactTypeCompositeAdapter(factType)));

            System.out.print("\n\nPersist this FactType?\n[y|N] > ");
            String chose = keyboard.nextLine();
            if(chose.equals("y") || chose.equals("Y"))
            {
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa");
                EntityManager em = emf.createEntityManager();
                EntityTransaction saveTransact = em.getTransaction();
                saveTransact.begin();
                em.persist(factType);
                saveTransact.commit();
                em.close();
                emf.close();

                System.out.print("\n\nMake a new FactType generation?\n[Y|n] > ");
                chose = keyboard.nextLine();
                if(chose.equals("n") || chose.equals("N"))
                    stop=true;
            }
        }while(!stop);
    }


}
