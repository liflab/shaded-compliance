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

public abstract class ShadedUnaryPredicate extends ShadedConnective
{
	public static IsEven even(Object argument)
	{
		return new IsEven(ShadedConstant.wrap(argument));
	}

	protected ShadedFunction m_argument;

	public ShadedUnaryPredicate(ShadedFunction arg)
	{
		super();
		m_argument = arg;
		m_color = null;
	}
	
	@Override
	public int size()
	{
		return 1 + m_argument.size();
	}

	@Override
	public int getArity()
	{
		return 1;
	}
	
	@Override
	public void trim()
	{
		m_argument.trim();
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		if (index == 0)
		{
			return m_argument;
		}
		return null;
	}
	
	@Override
	public ShadedUnaryPredicate addOperand(ShadedFunction f)
	{
		m_argument = f;
		return this;
	}

	@Override
	public void setValue(String name, Object value)
	{
		m_argument.setValue(name, value);
	}

	public static class IsEven extends ShadedUnaryPredicate
	{
		public IsEven(ShadedFunction arg)
		{
			super(arg);
		}

		@Override
		public IsEven duplicate(boolean with_state)
		{
			IsEven ie = new IsEven(m_argument.duplicate(with_state));
			if (with_state)
			{
				ie.m_color = m_color;
			}
			return ie;
		}
		
		@Override
		public IsEven cloneNode()
		{
			return new IsEven(null);
		}
		
		@Override
		protected void toString(StringBuilder out)
		{
			out.append("even");
		}

		@Override
		public ShadedUnaryPredicate update(Object event)
		{
			if (m_color == null)
			{
				m_argument.update(event);
				Object o = m_argument.getValue();
				m_color = (o instanceof Number && ((Number) o).intValue() % 2 == 0) ? Color.GREEN : Color.RED;
			}
			return this;
		}

		@Override
		public String getSymbol()
		{
			return "even";
		}

		@Override
		public boolean sameAs(ShadedFunction f)
		{
			if (!(f instanceof IsEven))
			{
				return false;
			}
			IsEven ie = (IsEven) f;
			return m_argument.sameAs(ie.m_argument);
		}
	}
}
