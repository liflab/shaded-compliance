package ca.uqac.lif.cep.shaded;

public interface ShadedFunction
{
	public void update(Object event);
	
	public int getArity();
	
	public ShadedFunction getOperand(int index);
	
	public ShadedFunction duplicate(boolean with_state);
	
	public ShadedFunction duplicate();
	
	public String getSymbol();
	
	public Object getValue();
}
