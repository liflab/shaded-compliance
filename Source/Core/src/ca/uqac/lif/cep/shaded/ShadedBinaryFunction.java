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

import static ca.uqac.lif.cep.shaded.ShadedConstant.wrap;

import ca.uqac.lif.cep.shaded.ShadedComparison.ShadedEquals;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public abstract class ShadedBinaryFunction implements ShadedFunction
{
	public static ShadedAbsoluteDifference delta(Object left, Object right)
	{
		return new ShadedAbsoluteDifference(wrap(left), wrap(right));
	}
	
	protected ShadedFunction m_left;
	
	protected ShadedFunction m_right;
	
	protected Object m_value;
	
	public ShadedBinaryFunction(ShadedFunction left, ShadedFunction right)
	{
		super();
		m_left = left;
		m_right = right;
		m_value = null;
	}
	
	@Override
	public ShadedBinaryFunction addOperand(ShadedFunction f)
	{
		if (m_left == null)
		{
			m_left = f;
		}
		else
		{
			m_right= f;
		}
		return this;
	}
	
	@Override
	public void trim()
	{
		m_left.trim();
		m_right.trim();
	}
	
	@Override
	public int size()
	{
		return 1 + m_left.size() + m_right.size();
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		m_left.setValue(name, value);
		m_right.setValue(name, value);
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
	public Object getValue()
	{
		return m_value;
	}
	
	public static class ShadedAbsoluteDifference extends ShadedBinaryFunction
	{
		public ShadedAbsoluteDifference(ShadedFunction left, ShadedFunction right)
		{
			super(left, right);
		}
		
		@Override
		public ShadedAbsoluteDifference update(Object event)
		{
			m_left.update(event);
			m_right.update(event);
			Object left = m_left.getValue();
			Object right = m_right.getValue();
			if (!(left instanceof Number) || !(right instanceof Number))
			{
				m_value = null;
			}
			else
			{
				Number n_left = (Number) left;
				Number n_right = (Number) right;
				m_value = Math.abs(n_left.doubleValue() - n_right.doubleValue());
			}
			return this;
		}
		
		@Override
		public ShadedAbsoluteDifference cloneNode()
		{
			return new ShadedAbsoluteDifference(null, null);
		}
		
		@Override
		public boolean sameAs(ShadedFunction f)
		{
			if (!(f instanceof ShadedAbsoluteDifference))
			{
				return false;
			}
			ShadedAbsoluteDifference eq = (ShadedAbsoluteDifference) f;
			return ShadedEquals.equals(m_value, eq.m_value) == Color.GREEN && eq.m_left.sameAs(m_left) && eq.m_right.sameAs(m_right);
		}

		@Override
		public String getSymbol()
		{
			return "&#x03b4;";
		}

		@Override
		public ShadedAbsoluteDifference duplicate(boolean with_state)
		{
			ShadedAbsoluteDifference d = new ShadedAbsoluteDifference(m_left.duplicate(with_state), m_right.duplicate(with_state));
			if (with_state)
			{
				d.m_value = m_value;
			}
			return d;
		}
		
		@Override
		public ShadedAbsoluteDifference duplicate()
		{
			return duplicate(false);
		}
	}
}