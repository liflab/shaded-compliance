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
	public ShadedTrue update(Object event)
	{
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
		return "true";
	}
	
	@Override
	public String getSymbol()
	{
		return "T";
	}
}
