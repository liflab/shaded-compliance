package ca.uqac.lif.cep.shaded;

import ca.uqac.lif.xml.XPathExpression;

public class ShadedExists extends ShadedQuantifier
{
	public ShadedExists(String x, XPathExpression pi, ShadedConnective phi)
	{
		super(x, pi, phi);
	}

	@Override
	public String getSymbol()
	{
		return "\u2203" + m_x + "\u2208" + m_pi;
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedExists))
		{
			return false;
		}
		return sameSubtrees((ShadedQuantifier) f);
	}

	@Override
	public ShadedConnective duplicate(boolean with_state)
	{
		ShadedExists sfa = new ShadedExists(m_x, m_pi, m_phi);
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
		boolean has_true = false, has_null = false;
		for (ShadedConnective c : m_instances)
		{
			Color v = c.getValue();
			if (v == null)
			{
				has_null = true;
			}
			if (v == Color.GREEN)
			{
				has_true = true;
			}
		}
		if (has_true)
		{
			return Color.GREEN;
		}
		if (has_null)
		{
			return null;
		}
		return Color.RED;
	}
}
