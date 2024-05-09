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
 * A constant connective with no value.
 * @author Sylvain Hallé
 */
public class ShadedUnknown extends ShadedConnective
{
	public ShadedUnknown()
	{
		super();
		m_color = null;
	}
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public ShadedUnknown cloneNode()
	{
		return new ShadedUnknown();
	}
	
	@Override
	public void trim()
	{
		// Do nothing
	}
	
	@Override
	public ShadedUnknown addOperand(ShadedFunction f)
	{
		// Do nothing
		return this;
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		// Do nothing
	}
	
	@Override
	public ShadedUnknown update(Object event)
	{
		return this;
	}
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("?");
	}
	
	@Override
	public int hashCode()
	{
		return "?".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		return o instanceof ShadedUnknown;
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
	public ShadedUnknown duplicate(boolean with_state)
	{
		ShadedUnknown t = new ShadedUnknown();
		t.setPolarity(m_polarity);
		return t;
	}
	
	@Override
	public ShadedUnknown duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "?";
	}
	
	@Override
	public String getSymbol()
	{
		return "?";
	}
}
