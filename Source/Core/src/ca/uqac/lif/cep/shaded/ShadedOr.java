package ca.uqac.lif.cep.shaded;

/**
 * Logical OR operator for multi-shade logic.
 * @author Sylvain Hallé
 */
public class ShadedOr extends ShadedNaryConnective
{
	public static ShadedConnective implies(ShadedConnective left, ShadedConnective right)
	{
		return or(new ShadedNot(left), right);
	}
	
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
	public int hashCode()
	{
		return "|".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedOr))
		{
			return false;
		}
		ShadedOr c = (ShadedOr) o;
		if (m_color != c.m_color || m_polarity != c.m_polarity || m_operands.size() != c.m_operands.size())
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
		return "\u2228";
	}
	
	@Override
	public String getSymbol()
	{
		return "\u2228";
	}
}
