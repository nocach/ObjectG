package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.ConfigurationBuilder;
import cz.nocach.masaryk.objectg.fixtures.TourSeason;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

/**
 * User: __nocach
 * Date: 28.9.12
 */
public class NullRuleTest {
    @Test
    @Ignore
    public void writeTests(){
//        ConfigurationBuilder conf = ObjectG.prototype();
//        conf.nullObjects();
//        TourSeason tourSeason = ObjectG.unique(TourSeason.class, conf);
//
//        assertNull(tourSeason.getTour());
        fail("waiting for cyclic generation fix");
    }
}
