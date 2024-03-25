package ca.uqac.lif.cep.shaded;

public interface Polarized
{
	public static enum Polarity
	{
		POSITIVE,
		NEGATIVE
	}
	
	public static Polarity invert(Polarity p)
	{
		if (p == Polarity.POSITIVE)
		{
			return Polarity.NEGATIVE;
		}
		return Polarity.POSITIVE;
	}
	
	public void setPolarity(Polarity p);
	
	public Polarity getPolarity();
}
