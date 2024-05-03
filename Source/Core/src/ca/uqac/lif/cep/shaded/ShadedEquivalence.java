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

public class ShadedEquivalence extends ShadedConnective
{
	public static ShadedConnective iff(ShadedConnective left, ShadedConnective right)
	{
		return new ShadedEquivalence(left, right);
	}
	
	protected ShadedConnective m_left;

	protected ShadedConnective m_right;

	public ShadedEquivalence(ShadedConnective left, ShadedConnective right)
	{
		super();
		m_left = left;
		m_right = right;
	}
	
	@Override
	public ShadedEquivalence addOperand(ShadedFunction f)
	{
		if (!(f instanceof ShadedConnective))
		{
			throw new IllegalArgumentException("Expected a ShadedFunction");
		}
		if (m_left == null)
		{
			m_left = (ShadedConnective) f;
		}
		else
		{
			m_right = (ShadedConnective) f;
		}
		return this;
	}

	@Override
	public int getArity()
	{
		return 2;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		if (index == 0)
		{
			return m_left;
		}
		else if (index == 1)
		{
			return m_right;
		}
		return null;
	}
	
	@Override
	public ShadedEquivalence cloneNode()
	{
		return new ShadedEquivalence(null, null);
	}

	@Override
	public String getSymbol()
	{
		return "\u2194";
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedEquivalence))
		{
			return false;
		}
		ShadedEquivalence eq = (ShadedEquivalence) f;
		return m_left.sameAs(eq.m_left) && m_right.sameAs(eq.m_right);
	}

	@Override
	public void setValue(String name, Object value)
	{
		m_left.setValue(name, value);
		m_right.setValue(name, value);
	}

	@Override
	public int size()
	{
		return m_left.size() + m_right.size() + 1;
	}

	@Override
	protected void toString(StringBuilder out)
	{
		out.append("<=>" + (m_color == Color.GREEN ? "+" : "-") + "(");
		m_left.toString(out);
		out.append(",");
		m_right.toString(out);
		out.append(")");
	}

	@Override
	public ShadedEquivalence update(Object event)
	{
		m_left.update(event);
		m_right.update(event);
		if (m_left != null && m_right != null)
		{
			m_color = m_left.getValue() == m_right.getValue() ? Color.GREEN : Color.RED;
		}
		return this;
	}

	@Override
	public ShadedConnective duplicate(boolean with_state)
	{
		ShadedEquivalence eq = new ShadedEquivalence(m_left.duplicate(with_state), m_right.duplicate(with_state));
		if (with_state)
		{
			eq.m_color = m_color;
		}
		return eq;
	}
	
	@Override
	public void trim()
	{
		m_left.trim();
		m_right.trim();
	}

}
