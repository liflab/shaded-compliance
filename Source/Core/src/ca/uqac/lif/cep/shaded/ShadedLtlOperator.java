package ca.uqac.lif.cep.shaded;

public abstract class ShadedLtlOperator extends ShadedNaryConnective
{
	protected final ShadedConnective m_phi;
	
	public ShadedLtlOperator(ShadedConnective phi)
	{
		super();
		m_phi = phi;
	}
}
