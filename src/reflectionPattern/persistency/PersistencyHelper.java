package reflectionPattern.persistency;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import org.hibernate.CacheMode;
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

    //private final static Strategy DEFAULT_STRATEGY = Strategy.singleTable;
    private final static Strategy DEFAULT_STRATEGY = Strategy.joinTable;

    Strategy strategy = DEFAULT_STRATEGY;

    private static final boolean DISABLE_CACHE = false;

    public enum Strategy {singleTable, joinTable; }



    private boolean useStatistics = true;
    private final String unitName;


    public PersistencyHelper() {
        this(DEFAULT_STRATEGY);
    }
    public PersistencyHelper(boolean useStatistics) {
       this(DEFAULT_STRATEGY, useStatistics);
    }
    public PersistencyHelper(Strategy strategy) {
        this.strategy = strategy;
        switch (strategy)
        {
            case singleTable:    unitName = SINGLE_TABLE_UNIT; break;
            case joinTable:     unitName = JOIN_TABLE_UNIT;   break;
            default:            unitName = JOIN_TABLE_UNIT;   break;
        }
    }
    public PersistencyHelper(Strategy strategy, boolean useStatistics) {
        this(strategy);
        this.useStatistics = useStatistics;
    }




    public Strategy getStrategy() { return this.strategy; }



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
            if(DISABLE_CACHE) emf.getCache().evictAll(); // disable cache

            em = emf.createEntityManager();


            if(useStatistics) {
                SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
                statistics = sessionFactory.getStatistics();
                statistics.setStatisticsEnabled(true);
            }

            if(DISABLE_CACHE) {
                org.hibernate.Session hibernateSession = (org.hibernate.Session) em.getDelegate();
                hibernateSession.setCacheMode(CacheMode.IGNORE);
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
        //if(!isClosed())
            em.persist(object);
    }

    public void remove(Object object) {
        em.remove(object);
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
        return this.em;
    }

    public EntityManagerFactory entityManagerFactory() {
        return this.emf;
    }

    public SessionFactory getSessionFactory()
    {
        return emf.unwrap(SessionFactory.class);
    }


    public void close() {
        if(isClosed() == false) {
            em.close();
            emf.close();
            em = null;
            emf = null;
            statistics = null;
            factDAO = null;
            factTypeDAO = null;
        }
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
        //Configuration config = new Configuration();
        //config.setProperty(org.hibernate.cfg.Environment.SHOW_SQL, "false");

        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new NullAppender());
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

}
