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

public abstract class ShadedConnective implements ShadedFunction, Polarized
{
	public static enum Color
	{
		GREEN,
		RED
	}

	protected Polarity m_polarity;
	
	protected Color m_color;
	
	public ShadedConnective()
	{
		super();
		m_polarity = Polarity.POSITIVE;
		m_color = null;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		toString(out);
		return out.toString();
	}
	
	protected abstract void toString(StringBuilder out);
	
	@Override
	public abstract ShadedConnective update(Object event);
	
	@Override
	public void setPolarity(Polarity p)
	{
		m_polarity = p;
	}
	
	@Override
	public Polarity getPolarity()
	{
		return m_polarity;
	}
	
	@Override
	public Color getValue()
	{
		return m_color;
	}
	
	@Override
	public abstract ShadedConnective duplicate(boolean with_state);
	
	@Override
	public ShadedConnective duplicate()
	{
		return duplicate(false);
	}
}
