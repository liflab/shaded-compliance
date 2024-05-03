/*
    A tree-based process compliance library
    Copyright (C) 2024 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.shaded;

/**
 * Multi-shade implements of the LTL "O" operator.
 * @author Sylvain Hallé
 */
public class ShadedO extends ShadedPastLtlOperator
{
	public static ShadedO O(ShadedConnective operand)
	{
		return O(Polarity.POSITIVE, operand);
	}
	
	protected static ShadedO O(Polarity p, ShadedConnective operand)
  {
		ShadedO f = new ShadedO(operand);
    f.setPolarity(p);
    return f;
  }

	public ShadedO(ShadedConnective phi)
	{
		super(phi);
	}
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("O" + (getValue() == Color.RED ? "-" : "+"));
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
	
	@Override
	public int hashCode()
	{
		return "O".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedO))
		{
			return false;
		}
		ShadedO c = (ShadedO) o;
		if (m_color != c.m_color || m_polarity != c.m_polarity || !m_phi.equals(c.m_phi) || m_operands.size() != c.m_operands.size())
		{
			return false;
		}
		for (int i = 0; i < m_operands.size(); i++)
		{
			if (!m_operands.get(i).sameAs(c.m_operands.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public ShadedO update(Object event)
	{
		m_events.add(event);
		ShadedConnective phi_copy = m_phi.duplicate();
		phi_copy.setPolarity(m_polarity);
		m_operands.add(phi_copy);
		for (int i = m_events.size() - 1; i >= 0; i--)
		{
			phi_copy.update(m_events.get(i));
		}
		boolean has_green = false, has_null = false;
		for (ShadedConnective op : m_operands)
		{
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
	public ShadedO duplicate(boolean with_state)
	{
		ShadedO g = new ShadedO(m_phi);
		copyInto(g, with_state);
		return g;
	}
	
	@Override
	public ShadedO duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "O";
	}
	
	@Override
	public String getSymbol()
	{
		return "<b>O</b>";
	}
	
	@Override
	public ShadedO cloneNode()
	{
		return duplicate(false);
	}
}

