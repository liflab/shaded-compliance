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

import ca.uqac.lif.cep.shaded.ShadedComparison.ShadedEquals;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class ShadedConstant implements ShadedFunction
{
	public static ShadedFunction wrap(Object o)
	{
		if (o instanceof ShadedFunction)
		{
			return (ShadedFunction) o;
		}
		else
		{
			return new ShadedConstant(o);
		}
	}
	protected final Object m_value;
	
	public ShadedConstant(Object value)
	{
		super();
		m_value = value;
	}
	
	@Override
	public ShadedConstant cloneNode()
	{
		return new ShadedConstant(m_value);
	}
	
	@Override
	public ShadedConstant addOperand(ShadedFunction f)
	{
		// Do nothing
		return this;
	}
	
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		// Do nothing
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedConstant))
		{
			return false;
		}
		ShadedConstant sfa = (ShadedConstant) o;
		return ShadedEquals.equals(m_value, sfa.m_value) == Color.GREEN;
	}

	@Override
	public ShadedConstant update(Object event)
	{
		return this;
	}

	@Override
	public int getArity()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		return null;
	}

	@Override
	public ShadedConstant duplicate(boolean with_state)
	{
		return new ShadedConstant(m_value);
	}

	@Override
	public ShadedConstant duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return m_value.toString();
	}

	@Override
	public String getSymbol()
	{
		return m_value.toString();
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}

	@Override
	public void trim()
	{
		// Do nothing
	}
}
