package ca.uqac.lif.cep.shaded;

import static org.junit.Assert.*;

import org.junit.Test;

import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.LtlTuplesTest.map;

public class TreeComparatorTest
{
	@Test
	public void test1()
	{
		ShadedConnective phi1 = G(eq(fetch("a"), 1));
		phi1.update(map("a", 1));
		phi1.update(map("a", 2));
		phi1.update(map("a", 1));
		ShadedConnective phi2 = phi1.duplicate();
		phi2.update(map("a", 1));
		phi2.update(map("a", 1));
		phi2.update(map("a", 2));
		TreeRenderer.render(phi2, System.out);
		assertTrue(TreeComparator.isSubsumed(phi2, phi1));
		assertTrue(TreeComparator.isSubsumed(phi1, phi2));
	}
	
	@Test
	public void test2()
	{
		ShadedConnective phi1 = G(eq(fetch("a"), 1));
		phi1.update(map("a", 1));
		phi1.update(map("a", 2));
		phi1.update(map("a", 1));
		ShadedConnective phi2 = phi1.duplicate();
		phi2.update(map("a", 1));
		phi2.update(map("a", 2));
		phi2.update(map("a", 2));
		TreeRenderer.render(phi2, System.out);
		assertTrue(TreeComparator.isSubsumed(phi2, phi1));
		assertFalse(TreeComparator.isSubsumed(phi1, phi2));
	}
}
