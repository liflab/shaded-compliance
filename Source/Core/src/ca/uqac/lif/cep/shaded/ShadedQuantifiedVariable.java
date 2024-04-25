package ca.uqac.lif.cep.shaded;

public class ShadedQuantifiedVariable implements ShadedFunction
{
	public static ShadedQuantifiedVariable v(String name)
	{
		return new ShadedQuantifiedVariable(name);
	}
	
	protected static boolean s_compareValues = false;
	
	protected final String m_name;
	
	protected Object m_value;
	
	public ShadedQuantifiedVariable(String name)
	{
		super();
		m_name = name;
	}
	
	@Override
	public void trim()
	{
		// Do nothing
	}
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public ShadedFunction update(Object event)
	{
		return this;
	}

	@Override
	public int getArity()
	{
		return 0;
	}

	@Override
	public ShadedQuantifiedVariable getOperand(int index)
	{
		return null;
	}

	@Override
	public ShadedQuantifiedVariable duplicate(boolean with_state)
	{
		ShadedQuantifiedVariable sqv = new ShadedQuantifiedVariable(m_name);
		//if (with_state)
		{
			sqv.m_value = m_value;
		}
		return sqv;
	}

	@Override
	public ShadedQuantifiedVariable duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String getSymbol()
	{
		return m_name + (m_value != null ? "[" + m_value + "]" : "");
	}
	
	@Override
	public String toString()
	{
		return getSymbol();
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}

	@Override
	public boolean sameAs(ShadedFunction f)
	{
		if (!(f instanceof ShadedQuantifiedVariable))
		{
			return false;
		}
		ShadedQuantifiedVariable sqv = (ShadedQuantifiedVariable) f;
		if (m_name.compareTo(sqv.m_name) != 0)
		{
			return false;
		}
		if (!s_compareValues)
		{
			return true;
		}
		if ((m_value == null) != (sqv.m_value == null))
		{
			return false;
		}
		if (m_value == null)
		{
			return true;
		}
		return m_value.equals(sqv.m_value);
	}

	@Override
	public void setValue(String name, Object value)
	{
		if (m_name.compareTo(name) == 0)
		{
			m_value = value;
		}
	}
}
