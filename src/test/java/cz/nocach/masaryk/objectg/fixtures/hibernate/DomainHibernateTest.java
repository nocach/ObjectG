package cz.nocach.masaryk.objectg.fixtures.hibernate;

import cz.nocach.masaryk.objectg.fixtures.Person;
import cz.nocach.masaryk.objectg.fixtures.Person2Address;
import cz.nocach.masaryk.objectg.fixtures.Tour;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.mapping.PersistentClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * User: __nocach
 * Date: 10.10.12
 */
public class DomainHibernateTest {
    private SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
        sessionFactory.getAllClassMetadata();
    }

    @After
    public void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @Test
    public void testDomainModelIsMappedProperly() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save( new Tour());
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from Tour" ).list();
        session.getTransaction().commit();
        session.close();
    }

}
