package ca.uqac.lif.cep.shaded;

import ca.uqac.lif.cep.shaded.ShadedComparison.ShadedEquals;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class ShadedConstant implements ShadedFunction
{
	public static ShadedFunction wrap(Object o)
	{
		if (o instanceof ShadedFunction)
		{
			return (ShadedFunction) o;
		}
		else
		{
			return new ShadedConstant(o);
		}
	}
	protected final Object m_value;
	
	public ShadedConstant(Object value)
	{
		super();
		m_value = value;
	}
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		// Do nothing
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedConstant))
		{
			return false;
		}
		ShadedConstant sfa = (ShadedConstant) o;
		return ShadedEquals.equals(m_value, sfa.m_value) == Color.GREEN;
	}

	@Override
	public ShadedConstant update(Object event)
	{
		return this;
	}

	@Override
	public int getArity()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		return null;
	}

	@Override
	public ShadedConstant duplicate(boolean with_state)
	{
		return new ShadedConstant(m_value);
	}

	@Override
	public ShadedConstant duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return m_value.toString();
	}

	@Override
	public String getSymbol()
	{
		return m_value.toString();
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}

	@Override
	public void trim()
	{
		// Do nothing
	}
}
