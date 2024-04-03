package ca.uqac.lif.cep.shaded;

/**
 * Multi-shade implements of the LTL "G" operator.
 * @author Sylvain Hallé
 */
public class ShadedG extends ShadedLtlOperator
{
	public static ShadedG G(ShadedConnective operand)
	{
		return G(Polarity.POSITIVE, operand);
	}
	
	protected static ShadedG G(Polarity p, ShadedConnective operand)
  {
		ShadedG g = new ShadedG(operand);
    g.setPolarity(p);
    return g;
  }
	
	public ShadedG(ShadedConnective phi)
	{
		super(phi);
	}
	
	@Override
	public int hashCode()
	{
		return "G".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedG))
		{
			return false;
		}
		ShadedG c = (ShadedG) o;
		if (m_color != c.m_color || m_polarity != c.m_polarity || !m_phi.equals(c.m_phi) || m_operands.size() != c.m_operands.size())
		{
			return false;
		}
		for (int i = 0; i < m_operands.size(); i++)
		{
			if (!m_operands.get(i).equals(c.m_operands.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public ShadedG update(Object event)
	{
		ShadedConnective phi_copy = m_phi.duplicate();
		phi_copy.setPolarity(m_polarity);
		m_operands.add(phi_copy);
		boolean has_red = false, has_null = false;
		for (ShadedConnective op : m_operands)
		{
			op.update(event);
			Color c = op.getValue();
			if (c == Color.RED)
			{
				has_red = true;
			}
			else if (c == null)
			{
				has_null = true;
			}
		}
		if (has_red)
		{
			m_color = Color.RED;
		}
		else if (has_null)
		{
			m_color = null;
		}
		else
		{
			m_color = Color.GREEN;
		}
		return this;
	}

	@Override
	public ShadedG duplicate(boolean with_state)
	{
		ShadedG g = new ShadedG(m_phi);
		copyInto(g, with_state);
		return g;
	}
	
	@Override
	public ShadedG duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "G";
	}
	
	@Override
	public String getSymbol()
	{
		return "G";
	}
}
