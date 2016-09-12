package test;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import org.hibernate.AssertionFailure;
import org.junit.Ignore;
import org.junit.Test;
import profiling.ProfilerHibernateJPA;
import profiling.TimeProfiler;
import reflectionPattern.IO.OutputManager;
import reflectionPattern.dataGeneration.FactGenerator;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import reflectionPattern.persistency.FactDAO;
import reflectionPattern.persistency.FactTypeDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by nagash on 12/09/16.
 */
public class UseCaseTest {


    private static void silentLog() {
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new NullAppender());
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

    @Test
    public void UC1_UC2() {
        silentLog();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa");
        EntityManager em = emf.createEntityManager();

        FactTypeDAO typeDao = new FactTypeDAO(em);


        List<CompositeType> rootTypes;
        CompositeType rootType;

        ProfilerHibernateJPA profiler = new ProfilerHibernateJPA(emf);
        TimeProfiler timer = new TimeProfiler();



        //     U.C.1: apertura di una nuova visita medica, istanziazione dei Fact (vuoti) corrispondenti alla struttura dei FactType.


        // TODO: same test but with Eager select (have to modify annotation in FactType, or make a new xml settings for FactType to load).
        System.out.print("...Lazy loading of all composite type root...\n");
        System.out.print("...Eager fetching of first composite type root...\n");

        EntityTransaction loadTransact = em.getTransaction();
        timer.resetStart();
        {
            loadTransact.begin();
            {
                rootTypes = typeDao.findAllCompositeRoots(false);
                rootType = rootTypes.get(0);
                typeDao.fetchCompositeEager(rootType);
            }
            loadTransact.commit();
        }
        double loadTime = timer.elapsedMs();
        long    loadQueries = profiler.getStatistics().getPrepareStatementCount();





        // U.C.2: il salvataggio di una visita medica compilata

        System.out.print("\n\n\n...Generating Fact with some random value...\n");
        Fact fRoot = FactGenerator.randomFact(rootType);


        System.out.print("...Persisting the generated Fact...\n");

        EntityTransaction saveTransact = em.getTransaction();
        timer.resetStart();
        {
            saveTransact.begin();
            {
                em.persist(fRoot);
            }
            saveTransact.commit();
        }
        double saveTime = timer.elapsedMs();
        long   saveQueries = profiler.getStatistics().getPrepareStatementCount() - loadQueries;


        em.close();
        emf.close();


        System.out.print("\n\n\nLoaded Fact Type: \n");
        OutputManager.printFactTypeTree(rootType);

        System.out.print("\n\n\nSaved Fact: \n");
        OutputManager.printFactTree(fRoot);


        System.out.print("\nElapsed load time: \t" +  loadTime + "\n");
        System.out.print("Executed load queries: \t" +  loadQueries + "\n");


        System.out.print("Elapsed save time: \t" +  saveTime + "\n");
        System.out.print("Executed save queries: \t" +  saveQueries + "\n");



    }








    @Test
    public void UC3() {

        silentLog();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa");
        EntityManager em = emf.createEntityManager();

        FactTypeDAO typeDao = new FactTypeDAO(em);


        List<CompositeType> rootTypes;
        CompositeType rootType = null;

        ProfilerHibernateJPA profiler = new ProfilerHibernateJPA(emf);
        TimeProfiler timer = new TimeProfiler();


        System.out.print("...Lazy loading of all composite type root...\n");
        System.out.print("...Eager fetching of first composite type root...\n");

        EntityTransaction loadTransact = em.getTransaction();
        timer.resetStart();
        {
            loadTransact.begin();
            {
                rootTypes = typeDao.findAllCompositeRoots(false);
                rootType = rootTypes.get(0);
                typeDao.fetchCompositeEager(rootType);
            }
            loadTransact.commit();
        }
        double loadTypeTime = timer.elapsedMs();
        long loadTypeQueries = profiler.getStatistics().getPrepareStatementCount();



        if(rootType == null){
            System.out.print("\n\n\nFake test: can't find a rootType in DB \n");
        }
        else
        {


            FactDAO fDao = new FactDAO(em);
            List<Fact> associatedFacts;
            Fact rootFact = null;

            System.out.print("...Loading Facts associated with the first FactType...\n");

            EntityTransaction loadFactTransact = em.getTransaction();
            timer.resetStart();
            {
                loadFactTransact.begin();
                {
                    associatedFacts = fDao.findAssociatedFacts(rootType, false);
                    if(associatedFacts.size() > 0)
                    {
                        rootFact = associatedFacts.get(0);
                        if (rootFact instanceof CompositeFact)
                            fDao.fetchCompositeEager((CompositeFact) rootFact);
                    }

                }
                loadFactTransact.commit();
            }
            double loadFactTime = timer.elapsedMs();
            long   loadFactQueries = profiler.getStatistics().getPrepareStatementCount() - loadTypeQueries;


            System.out.print("\n\n\nLoaded Fact Type: \n");
            OutputManager.printFactTypeTree(rootType);

            if(rootFact == null) {
                System.out.print("\n\n\nFake test: can't find a corresponding fact in DB \n");
            }
            else
            {
                System.out.print("\n\n\nLoaded Fact: \n");
                OutputManager.printFactTree(rootFact);
            }



            System.out.print("\nLoading type Elapsed time : \t" +  loadTypeTime + "\n");
            System.out.print("Loading type Executed queries: \t" +  loadTypeQueries + "\n");

            System.out.print("Loading fact Elapsed time : \t" +  loadFactTime + "\n");
            System.out.print("Loading fact Executed queries: \t" +  loadFactQueries + "\n");

        }

        em.close();
        emf.close();



    }

}
