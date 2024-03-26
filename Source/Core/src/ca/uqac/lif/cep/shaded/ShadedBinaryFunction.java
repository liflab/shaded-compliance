package ca.uqac.lif.cep.shaded;

import static ca.uqac.lif.cep.shaded.ShadedConstant.wrap;

public abstract class ShadedBinaryFunction implements ShadedFunction
{
	public static ShadedAbsoluteDifference delta(Object left, Object right)
	{
		return new ShadedAbsoluteDifference(wrap(left), wrap(right));
	}
	
	protected ShadedFunction m_left;
	
	protected ShadedFunction m_right;
	
	protected Object m_value;
	
	public ShadedBinaryFunction(ShadedFunction left, ShadedFunction right)
	{
		super();
		m_left = left;
		m_right = right;
		m_value = null;
	}
	
	@Override
	public int getArity()
	{
		return 2;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		if (index == 0)
		{
			return m_left;
		}
		else if (index == 1)
		{
			return m_right;
		}
		return null;
	}
	
	@Override
	public Object getValue()
	{
		return m_value;
	}
	
	public static class ShadedAbsoluteDifference extends ShadedBinaryFunction
	{
		public ShadedAbsoluteDifference(ShadedFunction left, ShadedFunction right)
		{
			super(left, right);
		}
		
		@Override
		public void update(Object event)
		{
			m_left.update(event);
			m_right.update(event);
			Object left = m_left.getValue();
			Object right = m_right.getValue();
			if (!(left instanceof Number) || !(right instanceof Number))
			{
				m_value = null;
			}
			else
			{
				Number n_left = (Number) left;
				Number n_right = (Number) right;
				m_value = Math.abs(n_left.doubleValue() - n_right.doubleValue());
			}
		}

		@Override
		public String getSymbol()
		{
			return "&#x03b4;";
		}

		@Override
		public ShadedAbsoluteDifference duplicate(boolean with_state)
		{
			ShadedAbsoluteDifference d = new ShadedAbsoluteDifference(m_left.duplicate(with_state), m_right.duplicate(with_state));
			if (with_state)
			{
				d.m_value = m_value;
			}
			return d;
		}
		
		@Override
		public ShadedAbsoluteDifference duplicate()
		{
			return duplicate(false);
		}
	}
}