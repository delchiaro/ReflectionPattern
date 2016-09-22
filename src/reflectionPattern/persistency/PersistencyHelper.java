package reflectionPattern.persistency;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import reflectionPattern.persistency.profiling.TimeProfiler;

import javax.persistence.*;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by nagash on 13/09/16.
 */
public class PersistencyHelper
{

    private final static String SINGLE_TABLE_UNIT = "single-table";
    private final static String JOIN_TABLE_UNIT = "join-table";

     public final static String DEFAULT_UNIT = SINGLE_TABLE_UNIT;  // <<< CHANGE THIS to change default DB globally
     //public final static String DEFAULT_UNIT = JOIN_TABLE_UNIT;


    public enum Unit { singleTabl , joinTable; }



    private boolean useStatistics = true;
    private final String unitName;


    public PersistencyHelper() {
        unitName = DEFAULT_UNIT;
    }
    public PersistencyHelper(boolean useStatistics) {
        unitName = DEFAULT_UNIT;
        this.useStatistics = useStatistics;
    }
    public PersistencyHelper(Unit unit) {
        switch (unit)
        {
            case singleTabl:    unitName = SINGLE_TABLE_UNIT; break;
            case joinTable:     unitName = JOIN_TABLE_UNIT;   break;
            default:            unitName = DEFAULT_UNIT;      break;
        }
    }
    public PersistencyHelper(Unit unit, boolean useStatistics) {
        this(unit);
        this.useStatistics = useStatistics;
    }







    private EntityManagerFactory emf = null;
    private EntityManager em = null;
    private Statistics statistics = null;
    private FactDAO factDAO = null;
    private FactTypeDAO factTypeDAO = null;
    private TimeProfiler timer = new TimeProfiler();


    public PersistencyHelper connect() {
        if(isClosed())
        {
            emf = Persistence.createEntityManagerFactory(this.unitName);
            em = emf.createEntityManager();

            if(useStatistics) {
                SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
                statistics = sessionFactory.getStatistics();
                statistics.setStatisticsEnabled(true);
            }

            return this;
        }
        else return null; //before connect, you must close the old connection.
    }


    public EntityTransaction newTransaction() {
        if(isClosed()) return null;
        else return em.getTransaction();
    }

    public Statistics statistics() {
        return statistics;
    }

    public TimeProfiler timer() {
        return timer;
    }

    public FactDAO factDAO() {
        if(factDAO == null)
           if(isClosed())
               return null;
           else return factDAO = new FactDAO(em);
        else return factDAO;
    }
    public FactTypeDAO factTypeDAO() {
        if(factTypeDAO == null)
            if(isClosed()) return null;
            else return factTypeDAO = new FactTypeDAO(em);
        else return factTypeDAO;
    }


    public void persist(Object object) {
        if(!isClosed())
            em.persist(object);
    }

    public <T> T find(Class<T> aClass, Object o) {
        return em.find(aClass, o);
    }
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return em.find(aClass, o, lockModeType);
    }
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return em.find(aClass, o, map);
    }
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType,Map<String, Object> map) {
        return em.find(aClass, o, lockModeType, map);
    }

    public EntityManager entityManager() {
        return entityManager();
    }


    public void close() {
        em.close();
        emf.close();
        em = null;
        emf = null;
        statistics = null;
        factDAO = null;
        factTypeDAO = null;
    }

    public boolean isClosed() {
        if(em==null && emf==null)
            return true;
        else return false;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(emf != null && em != null)
            close();
    }


    public static void silenceGlobalHibernateLogs() {
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new NullAppender());
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

}
