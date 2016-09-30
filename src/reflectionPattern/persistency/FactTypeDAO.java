package reflectionPattern.persistency;

import org.hibernate.Hibernate;
import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Unit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;
import java.util.List;

/**
 * Created by nagash on 12/09/16.
 */
public class FactTypeDAO {

    private EntityManager entityManager;

    public FactTypeDAO(EntityManager em) {
        entityManager = em;
    }


    public FactType findById(Long id) {
        List<FactType> results = entityManager.createQuery(
                                " select distinct t " +
                                " from FactType t " +
                                " where t.id = :id"   )
                                .setParameter("id", id )
                                .getResultList();
        if ( results.size() == 0 ) {
            return null;
        }
        else return results.get(0);
        //return entityManager.find(FactType.class, id);
    }


    public List<FactType> findAllRoots() {
        List<FactType> types =  entityManager
                .createQuery(   "select ft " +
                                "from FactType ft " +
                                "where ft.parent IS NULL")
                .getResultList();
        return types;
    }

    public List<CompositeType> findAllCompositeRoots() {
        List<CompositeType> types = entityManager
                                        .createQuery(   "select ft " +
                                                " from CompositeType ft " +
                                                " where ft.parent IS NULL")
                                        .getResultList();


        return types;
    }
    public List<CompositeType> findAllCompositeRootsEager() {
        List<CompositeType> types = entityManager
                .createQuery(   "select ft " +
                        " from CompositeType ft FETCH ALL PROPERTIES " +
                        " where TYPE(ft)= :type AND ft.parent IS NULL")
                .setParameter("type", CompositeType.class)
                .getResultList();

        for(CompositeType c : types)
            fetchCompositeEager(c);

        return types;
    }



    // Enforce EAGER fetch
    public void fetchCompositeEager(CompositeType comp)
    {
        Hibernate.initialize(comp);

        if(comp.getChilds() != null && comp.getChilds().size() > 0)
        {
            Hibernate.initialize((comp.getChilds()));

            for( FactType child : comp.getChilds() )
                if(child instanceof  CompositeType)
                    fetchCompositeEager((CompositeType) child);
        }
    }

    public void fetchTypeEager_PhenomsUnits(FactType type)
    {
        Hibernate.initialize(type);

        if(type instanceof CompositeType)
            for( FactType t : ((CompositeType) type).getChilds())
                fetchTypeEager_PhenomsUnits(t);

        else if( type instanceof QualitativeType )
            for(Phenomenon p : ((QualitativeType) type).getLegalPhenomenons())
                Hibernate.initialize(p);

        else if( type instanceof QuantitativeType )
            for(Unit u : ((QuantitativeType)type).getLegalUnits() )
                Hibernate.initialize(u);

    }


    public void delete(Long id) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        FactType toRemove = findById(id);
        entityManager.remove(toRemove);
        t.commit();
    }


}
