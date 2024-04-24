package ca.uqac.lif.cep.shaded;

/**
 * A constant true connective.
 * @author Sylvain Hallé
 */
public class ShadedFalse extends ShadedConnective
{
	public ShadedFalse()
	{
		super();
		m_color = Color.RED;
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
	protected void toString(StringBuilder out)
	{
		out.append("F");
	}

	@Override
	public ShadedFalse update(Object event)
	{
		return this;
	}

	@Override
	public boolean sameAs(ShadedFunction o)
	{
		return o instanceof ShadedFalse;
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
	public ShadedFalse duplicate(boolean with_state)
	{
		ShadedFalse t = new ShadedFalse();
		t.setPolarity(m_polarity);
		return t;
	}

	@Override
	public ShadedFalse duplicate()
	{
		return duplicate(false);
	}

	@Override
	public String toString()
	{
		return "\u22a5";
	}

	@Override
	public String getSymbol()
	{
		return "\u22a5";
	}
}
