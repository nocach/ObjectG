package org.objectg.fixtures.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectg.fixtures.domain.Tour;

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
