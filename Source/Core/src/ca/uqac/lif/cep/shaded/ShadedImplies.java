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

public class ShadedImplies extends ShadedConnective
{
	public static ShadedConnective implies(ShadedConnective left, ShadedConnective right)
	{
		return new ShadedImplies(left, right);
	}
	
	protected ShadedConnective m_left;

	protected ShadedConnective m_right;

	public ShadedImplies(ShadedConnective left, ShadedConnective right)
	{
		super();
		m_left = left;
		m_right = right;
	}
	
	@Override
	public ShadedImplies addOperand(ShadedFunction f)
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
	public void trim()
	{
		m_left.trim();
		m_right.trim();
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
	public String getSymbol()
	{
		return "\u2192";
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedImplies))
		{
			return false;
		}
		ShadedImplies eq = (ShadedImplies) f;
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
		out.append("=>" + (m_color == Color.GREEN ? "+" : "-") + "(");
		m_left.toString(out);
		out.append(",");
		m_right.toString(out);
		out.append(")");
	}

	@Override
	public ShadedImplies update(Object event)
	{
		m_left.update(event);
		m_right.update(event);
		Color c_l = m_left.getValue();
		Color c_r = m_right.getValue();
		if (c_l == Color.RED || c_r == Color.GREEN)
		{
			m_color = Color.GREEN;
		}
		else if (c_l == Color.GREEN && c_r == Color.RED)
		{
			m_color = Color.RED;
		}
		else
		{
			m_color = null;
		}
		return this;
	}

	@Override
	public ShadedConnective duplicate(boolean with_state)
	{
		ShadedImplies eq = new ShadedImplies(m_left.duplicate(with_state), m_right.duplicate(with_state));
		if (with_state)
		{
			eq.m_color = m_color;
		}
		return eq;
	}

}
