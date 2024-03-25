package ca.uqac.lif.cep.shaded;

import java.util.Map;

public class ShadedFetchAttribute implements ShadedFunction
{
	protected final String m_attribute;
	
	protected Object m_value;
	
	public ShadedFetchAttribute(String attribute)
	{
		super();
		m_attribute = attribute;
		m_value = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Object event)
	{
		if (m_value != null)
		{
			return;
		}
		Map<String,?> map = (Map<String,?>) event;
		m_value = map.get(m_attribute);
	}

	@Override
	public int getArity()
	{
		return 0;
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		return null;
	}

	@Override
	public ShadedFetchAttribute duplicate(boolean with_state)
	{
		ShadedFetchAttribute fa = new ShadedFetchAttribute(m_attribute);
		if (with_state)
		{
			fa.m_value = m_value;
		}
		return fa;
	}

	@Override
	public ShadedFetchAttribute duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String getSymbol()
	{
		return "?" + m_attribute;
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}
}
