package ca.uqac.lif.cep.shaded;

import ca.uqac.lif.xml.XPathExpression;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;

public class ShadedForAll extends ShadedQuantifier
{
	public static ShadedForAll all(String x, String pi, ShadedConnective phi) throws XPathParseException
	{
		return new ShadedForAll(x, XPathExpression.parse(pi + "/text()"), phi);
	}
	
	public ShadedForAll(String x, XPathExpression pi, ShadedConnective phi)
	{
		super(x, pi, phi);
	}

	@Override
	public String getSymbol()
	{
		return "\u2200" + m_x + "\u2208" + m_pi;
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedForAll))
		{
			return false;
		}
		return sameSubtrees((ShadedQuantifier) f);
	}

	@Override
	public ShadedConnective duplicate(boolean with_state)
	{
		ShadedForAll sfa = new ShadedForAll(m_x, m_pi, m_phi);
		sfa.m_polarity = m_polarity;
		if (with_state)
		{
			sfa.m_color = m_color;
			for (ShadedConnective c : m_instances)
			{
				sfa.m_instances.add(c.duplicate(with_state));
			}
		}
		return sfa;
	}

	@Override
	protected Color updateVerdict()
	{
		boolean has_false = false, has_null = false;
		for (ShadedConnective c : m_instances)
		{
			Color v = c.getValue();
			if (v == null)
			{
				has_null = true;
			}
			if (v == Color.RED)
			{
				has_false = true;
			}
		}
		if (has_false)
		{
			return Color.RED;
		}
		if (has_null)
		{
			return null;
		}
		return Color.GREEN;
	}
}
