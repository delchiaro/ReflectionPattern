package reflectionPattern.test.persistency;


import org.hibernate.Hibernate;
import org.junit.Ignore;
import org.junit.Test;
import reflectionPattern._deprecated.dataGenerator.RandomFactGenerator;
import reflectionPattern.dataGeneration.FactTypeGenerator;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import reflectionPattern.persistency.PersistencyHelper;
import utility.composite.out.CompositeTree;

import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Created by nagash on 09/09/16.
 */
public class PersistencyPerformanceTest {


    @Ignore
    @Test
    public void lazy_load_test_1() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        CompositeType c = helper.entityManager().find(CompositeType.class,1l);

        boolean b = Hibernate.isInitialized(c);


        helper.close();
        FactType d = c.getChilds().iterator().next(); // error !! lazy loading and now  em  and emf are closed

    }
    @Ignore
    @Test
    public void lazy_load_test_2() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        CompositeType c = helper.entityManager().find(CompositeType.class,1l);

        boolean b = Hibernate.isInitialized(c);


        helper.entityManager().close();
        FactType d = c.getChilds().iterator().next(); // error !! lazy loading and now  em is closed!
        helper.close();
    }

    @Ignore
    @Test
    public void lazy_load_test_3() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        CompositeType c = helper.entityManager().find(CompositeType.class,1l);

        boolean b = Hibernate.isInitialized(c);

        helper.entityManager().detach(c);

        FactType d = c.getChilds().iterator().next(); // No error.. detach won't works as expected!
        helper.close();
    }

    @Ignore
    @Test
    public void lazy_load_test_4() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        CompositeType c = (CompositeType)helper.entityManager().find(FactType.class,1l);;

        boolean b = Hibernate.isInitialized(c);


        FactType d = c.getChilds().iterator().next(); // No error..Cast works also with entity!

        helper.close();

    }

    @Ignore
    @Test
    public void lazy_load_test_5() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        List<CompositeType> results = helper.entityManager().createQuery(
                "select distinct ct " +
                        " from CompositeType ct " +
                        " where ct.id = :id")
                .setParameter("id", 1l )
                .getResultList();

        CompositeType c = results.get(0);

        helper.close();

        FactType d = c.getChilds().iterator().next(); // Error!! Lazy loading works also with query


    }

    @Ignore
    @Test
    public void lazy_load_test_6() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        List<CompositeType> results = helper.entityManager().
                                        createQuery(    "select ft " +
                                                        " from CompositeType ft " +
                                                        " where ft.parent IS NULL")
                                                .getResultList();

        CompositeType c = results.get(0);

        helper.close();

        FactType d = c.getChilds().iterator().next(); // Error!! Lazy loading works also with this query


    }



    @Ignore
    @Test
    public void lazy_load_test_7() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        List<CompositeType> results = helper.factTypeDAO().findAllCompositeRoots();
        CompositeType c = results.get(0);

        helper.close();

        FactType d = c.getChilds().iterator().next(); // Error!! Lazy loading works also with  factTypeDAO


    }
    @Test
    public void lazy_load_test_8() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        List<CompositeType> results = helper.factTypeDAO().findAllCompositeRootsEager();
        CompositeType c = results.get(0);
        helper.close();
        FactType d = c.getChilds().iterator().next(); // Ok!
    }

    @Test
    public void lazy_load_test_9() {
        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        List<CompositeType> results = helper.factTypeDAO().findAllCompositeRoots();
        CompositeType c = results.get(0);

        helper.factTypeDAO().fetchCompositeEager(c);
        helper.close();
        FactType d = c.getChilds().iterator().next(); // Ok!
    }



    @Ignore
    @org.junit.Test
    public void loadType_saveFact() {
        // HIBERNATE SAVING ENTITIES:

        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        transact.begin();
            List<CompositeType> factTypes = helper.factTypeDAO().findAllCompositeRoots();
        transact.commit();

        for (FactType ft : factTypes) {
            helper.entityManager().detach(ft);
        }

        double time = helper.timer().elapsedMs();
        long count = helper.statistics().getPrepareStatementCount();

        helper.close();


        System.out.print("\n\n\nLoaded FactType trees: \n\n" );

        for (FactType ft : factTypes) {
            CompositeTree.printTree(ft);
        }

        System.out.print("\n\n\n Prepared Statements: " + count);
        System.out.print("\n Time: " + time + "\n\n\n");
    }




    private static final int N_ROOT_FACTS = 2;


    @Ignore
    @org.junit.Test
    public void saveType_saveFacts() throws CompositeFact.IllegalFactTypeException {

        PersistencyHelper.silenceGlobalHibernateLogs();

        FactType rootType = FactTypeGenerator.fixedFactType("");


        Fact rootFacts[] = new Fact[N_ROOT_FACTS];
        for (int i = 0; i < N_ROOT_FACTS; i++)
            rootFacts[i] = RandomFactGenerator.randomFact(rootType);



        PersistencyHelper helper = new PersistencyHelper().connect();

        EntityTransaction   saveTransact = helper.newTransaction();

        helper.timer().reset_start();

        saveTransact.begin();
            helper.persist(rootType);
            for (int i = 0; i < N_ROOT_FACTS; i++)
                helper.persist(rootFacts[i]);
        saveTransact.commit();

        double time = helper.timer().elapsedMs();
        long count = helper.statistics().getPrepareStatementCount();


        helper.close();



        System.out.print("\n\n\nPersisted fixed FactType tree: \n\n" );
            CompositeTree.printTree(rootType);

        System.out.print("\n\n\nPersisted facts: \n\n" );
        for (int i = 0; i < N_ROOT_FACTS; i++)
            CompositeTree.printTree(rootFacts[i]);


        System.out.print("\n\n\nPrepared statements: " + count );
        System.out.print("\n Time: " + time + "\n\n\n");

    }



}