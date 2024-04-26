package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
import java.util.List;

public abstract class ShadedPastLtlOperator extends ShadedLtlOperator
{
	protected final List<Object> m_events;
	
	public ShadedPastLtlOperator(ShadedConnective phi)
	{
		super(phi);
		m_events = new ArrayList<>();
	}
	
	protected void copyInto(ShadedPastLtlOperator other, boolean with_state)
	{
		other.m_polarity = m_polarity;
		if (with_state)
		{
			other.m_color = m_color;
			for (ShadedConnective op : m_operands)
			{
				other.m_operands.add(op.duplicate(with_state));
			}
		}
	}
}
