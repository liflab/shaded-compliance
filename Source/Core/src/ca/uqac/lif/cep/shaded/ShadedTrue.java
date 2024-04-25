package ca.uqac.lif.cep.shaded;

/**
 * A constant true connective.
 * @author Sylvain Hallé
 */
public class ShadedTrue extends ShadedConnective
{
	public ShadedTrue()
	{
		super();
		m_color = Color.GREEN;
	}
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public void trim()
	{
		// Do nothing
	}
	
	@Override
	public void setValue(String name, Object value)
	{
		// Do nothing
	}
	
	@Override
	public ShadedTrue update(Object event)
	{
		return this;
	}
	
	@Override
	protected void toString(StringBuilder out)
	{
		out.append("T");
	}
	
	@Override
	public int hashCode()
	{
		return "T".hashCode();
	}
	
	@Override
	public boolean sameAs(ShadedFunction o)
	{
		return o instanceof ShadedTrue;
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
	public ShadedTrue duplicate(boolean with_state)
	{
		ShadedTrue t = new ShadedTrue();
		t.setPolarity(m_polarity);
		return t;
	}
	
	@Override
	public ShadedTrue duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "\u22a4";
	}
	
	@Override
	public String getSymbol()
	{
		return "\u22a4";
	}
}
