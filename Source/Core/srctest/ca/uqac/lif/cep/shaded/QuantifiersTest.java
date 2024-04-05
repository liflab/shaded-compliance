package ca.uqac.lif.cep.shaded;

import org.junit.Test;

import ca.uqac.lif.cep.shaded.DotRenderer.Format;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;
import ca.uqac.lif.xml.XmlElement;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

import static org.junit.Assert.*;

import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedForAll.all;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;
import static ca.uqac.lif.cep.shaded.ShadedQuantifiedVariable.v;
import static ca.uqac.lif.cep.shaded.ShadedUnaryPredicate.even;

public class QuantifiersTest
{
	public static final TreeRenderer s_renderer = new TreeRenderer(false);
	
	@Test
	public void test1() throws XPathParseException, XmlParseException
	{
		ShadedConnective phi = all("x", "m/foo/text()", leq(v("x"), 3));
		phi.update(XmlElement.parse("<m><foo>2</foo></m>"));
		assertEquals(Color.GREEN, phi.getValue());
	}
	
	@Test
	public void test2() throws XPathParseException, XmlParseException
	{
		ShadedConnective phi = all("x", "m/foo/text()", leq(v("x"), 3));
		phi.update(XmlElement.parse("<m><foo>2</foo><foo>10</foo></m>"));
		assertEquals(Color.RED, phi.getValue());
		// Further updates do not change the result
		phi.update(XmlElement.parse("<m><foo>0</foo><foo>1</foo></m>"));
		assertEquals(Color.RED, phi.getValue());
	}
	
	@Test
	public void testSubsumption1() throws XPathParseException, XmlParseException
	{
		ShadedConnective phi = all("x", "m/foo/text()", leq(v("x"), 3));
		ShadedConnective phi1 = phi.duplicate().update(XmlElement.parse("<m><foo>2</foo><foo>10</foo></m>"));
		ShadedConnective phi2 = phi.duplicate().update(XmlElement.parse("<m><foo>20</foo><foo>10</foo></m>"));
		s_renderer.toImage(phi1, "/tmp/phil1.png", Format.PNG);
		s_renderer.toImage(phi2, "/tmp/phil2.png", Format.PNG);
		Subsumption rel = new Subsumption(false);
		assertTrue(rel.inRelation(phi2, phi1));
		assertFalse(rel.inRelation(phi1, phi2));
	}
	
	@Test
	public void testSubsumption2() throws XPathParseException, XmlParseException
	{
		ShadedConnective phi = all("x", "m/foo/text()", or(leq(v("x"), 3), even(v("x"))));
		ShadedConnective phi1 = phi.duplicate().update(XmlElement.parse("<m><foo>2</foo><foo>10</foo></m>"));
		ShadedConnective phi2 = phi.duplicate().update(XmlElement.parse("<m><foo>20</foo><foo>10</foo></m>"));
		s_renderer.toImage(phi1, "/tmp/phi1.png", Format.PNG);
		s_renderer.toImage(phi2, "/tmp/phi2.png", Format.PNG);
		Subsumption rel = new Subsumption(false);
		assertTrue(rel.inRelation(phi2, phi1));
		assertFalse(rel.inRelation(phi1, phi2));
	}
}
