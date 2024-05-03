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

public abstract class ShadedComparison extends ShadedConnective
{
	public static ShadedEquals eq(Object left, Object right)
	{
		return eq(Polarity.POSITIVE, wrap(left), wrap(right));
	}

	protected static ShadedEquals eq(Polarity p, ShadedFunction left, ShadedFunction right)
	{
		ShadedEquals e = new ShadedEquals(left, right);
		e.setPolarity(p);
		return e;
	}

	public static ShadedGreaterThanOrEqual geq(Object left, Object right)
	{
		return geq(Polarity.POSITIVE, wrap(left), wrap(right));
	}
	
	public static ShadedLessThanOrEqual leq(Object left, Object right)
	{
		return leq(Polarity.POSITIVE, wrap(left), wrap(right));
	}

	protected static ShadedLessThanOrEqual leq(Polarity p, ShadedFunction left, ShadedFunction right)
	{
		ShadedLessThanOrEqual e = new ShadedLessThanOrEqual(left, right);
		e.setPolarity(p);
		return e;
	}
	
	protected static ShadedGreaterThanOrEqual geq(Polarity p, ShadedFunction left, ShadedFunction right)
	{
		ShadedGreaterThanOrEqual e = new ShadedGreaterThanOrEqual(left, right);
		e.setPolarity(p);
		return e;
	}

	protected ShadedFunction m_left;

	protected ShadedFunction m_right;

	public ShadedComparison(ShadedFunction left, ShadedFunction right)
	{
		super();
		m_left = left;
		m_right = right;
	}
	
	@Override
	public ShadedComparison addOperand(ShadedFunction f)
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

	public static class ShadedLessThanOrEqual extends ShadedComparison
	{
		public ShadedLessThanOrEqual(ShadedFunction left, ShadedFunction right)
		{
			super(left, right);
		}
		
		@Override
		public ShadedLessThanOrEqual cloneNode()
		{
			return new ShadedLessThanOrEqual(null, null);
		}
		
		@Override
		protected void toString(StringBuilder out)
		{
			out.append("<=(");
			out.append(m_left);
			out.append("),(");
			out.append(m_right);
			out.append(")");
		}

		@Override
		public boolean sameAs(ShadedFunction f)
		{
			if (!(f instanceof ShadedLessThanOrEqual))
			{
				return false;
			}
			ShadedLessThanOrEqual eq = (ShadedLessThanOrEqual) f;
			return eq.m_color == m_color && eq.m_left.sameAs(m_left) && eq.m_right.sameAs(m_right);
		}

		@Override
		public ShadedLessThanOrEqual update(Object event)
		{
			m_left.update(event);
			m_right.update(event);
			Object left = m_left.getValue();
			Object right = m_right.getValue();
			if (!(left instanceof Number) || !(right instanceof Number))
			{
				m_color = null;
			}
			else
			{
				Number n_left = (Number) left;
				Number n_right = (Number) right;
				m_color = n_left.doubleValue() <= n_right.doubleValue() ? Color.GREEN : Color.RED;
			}
			return this;
		}

		@Override
		public String getSymbol()
		{
			return "&#8804;";
		}

		@Override
		public ShadedLessThanOrEqual duplicate(boolean with_state)
		{
			ShadedLessThanOrEqual s = new ShadedLessThanOrEqual(m_left.duplicate(with_state), m_right.duplicate(with_state));
			s.m_polarity = m_polarity;
			if (with_state)
			{
				s.m_color = m_color;
			}
			return s;
		}

		@Override
		public ShadedLessThanOrEqual duplicate()
		{
			return duplicate(false);
		}
	}
	
	public static class ShadedGreaterThanOrEqual extends ShadedComparison
	{
		public ShadedGreaterThanOrEqual(ShadedFunction left, ShadedFunction right)
		{
			super(left, right);
		}
		
