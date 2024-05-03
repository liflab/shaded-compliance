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

public class ShadedQuantifiedVariable implements ShadedFunction
{
	public static ShadedQuantifiedVariable v(String name)
	{
		return new ShadedQuantifiedVariable(name);
	}
	
	protected static boolean s_compareValues = false;
	
	protected final String m_name;
	
	protected Object m_value;
	
	public ShadedQuantifiedVariable(String name)
	{
		super();
		m_name = name;
	}
	
	@Override
	public void trim()
	{
		// Do nothing
	}
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public ShadedFunction update(Object event)
	{
		return this;
	}

	@Override
	public int getArity()
	{
		return 0;
	}

	@Override
	public ShadedQuantifiedVariable getOperand(int index)
	{
		return null;
	}

	@Override
	public ShadedQuantifiedVariable duplicate(boolean with_state)
	{
		ShadedQuantifiedVariable sqv = new ShadedQuantifiedVariable(m_name);
		//if (with_state)
		{
			sqv.m_value = m_value;
		}
		return sqv;
	}

	@Override
	public ShadedQuantifiedVariable duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String getSymbol()
	{
		return m_name + (m_value != null ? "[" + m_value + "]" : "");
	}
	
	@Override
	public String toString()
	{
		return getSymbol();
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedQuantifiedVariable))
		{
			return false;
		}
		ShadedQuantifiedVariable sqv = (ShadedQuantifiedVariable) f;
		if (m_name.compareTo(sqv.m_name) != 0)
		{
			return false;
		}
		if (!s_compareValues)
		{
			return true;
		}
		if ((m_value == null) != (sqv.m_value == null))
		{
			return false;
		}
		if (m_value == null)
		{
			return true;
		}
		return m_value.equals(sqv.m_value);
	}

	@Override
	public void setValue(String name, Object value)
	{
		if (m_name.compareTo(name) == 0)
		{
			m_value = value;
		}
	}

	@Override
	public ShadedQuantifiedVariable addOperand(ShadedFunction f)
	{
		// Do nothing
		return this;
	}
	
	@Override
	public ShadedQuantifiedVariable cloneNode()
	{
		return new ShadedQuantifiedVariable(m_name);
	}
}
