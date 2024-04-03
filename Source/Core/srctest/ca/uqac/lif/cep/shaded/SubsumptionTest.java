package ca.uqac.lif.cep.shaded;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.cep.shaded.Polarized.Polarity;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;
import static ca.uqac.lif.cep.shaded.LtlTuplesTest.map;

public class SubsumptionTest
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
		Subsumption comp = new Subsumption();
		assertTrue(comp.inRelation(phi2, phi1));
		assertTrue(comp.inRelation(phi1, phi2));
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
		Subsumption comp = new Subsumption();
		assertTrue(comp.inRelation(phi2, phi1));
		assertFalse(comp.inRelation(phi1, phi2));
	}
	
	@Test
	public void test3()
	{
		ShadedConnective phi1 = G(eq(fetch("a"), 1));
		phi1.update(map("a", 1));
		phi1.update(map("a", 2));
		phi1.update(map("a", 1));
		ShadedConnective phi2 = phi1.duplicate();
		phi2.update(map("a", 1));
		phi2.update(map("a", 1));
		phi2.update(map("a", 2));
		Subsumption comp = new Subsumption();
		assertTrue(comp.inRelation(phi2, phi1));
		assertTrue(comp.inRelation(phi1, phi2));
	}
	
	@Test
	public void test4()
	{
		ShadedConnective phi1 = F(eq(fetch("a"), 1));
		phi1.update(map("a", 1));
		phi1.update(map("a", 2));
		phi1.update(map("a", 1));
		ShadedConnective phi2 = phi1.duplicate();
		phi2.update(map("a", 1));
		phi2.update(map("a", 2));
		phi2.update(map("a", 2));
		Subsumption comp = new Subsumption();
		assertTrue(comp.inRelation(phi2, phi1));
		assertFalse(comp.inRelation(phi1, phi2));
	}
	
	@Test
	public void test5()
	{
		// a = 1 && (b = 1 || c = 1)
		ShadedConnective phi1 = and(
				eq(fetch("a"), 1),
				or(
						eq(fetch("b"), 1),
						eq(fetch("c"), 1))
				);
		phi1.update(map("a", 1));
		phi1.update(map("b", 1));
		phi1.update(map("c", 1));
		ShadedConnective phi2 = phi1.duplicate();
		phi2.update(map("a", 1));
		phi2.update(map("b", 0));
		phi2.update(map("c", 1));
		Subsumption comp = new Subsumption();
		assertTrue(comp.inRelation(phi2, phi1));
		assertFalse(comp.inRelation(phi1, phi2));
	}
	
	@Test
	public void testPolarity1()
	{
		ShadedConnective phi1 = G(eq(fetch("a"), 1));
		phi1.update(map("a", 1));
		ShadedConnective phi2 = phi1.duplicate();
		phi2.setPolarity(Polarity.NEGATIVE);
		phi2.update(map("a", 1));
		Subsumption comp = new Subsumption();
		// None is subsumed because of different polarity
		assertFalse(comp.inRelation(phi2, phi1));
		assertFalse(comp.inRelation(phi1, phi2));
	}
	
	@Test
	public void testColor1()
	{
		ShadedConnective phi1 = G(eq(fetch("a"), 1));
		phi1.update(map("a", 1));
		ShadedConnective phi2 = phi1.duplicate();
		phi2.update(map("a", 0));
		Subsumption comp = new Subsumption();
		assertTrue(comp.inRelation(phi2, phi1));
		assertFalse(comp.inRelation(phi1, phi2));
	}
}
