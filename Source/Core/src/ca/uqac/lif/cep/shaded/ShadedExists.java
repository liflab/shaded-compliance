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

import ca.uqac.lif.xml.XPathExpression;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;

public class ShadedExists extends ShadedQuantifier
{
	public static ShadedExists exists(String x, String pi, ShadedConnective phi) throws XPathParseException
	{
		return new ShadedExists(x, XPathExpression.parse(pi + "/text()"), phi);
	}
	
	public ShadedExists(String x, XPathExpression pi, ShadedConnective phi)
	{
		super(x, pi, phi);
	}

	@Override
	public String getSymbol()
	{
		return "\u2203" + m_x + "\u2208" + m_pi;
	}
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("exists");
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedExists))
		{
			return false;
		}
		return sameSubtrees((ShadedQuantifier) f);
	}

	@Override
	public ShadedConnective duplicate(boolean with_state)
	{
		ShadedExists sfa = new ShadedExists(m_x, m_pi, m_phi);
		sfa.m_polarity = m_polarity;
		if (with_state)
		{
			sfa.m_color = m_color;
			for (ShadedConnective c : m_instances)
			{
				sfa.m_instances.add(c.duplicate(with_state));
			}
		}
		return sfa;
	}

	@Override
	protected Color updateVerdict()
	{
		boolean has_true = false, has_null = false;
		for (ShadedConnective c : m_instances)
		{
			Color v = c.getValue();
			if (v == null)
			{
				has_null = true;
			}
			if (v == Color.GREEN)
			{
				has_true = true;
			}
		}
		if (has_true)
		{
			return Color.GREEN;
		}
		if (has_null)
		{
			return null;
		}
		return Color.RED;
	}
	
	@Override
	public ShadedConnective cloneNode()
	{
		return duplicate(false);
	}
}
