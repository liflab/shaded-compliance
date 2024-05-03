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
 * Multi-shade implements of the LTL "G" operator.
 * @author Sylvain Hallé
 */
public class ShadedG extends ShadedLtlOperator
{
	public static ShadedG G(ShadedConnective operand)
	{
		return G(Polarity.POSITIVE, operand);
	}
	
	protected static ShadedG G(Polarity p, ShadedConnective operand)
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
	public int hashCode()
	{
		return "G".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedG))
		{
			return false;
		}
		ShadedG c = (ShadedG) o;
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
	public ShadedG update(Object event)
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
		return this;
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
	protected void toString(StringBuilder out)
	{
		out.append("G" + (getValue() == Color.RED ? "-" : "+"));
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
	public String getSymbol()
	{
		return "<b>G</b>";
	}
	
	@Override
	public ShadedConnective cloneNode()
	{
		return duplicate(false);
	}
}
