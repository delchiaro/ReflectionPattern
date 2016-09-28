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
        return entityManager.find(FactType.class, id);
    }


    public List<FactType> findAllRoots(boolean eager) {
        List<FactType> types =  entityManager
                .createQuery(   "select ft " +
                                "from FactType ft " +
                                "where ft.parent IS NULL")
                .getResultList();

        if(eager && types != null)
            for(FactType t : types)
                if(t instanceof CompositeType)
                    fetchCompositeEager((CompositeType) t);

        return types;
    }

    public List<CompositeType> findAllCompositeRoots(boolean eager) {
        List<CompositeType> types = entityManager
                                        .createQuery(   "select ft " +
                                                " from FactType ft " +
                                                " where TYPE(ft)= :type AND ft.parent IS NULL")
                                        .setParameter("type", CompositeType.class)
                                        .getResultList();

        if(eager && types != null)
            for(CompositeType t : types)
                fetchCompositeEager((CompositeType) t);

        return types;
    }



    // Enforce EAGER fetch
    // TODO: reflectionPattern.test performance with annotation EAGER vs annotation LAZY + this function
    public void fetchCompositeEager(CompositeType comp)
    {
//        EntityTransaction loadTransact = entityManager.getTransaction();
//        loadTransact.begin();

        if(comp.getChilds() != null && comp.getChilds().size() > 0)
        {
            Hibernate.initialize((comp.getChilds()));

            for( FactType child : comp.getChilds() )
                if(child instanceof  CompositeType)
                    fetchCompositeEager((CompositeType) child);
        }

//        loadTransact.commit();
    }



    public void delete(Long id) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        FactType toRemove = findById(id);
        entityManager.remove(toRemove);
        t.commit();
    }


}
