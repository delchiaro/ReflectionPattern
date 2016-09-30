package reflectionPattern.persistency;

import org.hibernate.Hibernate;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import reflectionPattern.model.operational.QualitativeFact;
import reflectionPattern.model.operational.QuantitativeFact;

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
        List<Fact> results = entityManager.createQuery(
                "select distinct f " +
                        " from Fact f " +
                        " where f.id = :id")
                .setParameter("id", id )
                .getResultList();
        if ( results.size() == 0 ) {
            return null;
        }
        else return results.get(0);

//        return (FactType) results.get( 0 );



    }


    public List<Fact> findAllRoots(boolean eager) {
        List<Fact> facts = entityManager
                .createQuery(   "select f " +
                                "from Fact f " +
                                "where f.parent IS NULL")
                .getResultList();
        if(eager)
            for( Fact f : facts )
                if(f instanceof CompositeFact)
                    fetchCompositeEager((CompositeFact) f);
        return facts;
    }

    public List<CompositeFact> findAllCompositeRoots() {
        List<CompositeFact> facts = entityManager
                                        .createQuery(   "select f " +
                                                " from CompositeFact f " +
                                                " where f.parent IS NULL")
                                        .getResultList();
        return facts;
    }

    public List<CompositeFact> findAllCompositeRootsEager() {
        List<CompositeFact> facts = entityManager
                .createQuery(   "select f " +
                        " from CompositeFact f FETCH ALL PROPERTIES " +
                        " where f.parent IS NULL")
                .getResultList();

        for(CompositeFact f : facts)
            fetchCompositeEager(f);

        return facts;
    }


//    public List<Fact> findAssociatedFacts(FactType type, boolean eager) {
//        return findAssociatedFacts(type.getId(), eager);
//    }
    public List<Fact> findAssociatedFacts(long idType, boolean eager) {
    List<Fact> facts = entityManager
            .createQuery("select f " +
                    "from Fact f " +
                    "where f.type.id = :factTypeID")
            .setParameter("factTypeID", idType)
            .getResultList();
    if (eager && facts != null)
        for (Fact f : facts)
            if (f instanceof CompositeFact)
                fetchCompositeEager((CompositeFact) f);
    return facts;
}

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
    // TODO: reflectionPattern.test performance with annotation EAGER vs annotation LAZY + this function
    public void fetchCompositeEager(CompositeFact fact)
    {
        Hibernate.initialize(fact);
        if(fact.getChilds() != null && fact.getChilds().size() > 0)
        {
            Hibernate.initialize((fact.getChilds()));
            for( Fact child : fact.getChilds() )
                if(child instanceof  CompositeFact)
                    fetchCompositeEager((CompositeFact) child);
        }
    }

    public void fetchEager_TypeUnitPhenom(Fact fact)
    {
        Hibernate.initialize(fact);
        Hibernate.initialize(fact.getType());

        if(fact instanceof CompositeFact)
            for( Fact t : ((CompositeFact) fact).getChilds())
                fetchEager_TypeUnitPhenom(t);

//        else if( fact instanceof QualitativeFact)
//            Hibernate.initialize(((QualitativeFact) fact).getPhenomenon());
//
//        else if( fact instanceof QuantitativeFact)
//                Hibernate.initialize(((QuantitativeFact) fact).getQuantity().getUnit());
    }
    public void fetchFactType(Fact fact)
    {
        Hibernate.initialize(fact.getType());
    }


    public  List<Fact> findAllDescendants(CompositeFact ancestor) {
            List<Fact> facts = entityManager.createQuery(
                            "SELECT f " +
                            "FROM Fact f " +
                            "LEFT JOIN FETCH f.type t " +
                            "WHERE :ancestor member of f.ancestors")
            .setParameter("ancestor", ancestor)
            .getResultList();

            for (Fact f :    facts)
            {
                if(f instanceof QualitativeFact)
                    Hibernate.initialize(((QualitativeFact) f).getPhenomenon());
                else if(f instanceof  QuantitativeFact)
                    Hibernate.initialize(((QuantitativeFact) f).getQuantity().getUnit());
        }

            return facts;
        }



    public void delete(Long id) {
        Fact toRemove = findById(id);
        entityManager.remove(toRemove);
    }




}
