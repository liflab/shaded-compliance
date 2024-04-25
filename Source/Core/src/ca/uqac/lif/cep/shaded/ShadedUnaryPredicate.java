package ca.uqac.lif.cep.shaded;

public abstract class ShadedUnaryPredicate extends ShadedConnective
{
	public static IsEven even(Object argument)
	{
		return new IsEven(ShadedConstant.wrap(argument));
	}

	protected final ShadedFunction m_argument;

	public ShadedUnaryPredicate(ShadedFunction arg)
	{
		super();
		m_argument = arg;
		m_color = null;
	}
	
	@Override
	public int size()
	{
		return 1 + m_argument.size();
	}

	@Override
	public int getArity()
	{
		return 1;
	}
	
	@Override
	public void trim()
	{
		m_argument.trim();
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		if (index == 0)
		{
			return m_argument;
		}
		return null;
	}

	@Override
	public void setValue(String name, Object value)
	{
		m_argument.setValue(name, value);
	}

	public static class IsEven extends ShadedUnaryPredicate
	{
		public IsEven(ShadedFunction arg)
		{
			super(arg);
		}

		@Override
		public IsEven duplicate(boolean with_state)
		{
			IsEven ie = new IsEven(m_argument.duplicate(with_state));
			if (with_state)
			{
				ie.m_color = m_color;
			}
			return ie;
		}
		
		@Override
		protected void toString(StringBuilder out)
		{
			out.append("even");
		}

		@Override
		public ShadedUnaryPredicate update(Object event)
		{
			if (m_color == null)
			{
				m_argument.update(event);
				Object o = m_argument.getValue();
				m_color = (o instanceof Number && ((Number) o).intValue() % 2 == 0) ? Color.GREEN : Color.RED;
			}
			return this;
		}

		@Override
		public String getSymbol()
		{
			return "even";
		}

		@Override
		public boolean sameAs(ShadedFunction f)
		{
			if (!(f instanceof IsEven))
			{
				return false;
			}
			IsEven ie = (IsEven) f;
			return m_argument.sameAs(ie.m_argument);
		}
	}
}
