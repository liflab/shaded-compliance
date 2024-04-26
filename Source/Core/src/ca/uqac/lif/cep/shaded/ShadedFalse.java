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
 * A constant true connective.
 * @author Sylvain Hallé
 */
public class ShadedFalse extends ShadedConnective
{
	public ShadedFalse()
	{
		super();
		m_color = Color.RED;
	}
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public void trim()
	{
		// Do nothing
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		// Do nothing
	}
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("F");
	}

	@Override
	public ShadedFalse update(Object event)
	{
		return this;
	}

	@Override
	public boolean sameAs(ShadedFunction o)
	{
		return o instanceof ShadedFalse;
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
	public ShadedFalse duplicate(boolean with_state)
	{
		ShadedFalse t = new ShadedFalse();
		t.setPolarity(m_polarity);
		return t;
	}

	@Override
	public ShadedFalse duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String toString()
	{
		return "\u22a5";
	}

	@Override
	public String getSymbol()
	{
		return "\u22a5";
	}
}
