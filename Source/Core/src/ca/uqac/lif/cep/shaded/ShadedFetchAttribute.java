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

import java.util.Map;

import ca.uqac.lif.cep.shaded.ShadedComparison.ShadedEquals;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class ShadedFetchAttribute implements ShadedFunction
{
	protected static boolean s_compareValues = false;
	
	public static ShadedFetchAttribute fetch(String attribute)
	{
		return new ShadedFetchAttribute(attribute);
	}
	
	protected final String m_attribute;
	
	protected Object m_value;
	
	public ShadedFetchAttribute(String attribute)
	{
		super();
		m_attribute = attribute;
		m_value = null;
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
	public void setValue(String name, Object value)
	{
		// Do nothing
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedFetchAttribute))
		{
			return false;
		}
		ShadedFetchAttribute sfa = (ShadedFetchAttribute) o;
		return m_attribute.compareTo(sfa.m_attribute) == 0 && (!s_compareValues || ShadedEquals.equals(m_value, sfa.m_value) == Color.GREEN);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShadedFetchAttribute update(Object event)
	{
		if (m_value != null)
		{
			return this;
		}
		Map<String,?> map = (Map<String,?>) event;
		m_value = map.get(m_attribute);
		return this;
	}

	@Override
	public int getArity()
	{
		return 0;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		return null;
	}

	@Override
	public ShadedFetchAttribute duplicate(boolean with_state)
	{
		ShadedFetchAttribute fa = new ShadedFetchAttribute(m_attribute);
		if (with_state)
		{
			fa.m_value = m_value;
		}
		return fa;
	}

	@Override
	public ShadedFetchAttribute duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "?" + m_attribute;
	}

	@Override
	public String getSymbol()
	{
		return "<i>" + m_attribute + "</i>";
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}
}
