package ca.uqac.lif.cep.shaded;

import java.util.Map;

import ca.uqac.lif.cep.shaded.ShadedComparison.ShadedEquals;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class ShadedFetchAttribute implements ShadedFunction
{
	public static ShadedFetchAttribute fetch(String attribute)
	{
		return new ShadedFetchAttribute(attribute);
	}
	
	protected final String m_attribute;
	
	protected Object m_value;
	
	public ShadedFetchAttribute(String attribute)
	{
		super();
		m_attribute = attribute;
		m_value = null;
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedFetchAttribute))
		{
			return false;
		}
		ShadedFetchAttribute sfa = (ShadedFetchAttribute) o;
		return m_attribute.compareTo(sfa.m_attribute) == 0 && ShadedEquals.equals(m_value, sfa.m_value) == Color.GREEN;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShadedFetchAttribute update(Object event)
	{
		if (m_value != null)
		{
			return this;
		}
		Map<String,?> map = (Map<String,?>) event;
		m_value = map.get(m_attribute);
		return this;
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
	public String toString()
	{
		return "?" + m_attribute;
	}

	@Override
	public String getSymbol()
	{
		return "<i>" + m_attribute + "</i>";
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}
}
