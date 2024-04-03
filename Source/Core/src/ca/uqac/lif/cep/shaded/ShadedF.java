package ca.uqac.lif.cep.shaded;

/**
 * Multi-shade implements of the LTL "F" operator.
 * @author Sylvain Hallé
 */
public class ShadedF extends ShadedLtlOperator
{
	public static ShadedF F(ShadedConnective operand)
	{
		return F(Polarity.POSITIVE, operand);
	}
	
	protected static ShadedF F(Polarity p, ShadedConnective operand)
  {
		ShadedF f = new ShadedF(operand);
    f.setPolarity(p);
    return f;
  }
	
	public ShadedF(ShadedConnective phi)
	{
		super(phi);
	}
	
	@Override
	public int hashCode()
	{
		return "F".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedF))
		{
			return false;
		}
		ShadedF c = (ShadedF) o;
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
	public ShadedF update(Object event)
	{
		ShadedConnective phi_copy = m_phi.duplicate();
		phi_copy.setPolarity(m_polarity);
		m_operands.add(phi_copy);
		boolean has_green = false, has_null = false;
		for (ShadedConnective op : m_operands)
		{
			op.update(event);
			Color c = op.getValue();
			if (c == Color.GREEN)
			{
				has_green = true;
			}
			else if (c == null)
			{
				has_null = true;
			}
		}
		if (has_green)
		{
			m_color = Color.GREEN;
		}
		else if (has_null)
		{
			m_color = null;
		}
		else
		{
			m_color = Color.RED;
		}
		return this;
	}

	@Override
	public ShadedF duplicate(boolean with_state)
	{
		ShadedF g = new ShadedF(m_phi);
		copyInto(g, with_state);
		return g;
	}
	
	@Override
	public ShadedF duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "F";
	}
	
	@Override
	public String getSymbol()
	{
		return "<b>F</b>";
	}
}
