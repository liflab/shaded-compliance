package ca.uqac.lif.cep.shaded;

/**
 * Multi-shade implements of the LTL "G" operator.
 * @author Sylvain Hallé
 */
public class ShadedG extends ShadedLtlOperator
{
	public static ShadedConnective G(ShadedConnective operand)
	{
		return G(Polarity.POSITIVE, operand);
	}
	
	protected static ShadedConnective G(Polarity p, ShadedConnective operand)
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
	public void update(Object event)
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
