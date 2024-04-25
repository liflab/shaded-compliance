package ca.uqac.lif.cep.shaded;

import ca.uqac.lif.cep.shaded.ShadedComparison.ShadedEquals;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;
import ca.uqac.lif.xml.XPathExpression;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;
import ca.uqac.lif.xml.XmlElement;

public class ShadedFetchPath implements ShadedFunction
{
	protected static boolean s_compareValues = false;
	
	public static ShadedFetchPath path(String path)
	{
		return new ShadedFetchPath(path);
	}
	
	protected final String m_path;
	
	protected XPathExpression m_pi;
	
	protected Object m_value;
	
	public ShadedFetchPath(String path)
	{
		super();
		m_path = path;
		m_value = null;
		try
		{
			m_pi = XPathExpression.parse(path);
		}
		catch (XPathParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_pi = null;
		}
	}
	
	@Override
	public void trim()
	{
		// Do nothing
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
	public boolean sameAs(ShadedFunction o)
	{
		if (!(o instanceof ShadedFetchPath))
		{
			return false;
		}
		ShadedFetchPath sfa = (ShadedFetchPath) o;
		return m_path.compareTo(sfa.m_path) == 0 && (!s_compareValues || ShadedEquals.equals(m_value, sfa.m_value) == Color.GREEN);
	}

	@Override
	public ShadedFetchPath update(Object event)
	{
		if (m_value != null)
		{
			return this;
		}
		XmlElement xml = (XmlElement) event;
		m_value = ShadedQuantifier.cast(m_pi.evaluateAny(xml));
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
	public ShadedFetchPath duplicate(boolean with_state)
	{
		ShadedFetchPath fa = new ShadedFetchPath(m_path);
		if (with_state)
		{
			fa.m_value = m_value;
		}
		return fa;
	}

	@Override
	public ShadedFetchPath duplicate()
	{
		return duplicate(false);
	}
	
	@Override
	public String toString()
	{
		return "?" + m_path;
	}

	@Override
	public String getSymbol()
	{
		return "<i>" + m_path + "</i>";
	}

	@Override
	public Object getValue()
	{
		return m_value;
	}
}
