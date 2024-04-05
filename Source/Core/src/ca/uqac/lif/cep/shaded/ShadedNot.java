package ca.uqac.lif.cep.shaded;

public class ShadedNot extends ShadedConnective
{
	protected ShadedConnective m_operand;

	public static ShadedConnective not(ShadedConnective operand)
	{
		return not(Polarity.POSITIVE, operand);
	}

	protected static ShadedConnective not(Polarity p, ShadedConnective operand)
	{
		ShadedNot not = new ShadedNot(operand);
		not.setPolarity(p);
		return not;
	}

	public ShadedNot(ShadedConnective operand)
	{
		super();
		m_operand = operand;
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		m_operand.setValue(name, value);
	}

	@Override
	public int hashCode()
	{
		return "!".hashCode();
	}

	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedNot))
		{
			return false;
		}
		ShadedNot c = (ShadedNot) o;
		if (m_color != c.m_color || m_polarity != c.m_polarity)
		{
			return false;
		}
		return m_operand.equals(c.m_operand);
	}


	@Override
	public ShadedNot update(Object event)
	{
		m_operand.update(event);
		return this;
	}

	@Override
	public int getArity()
	{
		return 1;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		if (index == 0)
		{
			return m_operand;
		}
		return null;
	}

	@Override
	public void setPolarity(Polarity p)
	{
		super.setPolarity(p);
		m_operand.setPolarity(Polarized.invert(p));
	}

	@Override
	public Color getValue()
	{
		Color c = m_operand.getValue();
		if (c == Color.RED)
		{
			return Color.GREEN;
		}
		else if (c == Color.GREEN)
		{
			return Color.RED;
		}
		return null;
	}

	@Override
	public ShadedNot duplicate(boolean with_state)
	{
		ShadedNot n = new ShadedNot(m_operand.duplicate(with_state));
		n.setPolarity(m_polarity);
		return n;
	}

	@Override
	public ShadedNot duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String toString()
	{
		return "\u00ac";
	}

	@Override
	public String getSymbol()
	{
		return "\u00ac";
	}
}
