package test.persistency;


import reflectionPattern.dataGeneration.FactGenerator;
import reflectionPattern.dataGeneration.FactTypeGenerator;
import reflectionPattern.modelALS.knowledge.CompositeType;
import reflectionPattern.modelALS.knowledge.FactType;
import reflectionPattern.modelALS.operational.CompositeFact;
import reflectionPattern.modelALS.operational.Fact;
import reflectionPattern.persistency.PersistencyHelper;
import reflectionPattern.utility.composite.out.CompositeTree;
import reflectionPattern.utility.compositeWithAncestors.out.CompositeTreeALS;

import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Created by nagash on 09/09/16.
 */
public class PersistencyPerformanceTest {



    @org.junit.Test
    public void loadType_saveFact() {
        // HIBERNATE SAVING ENTITIES:

        PersistencyHelper.silenceGlobalHibernateLogs();
        PersistencyHelper helper = new PersistencyHelper().connect();
        EntityTransaction   transact = helper.newTransaction();

        helper.timer().reset_start();

        transact.begin();
            List<CompositeType> factTypes = helper.factTypeDAO().findAllCompositeRoots(true);
        transact.commit();

        double time = helper.timer().elapsedMs();
        long count = helper.statistics().getPrepareStatementCount();

        helper.close();


        System.out.print("\n\n\nLoaded FactType trees: \n\n" );

        for (FactType ft : factTypes) {
            CompositeTreeALS.printTree(ft);
        }

        System.out.print("\n\n\n Prepared Statements: " + count);
        System.out.print("\n Time: " + time + "\n\n\n");
    }




    private static final int N_ROOT_FACTS = 2;


    @org.junit.Test
    public void saveType_saveFacts() throws CompositeFact.IllegalFactTypeException {

        PersistencyHelper.silenceGlobalHibernateLogs();

        FactType rootType = FactTypeGenerator.fixedFactType("");


        Fact rootFacts[] = new Fact[N_ROOT_FACTS];
        for (int i = 0; i < N_ROOT_FACTS; i++)
            rootFacts[i] = FactGenerator.randomFact(rootType);



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
            CompositeTreeALS.printTree(rootType);

        System.out.print("\n\n\nPersisted facts: \n\n" );
        for (int i = 0; i < N_ROOT_FACTS; i++)
            CompositeTreeALS.printTree(rootFacts[i]);


        System.out.print("\n\n\nPrepared statements: " + count );
        System.out.print("\n Time: " + time + "\n\n\n");

    }



}