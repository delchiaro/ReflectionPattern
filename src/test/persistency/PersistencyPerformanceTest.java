package test.persistency;


import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.Ignore;
import profiling.ProfilerHibernateJPA;
import profiling.TimeProfiler;
import reflectionPattern.IO.OutputManager;
import reflectionPattern.dataGeneration.FactGenerator;
import reflectionPattern.dataGeneration.FactTypeGenerator;
import reflectionPattern.dataGeneration.RandomUtils;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import reflectionPattern.persistency.FactTypeDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by nagash on 09/09/16.
 */
public class PersistencyPerformanceTest {



    @org.junit.Test
    public void testDAO() {
        // HIBERNATE SAVING ENTITIES:
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa");
        EntityManager em = emf.createEntityManager();
        FactTypeDAO dao = new FactTypeDAO(em);

        ProfilerHibernateJPA profiler = new ProfilerHibernateJPA(emf);

        TimeProfiler timeProf = new TimeProfiler();
        EntityTransaction transact = em.getTransaction();
        timeProf.resetStart();
        transact.begin();
            List<CompositeType> factTypes = dao.findAllCompositeRoots(true);
        transact.commit();
        long count = profiler.getStatistics().getPrepareStatementCount();
        double time = timeProf.elapsedMs();

        em.close();
        emf.close();

        for (FactType ft : factTypes) {
            OutputManager.printFactTypeTree(ft);
        }

        System.out.print("\n\n\n Prepared Statements: " + count);
        System.out.print("\n\n\n Time: " + time);
    }




    private static final int N_ROOT_FACTS = 2;


    @Ignore
    @org.junit.Test
    public void persistencyTestSave() throws CompositeFact.IllegalFactTypeException {

        FactType rootType = FactTypeGenerator.fixedFactType("");


        Fact rootFacts[] = new Fact[N_ROOT_FACTS];
        for (int i = 0; i < N_ROOT_FACTS; i++)
            rootFacts[i] = FactGenerator.randomFact(rootType);


        // HIBERNATE SAVING ENTITIES:
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa");

        // Activate hibernate statistics
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);

        EntityManager em = emf.createEntityManager();
        EntityTransaction saveTransact = em.getTransaction();


        ProfilerHibernateJPA profiler = new ProfilerHibernateJPA(emf);

        saveTransact.begin();
        em.persist(rootType);
        for (int i = 0; i < N_ROOT_FACTS; i++)
            em.persist(rootFacts[i]);

        saveTransact.commit();
        em.close();

        System.out.print("\n\n\nPrepared statements: " +profiler.getStatistics().getPrepareStatementCount() + "\n\n" );
    }


    @Ignore
    @org.junit.Test
    public void persistencyTestLoad() {

    }

}