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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ShadedNaryConnective extends ShadedConnective
{
	protected final List<ShadedConnective> m_operands;
	
	public ShadedNaryConnective(ShadedConnective... operands)
	{
		super();
		m_operands = new ArrayList<>(operands.length);
		for (ShadedConnective op : operands)
		{
			m_operands.add(op);
		}
	}
	
	@Override
	public ShadedNaryConnective addOperand(ShadedFunction f)
	{
		if (!(f instanceof ShadedConnective))
		{
			throw new IllegalArgumentException("Expected a ShadedFunction");
		}
		m_operands.add((ShadedConnective) f);
		return this;
	}
	
	@Override
	public void trim()
	{
		Color c = getValue();
		if (c == null)
		{
			return;
		}
		Iterator<ShadedConnective> it = m_operands.iterator();
		while (it.hasNext())
		{
			ShadedConnective con = it.next();
			Color op_color = con.getValue();
			if ((m_polarity == Polarity.POSITIVE && op_color != c) || (m_polarity == Polarity.NEGATIVE && op_color == c))
			{
				it.remove();
			}
		}
	}
	
	public void addOperand(ShadedConnective op)
	{
		m_operands.add(op);
	}
	
	@Override
	public int size()
	{
		int size = 1;
		for (ShadedConnective op : m_operands)
		{
			size += op.size();
		}
		return size;
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		for (ShadedConnective c : m_operands)
		{
			c.setValue(name, value);
		}
	}
	
	@Override
	public void setPolarity(Polarity p)
	{
		super.setPolarity(p);
		for (ShadedConnective op : m_operands)
		{
			op.setPolarity(p);
		}
	}

	@Override
	public int getArity()
	{
		return m_operands.size();
	}

	@Override
	public ShadedConnective getOperand(int index)
	{
		return m_operands.get(index);
	}

	protected void copyInto(ShadedNaryConnective other, boolean with_state)
	{
		other.m_polarity = m_polarity;
		if (with_state)
		{
			other.m_color = m_color;
		}
		for (ShadedConnective op : m_operands)
		{
			other.m_operands.add(op.duplicate(with_state));
		}
	}
}
