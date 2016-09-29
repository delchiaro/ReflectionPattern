package reflectionPattern.persistency;

import org.hibernate.Hibernate;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;

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

        //  Hibelrnate.initialize(comp); // use smply this doesn't work
        for(CompositeType c : types)
            fetchCompositeEager(c);

        return types;
    }



    // Enforce EAGER fetch
    // TODO: reflectionPattern.test performance with annotation EAGER vs annotation LAZY + this function
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



    public void delete(Long id) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        FactType toRemove = findById(id);
        entityManager.remove(toRemove);
        t.commit();
    }


}
