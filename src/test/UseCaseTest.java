package test;

import org.junit.Test;
import reflectionPattern.persistency.PersistencyHelper;
import reflectionPattern.persistency.profiling.ProfilerHibernateJPA;
import reflectionPattern.persistency.profiling.TimeProfiler;
import reflectionPattern.dataGeneration.FactGenerator;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import reflectionPattern.persistency.FactDAO;
import reflectionPattern.persistency.FactTypeDAO;
import reflectionPattern.utility.composite.out.CompositeTree;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by nagash on 12/09/16.
 */
public class UseCaseTest {

    @Test
    public void UC1_UC2() {


        PersistencyHelper.silenceGlobalHibernateLogs();

        PersistencyHelper ph = new PersistencyHelper(true).connect();


        List<CompositeType> rootTypes;
        CompositeType rootType;


        //     U.C.1: apertura di una nuova visita medica, istanziazione dei Fact (vuoti) corrispondenti alla struttura dei FactType.


        // TODO: same test but with Eager select (have to modify annotation in FactType, or make a new xml settings for FactType to load).
        System.out.print("...Lazy loading of all composite type root...\n");
        System.out.print("...Eager fetching of first composite type root...\n");


        EntityTransaction loadTransact = ph.newTransaction();

        ph.timer().reset_start();

        loadTransact.begin();
        {
            rootTypes = ph.factTypeDAO().findAllCompositeRoots(false);
            rootType = rootTypes.get(0);
            ph.factTypeDAO().fetchCompositeEager(rootType);
        }
        loadTransact.commit();

        double loadTime = ph.timer().elapsedMs();
        long    loadQueries = ph.statistics().getPrepareStatementCount();





        // U.C.2: il salvataggio di una visita medica compilata

        System.out.print("\n\n\n...Generating Fact with some random value...\n");
        Fact fRoot = FactGenerator.randomFact(rootType);


        System.out.print("...Persisting the generated Fact...\n");

        EntityTransaction saveTransact = ph.newTransaction();

        ph.timer().reset_start();

        saveTransact.begin();
        {
                ph.persist(fRoot);
        }
        saveTransact.commit();

        double saveTime = ph.timer().elapsedMs();
        long   saveQueries = ph.statistics().getPrepareStatementCount() - loadQueries;


        ph.close();

        System.out.print("\n\n\nLoaded Fact Type: \n");
        CompositeTree.printTree(rootType);

        System.out.print("\n\n\nSaved Fact: \n");
        CompositeTree.printTree(fRoot);


        System.out.print("\nElapsed load time: \t" +  loadTime + "\n");
        System.out.print("Executed load queries: \t" +  loadQueries + "\n");


        System.out.print("Elapsed save time: \t" +  saveTime + "\n");
        System.out.print("Executed save queries: \t" +  saveQueries + "\n");



    }








    @Test
    public void UC3() {

        PersistencyHelper.silenceGlobalHibernateLogs();

        PersistencyHelper ph = new PersistencyHelper(true).connect();

        List<CompositeType> rootTypes;
        CompositeType rootType = null;


        System.out.print("...Lazy loading of all composite type root...\n");
        System.out.print("...Eager fetching of first composite type root...\n");

        EntityTransaction loadTransact = ph.newTransaction();

        ph.timer().reset_start();

        loadTransact.begin();
        {
                rootTypes = ph.factTypeDAO().findAllCompositeRoots(false);
                rootType = rootTypes.get(0);
                ph.factTypeDAO().fetchCompositeEager(rootType);
        }
        loadTransact.commit();

        double loadTypeTime = ph.timer().elapsedMs();
        long loadTypeQueries = ph.statistics().getPrepareStatementCount();



        if(rootType == null){
            System.out.print("\n\n\nFake test: can't find a rootType in DB \n");
            ph.close();
        }
        else
        {


            List<Fact> associatedFacts;
            Fact rootFact = null;

            System.out.print("...Loading Facts associated with the first FactType...\n");

            EntityTransaction loadFactTransact = ph.newTransaction();
            ph.timer().reset_start();
            {
                loadFactTransact.begin();
                {
                    associatedFacts = ph.factDAO().findAssociatedFacts(rootType, false);
                    if(associatedFacts.size() > 0)
                    {
                        rootFact = associatedFacts.get(0);
                        if (rootFact instanceof CompositeFact)
                            ph.factDAO().fetchCompositeEager((CompositeFact) rootFact);
                    }

                }
                loadFactTransact.commit();
            }
            double loadFactTime = ph.timer().elapsedMs();
            long   loadFactQueries = ph.statistics().getPrepareStatementCount() - loadTypeQueries;


            ph.close();

            System.out.print("\n\n\nLoaded Fact Type: \n");
            CompositeTree.printTree(rootType);

            if(rootFact == null) {
                System.out.print("\n\n\nFake test: can't find a corresponding fact in DB \n");
            }
            else
            {
                System.out.print("\n\n\nLoaded Fact: \n");
                CompositeTree.printTree(rootFact);
            }



            System.out.print("\nLoading type Elapsed time : \t" +  loadTypeTime + "\n");
            System.out.print("Loading type Executed queries: \t" +  loadTypeQueries + "\n");

            System.out.print("Loading fact Elapsed time : \t" +  loadFactTime + "\n");
            System.out.print("Loading fact Executed queries: \t" +  loadFactQueries + "\n");

        }





    }

}
