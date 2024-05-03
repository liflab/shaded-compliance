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

public class ShadedNot extends ShadedConnective
{
	protected ShadedConnective m_operand;

	public static ShadedConnective not(ShadedConnective operand)
	{
		return not(Polarity.POSITIVE, operand);
	}

	protected static ShadedConnective not(Polarity p, ShadedConnective operand)
	{
		ShadedNot not = new ShadedNot(operand);
		not.setPolarity(p);
		return not;
	}

	public ShadedNot(ShadedConnective operand)
	{
		super();
		m_operand = operand;
	}
	
	@Override
	public void trim()
	{
		m_operand.trim();
	}
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("!" + (getValue() == Color.RED ? "-" : "+"));
		out.append("(");
		m_operand.toString(out);
		out.append(")");
	}
	
	@Override
	public int size()
	{
		return 1 + m_operand.size();
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		m_operand.setValue(name, value);
	}

	@Override
	public int hashCode()
	{
		return "!".hashCode();
	}

	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedNot))
		{
			return false;
		}
		ShadedNot c = (ShadedNot) o;
		if (m_color != c.m_color || m_polarity != c.m_polarity)
		{
			return false;
		}
		return m_operand.sameAs(c.m_operand);
	}


	@Override
	public ShadedNot update(Object event)
	{
		m_operand.update(event);
		return this;
	}

	@Override
	public int getArity()
	{
		return 1;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		if (index == 0)
		{
			return m_operand;
		}
		return null;
	}

	@Override
	public void setPolarity(Polarity p)
	{
		super.setPolarity(p);
		m_operand.setPolarity(Polarized.invert(p));
	}

	@Override
	public Color getValue()
	{
		Color c = m_operand.getValue();
		if (c == Color.RED)
		{
			return Color.GREEN;
		}
		else if (c == Color.GREEN)
		{
			return Color.RED;
		}
		return null;
	}

	@Override
	public ShadedNot duplicate(boolean with_state)
	{
		ShadedNot n = new ShadedNot(m_operand.duplicate(with_state));
		n.setPolarity(m_polarity);
		return n;
	}

	@Override
	public ShadedNot duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String toString()
	{
		return "\u00ac";
	}

	@Override
	public String getSymbol()
	{
		return "\u00ac";
	}

	@Override
	public ShadedNot addOperand(ShadedFunction f)
	{
		if (!(f instanceof ShadedConnective))
		{
			throw new IllegalArgumentException("Expected a ShadedFunction");
		}
		m_operand = (ShadedConnective) f;
		return this;
	}
	
	@Override
	public ShadedNot cloneNode()
	{
		return new ShadedNot(null);
	}
}
