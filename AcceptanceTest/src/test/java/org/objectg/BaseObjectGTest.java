package org.objectg;

import junit.framework.Assert;
import org.junit.Rule;
import org.objectg.integration.ObjectGTestRule;

/**
 * <p>
 *     Base test supporting ObjectG lifecycle managing
 * </p>
 * <p>
 * User: __nocach
 * Date: 23.1.13
 * </p>
 */
public abstract class BaseObjectGTest extends Assert{
	@Rule
	public ObjectGTestRule objectGTestRule = new ObjectGTestRule();
}
