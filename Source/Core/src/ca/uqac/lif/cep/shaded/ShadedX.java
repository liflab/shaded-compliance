package ca.uqac.lif.cep.shaded;

public class ShadedX extends ShadedConnective
{
	public static final ShadedX X(ShadedConnective phi)
	{
		return new ShadedX(phi);
	}
	
	protected final ShadedConnective m_phi;
	
	protected ShadedConnective m_operand;
	
	protected boolean m_seenFirst;
	
	public ShadedX(ShadedConnective phi)
	{
		super();
		m_phi = phi;
		m_operand = null;
		m_seenFirst = false;
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
	public String getSymbol()
	{
		return "<b>X</b>";
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedX))
		{
			return false;
		}
		if (m_operand == null)
		{
			return ((ShadedX) f).m_operand == null;
		}
		return m_operand.sameAs(((ShadedX) f).m_operand);
	}

	@Override
	public void setValue(String name, Object value)
	{
		m_phi.setValue(name, value);
	}

	@Override
	public int size()
	{
		if (m_operand == null)
		{
			return 1;
		}
		return 1 + m_phi.size();
	}

	@Override
	public ShadedConnective update(Object event)
	{
		if (!m_seenFirst)
		{
			m_seenFirst = true;
			return this;
		}
		if (m_operand == null)
		{
			m_operand = m_phi.duplicate();
		}
		m_operand.update(event);
		m_color = m_operand.getValue();
		return this;
	}

	@Override
	public ShadedX duplicate(boolean with_state)
	{
		ShadedX x = new ShadedX(m_phi);
		x.m_polarity = m_polarity;
		if (with_state)
		{
			if (m_operand != null)
			{
				x.m_operand = m_operand.duplicate(with_state);
			}
			x.m_seenFirst = m_seenFirst;
			x.m_color = m_color;
		}
		return x;
	}
	
	@Override
	public String toString()
	{
		return "X";
	}
}
