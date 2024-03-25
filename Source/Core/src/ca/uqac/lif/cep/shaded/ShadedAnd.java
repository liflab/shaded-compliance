package ca.uqac.lif.cep.shaded;

/**
 * Logical AND operator for multi-shade logic.
 * @author Sylvain Hallé
 */
public class ShadedAnd extends ShadedNaryConnective
{
	public static ShadedConnective and(ShadedConnective... operands)
	{
		return and(Polarity.POSITIVE, operands);
	}
	
	protected static ShadedConnective and(Polarity p, ShadedConnective... operands)
  {
		if (operands.length == 1)
		{
			operands[0].setPolarity(p);
			return operands[0];
		}
		ShadedAnd and = new ShadedAnd(operands);
    and.setPolarity(p);
    return and;
  }
	
	/**
	 * Creates a new AND operator.
	 * @param p The polarity of the connective
	 * @param operands The operands of the connective
	 */
	public ShadedAnd(ShadedConnective... operands)
	{
		super(operands);
	}

	@Override
	public ShadedAnd duplicate(boolean with_state)
	{
		ShadedAnd and = new ShadedAnd();
		copyInto(and, with_state);
		return and;
	}

	@Override
	public void update(Object event)
	{
		boolean has_red = false, has_null = false;
		for (ShadedConnective op : m_operands)
		{
			op.update(event);
			if (op.getValue() == Color.RED)
			{
				has_red = true;
			}
			else if (op.getValue() == null)
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
	public String toString()
	{
		return "&";
	}
	
	@Override
	public String getSymbol()
	{
		return "&";
	}
}