		@Override
		protected void toString(StringBuilder out)
		{
			out.append(">=(");
			out.append(m_left);
			out.append("),(");
			out.append(m_right);
			out.append(")");
		}
		
		@Override
		public ShadedGreaterThanOrEqual cloneNode()
		{
			return new ShadedGreaterThanOrEqual(null, null);
		}

		@Override
		public boolean sameAs(ShadedFunction f)
		{
			if (!(f instanceof ShadedGreaterThanOrEqual))
			{
				return false;
			}
			ShadedGreaterThanOrEqual eq = (ShadedGreaterThanOrEqual) f;
			return eq.m_color == m_color && eq.m_left.sameAs(m_left) && eq.m_right.sameAs(m_right);
		}

		@Override
		public ShadedGreaterThanOrEqual update(Object event)
		{
			m_left.update(event);
			m_right.update(event);
			Object left = m_left.getValue();
			Object right = m_right.getValue();
			if (!(left instanceof Number) || !(right instanceof Number))
			{
				m_color = null;
			}
			else
			{
				Number n_left = (Number) left;
				Number n_right = (Number) right;
				m_color = n_left.doubleValue() >= n_right.doubleValue() ? Color.GREEN : Color.RED;
			}
			return this;
		}

		@Override
		public String getSymbol()
		{
			return "&#8805;";
		}

		@Override
		public ShadedGreaterThanOrEqual duplicate(boolean with_state)
		{
			ShadedGreaterThanOrEqual s = new ShadedGreaterThanOrEqual(m_left.duplicate(with_state), m_right.duplicate(with_state));
			s.m_polarity = m_polarity;
			if (with_state)
			{
				s.m_color = m_color;
			}
			return s;
		}

		@Override
		public ShadedGreaterThanOrEqual duplicate()
		{
			return duplicate(false);
		}
	}

	public static class ShadedEquals extends ShadedComparison
	{
		public ShadedEquals(ShadedFunction left, ShadedFunction right)
		{
			super(left, right);
		}
		
		@Override
		protected void toString(StringBuilder out)
		{
			out.append("=(");
			out.append(m_left);
			out.append("),(");
			out.append(m_right);
			out.append(")");
		}
		
		@Override
		public ShadedEquals cloneNode()
		{
			return new ShadedEquals(null, null);
		}

		@Override
		public boolean sameAs(ShadedFunction f)
		{
			if (!(f instanceof ShadedEquals))
			{
				return false;
			}
			ShadedEquals eq = (ShadedEquals) f;
			return eq.m_color == m_color && eq.m_left.sameAs(m_left) && eq.m_right.sameAs(m_right);
		}

		@Override
		public ShadedEquals update(Object event)
		{
			m_left.update(event);
			m_right.update(event);
			Object left = m_left.getValue();
			Object right = m_right.getValue();
			m_color = equals(left, right);
			return this;
		}

		public static Color equals(Object left, Object right)
		{
			if (left == null && right == null)
			{
				return Color.GREEN;
			}
			if ((left == null && right != null) || (left != null && right == null))
			{
				return Color.RED;
			}
			if (left instanceof String && right instanceof String)
			{
				return ((String) left).compareTo((String) right) == 0 ? Color.GREEN : Color.RED;
			}
			if (left instanceof Number && right instanceof Number)
			{
				return ((Number) left).doubleValue() == ((Number) right).doubleValue() ? Color.GREEN : Color.RED;
			}
			return Color.RED;
		}

		@Override
		public String getSymbol()
		{
			return "=";
		}

		@Override
		public ShadedEquals duplicate(boolean with_state)
		{
			ShadedEquals d = new ShadedEquals(m_left.duplicate(with_state), m_right.duplicate(with_state));
			d.m_polarity = m_polarity;
			if (with_state)
			{
				d.m_color = m_color;
			}
			return d;
		}

		@Override
		public ShadedEquals duplicate()
		{
			return duplicate(false);
		}
	}

}
