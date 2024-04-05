package ca.uqac.lif.cep.shaded;

public class ShadedQuantifiedVariable implements ShadedFunction
{
	protected final String m_name;
	
	protected Object m_value;
	
	public ShadedQuantifiedVariable(String name)
	{
		super();
		m_name = name;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShadedQuantifiedVariable duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String getSymbol()
	{
		return m_name;
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
		return m_name.compareTo(((ShadedQuantifiedVariable) f).m_name) == 0;
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
