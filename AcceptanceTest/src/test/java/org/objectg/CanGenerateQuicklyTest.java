package org.objectg;

import org.junit.Test;
import org.objectg.fixtures.big.BigHierarchyRoot;

/**
 * User: __nocach
 * Date: 16.11.12
 */
public class CanGenerateQuicklyTest extends BaseObjectGTest{

	@Test
	public void bigHierarchy(){
		ObjectG.unique(BigHierarchyRoot.class);
	}
}
