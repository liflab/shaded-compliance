package ca.uqac.lif.cep.shaded;

public interface ShadedFunction
{
	public ShadedFunction update(Object event);
	
	public int getArity();
	
	public ShadedFunction getOperand(int index);
	
	public ShadedFunction duplicate(boolean with_state);
	
	public ShadedFunction duplicate();
	
	public String getSymbol();
	
	public Object getValue();
	
	public boolean sameAs(ShadedFunction f);
	
	public void setValue(String name, Object value);
	
	public int size();
}
