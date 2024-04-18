package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
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
