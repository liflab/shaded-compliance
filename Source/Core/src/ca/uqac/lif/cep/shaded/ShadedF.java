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
	public void update(Object event)
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
		return "F";
	}
}
