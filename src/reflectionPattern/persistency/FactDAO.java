package reflectionPattern.persistency;

import org.hibernate.Hibernate;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by nagash on 12/09/16.
 */
public class FactDAO {

    private EntityManager entityManager;

    public FactDAO(EntityManager em) {
        entityManager = em;
    }


    public Fact findById(Long id) {
//        List<?> results = entityManager.createQuery(
//                        "select distinct et " +
//                        " from FactType ft " +
//                        " where ft.id = :id")
//                .setParameter("id", id )
//                .getResultList();
//
//        if ( results.size() == 0 ) {
//            return null;
//        }
//
//        return (FactType) results.get( 0 );
        return entityManager.find(Fact.class, id);

    }


    public List<Fact> findAllRoots(boolean eager) {
        List<Fact> facts = entityManager
                .createQuery(   "select f " +
                                "from Fact f " +
                                "where f.parent_fact IS NULL")
                .getResultList();
        if(eager)
            for( Fact f : facts )
                if(f instanceof CompositeFact)
                    fetchCompositeEager((CompositeFact) f);
        return facts;
    }

    public List<CompositeFact> findAllCompositeRoots(boolean eager) {
        List<CompositeFact> facts = entityManager
                                        .createQuery(   "select f " +
                                                " from Fact f " +
                                                " where TYPE(f)= :type AND f.parent_fact IS NULL")
                                        .setParameter("type", CompositeType.class)
                                        .getResultList();

        if(eager && facts != null)
            for( CompositeFact f : facts )
                fetchCompositeEager(f);

        return facts;
    }


//    public List<Fact> findAssociatedFacts(FactType type, boolean eager) {
//        return findAssociatedFacts(type.getId(), eager);
//    }
    public List<Fact> findAssociatedFacts(FactType type, boolean eager) {
        List<Fact> facts = entityManager
                .createQuery(   "select f " +
                                "from Fact f " +
                                "where f.type= :factTypeID")
                .setParameter("factTypeID", type)
                .getResultList();
        if(eager && facts != null)
            for( Fact f : facts )
                if(f instanceof CompositeFact)
                    fetchCompositeEager((CompositeFact) f);
        return facts;
    }


    // Enforce EAGER fetch
    // TODO: test performance with annotation EAGER vs annotation LAZY + this function
    public void fetchCompositeEager(CompositeFact fact)
    {
        if(fact.getChildFacts() != null && fact.getChildFacts().size() > 0)
        {
            Hibernate.initialize((fact.getChildFacts()));
            for( Fact child : fact.getChildFacts() )
                if(child instanceof  CompositeFact)
                    fetchCompositeEager((CompositeFact) child);
        }
    }



    public void delete(Long id) {
        Fact toRemove = findById(id);
        entityManager.remove(toRemove);
    }


}