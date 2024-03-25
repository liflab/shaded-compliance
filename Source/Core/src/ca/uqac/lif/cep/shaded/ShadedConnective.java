package ca.uqac.lif.cep.shaded;

public abstract class ShadedConnective implements ShadedFunction, Polarized
{
	public static enum Color
	{
		GREEN,
		RED
	}

	protected Polarity m_polarity;
	
	protected Color m_color;
	
	public ShadedConnective()
	{
		super();
		m_polarity = Polarity.POSITIVE;
		m_color = null;
	}
	
	public void setPolarity(Polarity p)
	{
		m_polarity = p;
	}
	
	public Polarity getPolarity()
	{
		return m_polarity;
	}
	
	@Override
	public Color getValue()
	{
		return m_color;
	}
	
	@Override
	public abstract ShadedConnective duplicate(boolean with_state);
	
	@Override
	public ShadedConnective duplicate()
	{
		return duplicate(false);
	}
}
