package reflectionPattern.persistency.profiling;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManagerFactory;

/**
 * Created by nagash on 10/09/16.
 */
public class ProfilerHibernateJPA {

    private SessionFactory sessionFactory;
    private Statistics statistics;

    public ProfilerHibernateJPA(EntityManagerFactory emf)
    {
        // Activate hibernate statistics
        sessionFactory =  emf.unwrap(SessionFactory.class);
        statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
    }

    public Statistics getStatistics() {
        return statistics;
    }


}
