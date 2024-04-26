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
import ca.uqac.lif.xml.XPathExpression;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;
import ca.uqac.lif.xml.XmlElement;

public class ShadedHasPath extends ShadedConnective
{
	public static ShadedHasPath hasPath(String path)
	{
		return new ShadedHasPath(path);
	}
	
	protected static boolean s_compareValues = false;
	
	protected final String m_path;
	
	protected XPathExpression m_pi;
	
	public ShadedHasPath(String path)
	{
		super();
		m_path = path;
		m_color = null;
		try
		{
			m_pi = XPathExpression.parse(path);
		}
		catch (XPathParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_pi = null;
		}
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
		if (!(o instanceof ShadedHasPath))
		{
			return false;
		}
		ShadedHasPath sfa = (ShadedHasPath) o;
		return m_path.compareTo(sfa.m_path) == 0 && (!s_compareValues || ShadedEquals.equals(m_color, sfa.m_color) == Color.GREEN);
	}

	@Override
	public ShadedHasPath update(Object event)
	{
		if (m_color != null)
		{
			return this;
		}
		XmlElement xml = (XmlElement) event;
		Object o = ShadedQuantifier.cast(m_pi.evaluateAny(xml));
		m_color = (o != null) ? Color.GREEN : Color.RED;
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
	public ShadedHasPath duplicate(boolean with_state)
	{
		ShadedHasPath fa = new ShadedHasPath(m_path);
		if (with_state)
		{
			fa.m_color = m_color;
		}
		return fa;
	}

	@Override
	public ShadedHasPath duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "?" + m_path;
	}

	@Override
	public String getSymbol()
	{
		return "<i>" + m_path + "</i>";
	}

	@Override
	protected void toString(StringBuilder out)
	{
		out.append(m_path);
	}
}
