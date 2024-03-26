package ca.uqac.lif.cep.shaded;

public abstract class ShadedLtlOperator extends ShadedNaryConnective
{
	protected final ShadedConnective m_phi;
	
	public ShadedLtlOperator(ShadedConnective phi)
	{
		super();
		m_phi = phi;
	}
	
	@Override
	protected void copyInto(ShadedNaryConnective other, boolean with_state)
	{
		m_polarity = other.m_polarity;
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
