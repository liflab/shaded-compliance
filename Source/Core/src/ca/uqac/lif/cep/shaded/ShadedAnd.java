package ca.uqac.lif.cep.shaded;

import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

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
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("&" + (getValue() == Color.RED ? "-" : "+"));
		boolean first = true;
		for (ShadedConnective c : m_operands)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				out.append(",");
			}
			c.toString(out);
		}
		out.append(")");
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
	public int hashCode()
	{
		return "&".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedAnd))
		{
			return false;
		}
		ShadedAnd c = (ShadedAnd) o;
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
	public ShadedAnd duplicate(boolean with_state)
	{
		ShadedAnd and = new ShadedAnd();
		copyInto(and, with_state);
		return and;
	}

	@Override
	public ShadedAnd update(Object event)
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
		return this;
	}
	
	@Override
	public String getSymbol()
	{
		return "\u2227";
	}
}
