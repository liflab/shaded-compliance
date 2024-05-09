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

import java.util.ArrayList;
import java.util.List;

/**
 * Multi-shade implements of the LTL "Y" operator.
 * @author Sylvain Hallé
 */
public class ShadedY extends ShadedConnective
{
	public static final ShadedY X(ShadedConnective phi)
	{
		return new ShadedY(phi);
	}
	
	protected final ShadedConnective m_phi;
	
	protected ShadedConnective m_operand;
	
	protected final List<Object> m_events;
	
	public ShadedY(ShadedConnective phi)
	{
		super();
		m_phi = phi;
		m_operand = null;
		m_events = new ArrayList<>();
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
		out.append("Y" + (getValue() == Color.RED ? "-" : "+"));
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
		return "<b>Y</b>";
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedY))
		{
			return false;
		}
		if (m_operand == null)
		{
			return ((ShadedY) f).m_operand == null;
		}
		return m_operand.sameAs(((ShadedY) f).m_operand);
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
		m_events.add(event);
		if (m_events.size() == 1)
		{
			// Not enough events to evaluate the Y operator
			return this;
		}
		m_operand = m_phi.duplicate();
		for (int i = 0; i < m_events.size() - 1; i++) // yes -1, we omit the last event
		{
			Object e = m_events.get(i);
			m_phi.update(e);
		}
		m_color = m_operand.getValue();
		return this;
	}

	@Override
	public ShadedY duplicate(boolean with_state)
	{
		ShadedY x = new ShadedY(m_phi);
		x.m_polarity = m_polarity;
		if (with_state)
		{
			if (m_operand != null)
			{
				x.m_operand = m_operand.duplicate(with_state);
			}
			x.m_events.addAll(m_events);
			x.m_color = m_color;
		}
		return x;
	}
	
	@Override
	public String toString()
	{
		return "Y" + (m_color == Color.RED ? "-" : "+") + "(" + m_operand + ")";
	}
	
	@Override
	public ShadedY addOperand(ShadedFunction f)
	{
		if (!(f instanceof ShadedConnective))
		{
			throw new IllegalArgumentException("Expected a ShadedFunction");
		}
		m_operand = (ShadedConnective) f;
		return this;
	}
	
	@Override
	public ShadedY cloneNode()
	{
		ShadedY clone = duplicate(false);
		clone.m_color = m_color;
		return clone;
	}
}
