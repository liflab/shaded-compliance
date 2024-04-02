package ca.uqac.lif.cep.shaded;

/**
 * Logical OR operator for multi-shade logic.
 * @author Sylvain Hallé
 */
public class ShadedOr extends ShadedNaryConnective
{
	public static ShadedConnective or(ShadedConnective... operands)
	{
		return or(Polarity.POSITIVE, operands);
	}
	
	protected static ShadedConnective or(Polarity p, ShadedConnective... operands)
  {
		if (operands.length == 1)
		{
			operands[0].setPolarity(p);
			return operands[0];
		}
		ShadedOr or = new ShadedOr(operands);
    or.setPolarity(p);
    return or;
  }
	
	/**
	 * Creates a new OR operator.
	 * @param operands The operands of the connective
	 */
	public ShadedOr(ShadedConnective... operands)
	{
		super(operands);
	}

	@Override
	public ShadedOr duplicate(boolean with_state)
	{
		ShadedOr and = new ShadedOr();
		copyInto(and, with_state);
		return and;
	}

	@Override
	public ShadedOr update(Object event)
	{
		boolean has_green = false, has_null = false;
		for (ShadedConnective op : m_operands)
		{
			op.update(event);
			if (op.getValue() == Color.GREEN)
			{
				has_green = true;
			}
			else if (op.getValue() == null)
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
	public String toString()
	{
		return "|";
	}
	
	@Override
	public String getSymbol()
	{
		return "|";
	}
}
