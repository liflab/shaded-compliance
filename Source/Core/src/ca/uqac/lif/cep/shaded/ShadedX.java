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
 * Multi-shade implements of the LTL "X" operator.
 * @author Sylvain Hallé
 */
public class ShadedX extends ShadedConnective
{
	public static final ShadedX X(ShadedConnective phi)
	{
		return new ShadedX(phi);
	}
	
	protected final ShadedConnective m_phi;
	
	protected ShadedConnective m_operand;
	
	protected boolean m_seenFirst;
	
	public ShadedX(ShadedConnective phi)
	{
		super();
		m_phi = phi;
		m_operand = null;
		m_seenFirst = false;
	}
	
	@Override
	public void trim()
	{
		if (m_operand != null)
		{
			m_operand.trim();
		}
	}
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("X" + (getValue() == Color.RED ? "-" : "+"));
		out.append("(");
		m_operand.toString(out);
		out.append(")");
	}
	
	@Override
	public int getArity()
	{
		return m_operand == null ? 0 : 1;
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
	public String getSymbol()
	{
		return "<b>X</b>";
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedX))
		{
			return false;
		}
		if (m_operand == null)
		{
			return ((ShadedX) f).m_operand == null;
		}
		return m_operand.sameAs(((ShadedX) f).m_operand);
	}

	@Override
	public void setValue(String name, Object value)
	{
		m_phi.setValue(name, value);
	}

	@Override
	public int size()
	{
		if (m_operand == null)
		{
			return 1;
		}
		return 1 + m_operand.size();
	}

	@Override
	public ShadedConnective update(Object event)
	{
		if (!m_seenFirst)
		{
			m_seenFirst = true;
			return this;
		}
		if (m_operand == null)
		{
			m_operand = m_phi.duplicate();
		}
		m_operand.update(event);
		m_color = m_operand.getValue();
		return this;
	}

	@Override
	public ShadedX duplicate(boolean with_state)
	{
		ShadedX x = new ShadedX(m_phi);
		x.m_polarity = m_polarity;
		if (with_state)
		{
			if (m_operand != null)
			{
				x.m_operand = m_operand.duplicate(with_state);
			}
			x.m_seenFirst = m_seenFirst;
			x.m_color = m_color;
		}
		return x;
	}
	
	@Override
	public String toString()
	{
		return "X" + (m_color == Color.RED ? "-" : "+") + "(" + m_operand + ")";
	}

	@Override
	public ShadedX addOperand(ShadedFunction f)
	{
		if (!(f instanceof ShadedConnective))
		{
			throw new IllegalArgumentException("Expected a ShadedFunction");
		}
		m_operand = (ShadedConnective) f;
		return this;
	}
	
	@Override
	public ShadedX cloneNode()
	{
		return duplicate(false);
	}
}
