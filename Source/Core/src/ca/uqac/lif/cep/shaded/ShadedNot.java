package ca.uqac.lif.cep.shaded;

public class ShadedNot extends ShadedConnective
{
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
	
	protected ShadedConnective m_operand;
	
	public ShadedNot(ShadedConnective operand)
	{
		super();
		m_operand = operand;
	}
	
	@Override
	public void update(Object event)
	{
		m_operand.update(event);
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
		return "!";
	}
	
	@Override
	public String getSymbol()
	{
		return "!";
	}
}
