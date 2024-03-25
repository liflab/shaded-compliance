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
	public void update(Object event)
	{
		// Do nothing
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
		return "false";
	}
	
	@Override
	public String getSymbol()
	{
		return "F";
	}
}
