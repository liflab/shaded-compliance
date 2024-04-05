package ca.uqac.lif.cep.shaded;

import org.junit.Test;

import ca.uqac.lif.cep.shaded.ShadedConnective.Color;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;
import ca.uqac.lif.xml.XmlElement;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

import static org.junit.Assert.*;

import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedForAll.all;
import static ca.uqac.lif.cep.shaded.ShadedQuantifiedVariable.v;

public class QuantifiersTest
{
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
}
