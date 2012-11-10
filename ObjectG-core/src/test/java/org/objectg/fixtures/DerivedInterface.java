package org.objectg.fixtures;

import org.objectg.fixtures.domain.ITour;

/**
 * User: __nocach
 * Date: 1.11.12
 */
public interface DerivedInterface extends ParentInterface{
    public ITour getTour();
    public void setTour(ITour tour);
}
