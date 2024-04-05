package ca.uqac.lif.cep.shaded;

import static ca.uqac.lif.cep.shaded.ShadedConstant.wrap;

import ca.uqac.lif.cep.shaded.ShadedComparison.ShadedEquals;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

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
	public void setValue(String name, Object value)
	{
		m_left.setValue(name, value);
		m_right.setValue(name, value);
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
		public ShadedAbsoluteDifference update(Object event)
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
			return this;
		}
		
		@Override
		public boolean sameAs(ShadedFunction f)
		{
			if (!(f instanceof ShadedAbsoluteDifference))
			{
				return false;
			}
			ShadedAbsoluteDifference eq = (ShadedAbsoluteDifference) f;
			return ShadedEquals.equals(m_value, eq.m_value) == Color.GREEN && eq.m_left.sameAs(m_left) && eq.m_right.sameAs(m_right);
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