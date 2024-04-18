package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XPathExpression;
import ca.uqac.lif.xml.XmlElement;

public abstract class ShadedQuantifier extends ShadedConnective
{
	protected final XPathExpression m_pi;

	protected final String m_x;

	protected final ShadedConnective m_phi;

	protected List<ShadedConnective> m_instances;

	public ShadedQuantifier(String x, XPathExpression pi, ShadedConnective phi)
	{
		super();
		m_x = x;
		m_pi = pi;
		m_phi = phi;
		m_instances = null;
	}
	
	@Override
	public int size()
	{
		int size = 1;
		for (ShadedConnective op : m_instances)
		{
			size += op.size();
		}
		return size;
	}

	@Override
	public int getArity()
	{
		return m_instances.size();
	}

	@Override
	public ShadedFunction getOperand(int index)
	{
		return m_instances.get(index);
	}

	@Override
	public void setValue(String name, Object value)
	{
		for (ShadedConnective c : m_instances)
		{
			c.setValue(name, value);
		}
	}

	@Override
	public ShadedQuantifier update(Object o)
	{
		if (m_instances == null)
		{
			m_instances = new ArrayList<>();
			List<Object> values = getValues(o);
			for (Object v : values)
			{
				ShadedConnective phi_dup = m_phi.duplicate();
				phi_dup.setValue(m_x, v);
				m_instances.add(phi_dup);
			}
		}
		for (ShadedConnective c : m_instances)
		{
			c.update(o);
		}
		m_color = updateVerdict();
		return this;
	}

	protected boolean sameSubtrees(ShadedQuantifier q)
	{
		if (!m_phi.sameAs(q.m_phi))
		{
			return false;
		}
		if ((m_instances == null) != (q.m_instances == null))
		{
			return false;
		}
		if (m_instances == null)
		{
			return true;
		}
		if (m_instances.size() != q.m_instances.size())
		{
			return false;
		}
		for (int i = 0; i < m_instances.size(); i++)
		{
			if (!m_instances.get(i).sameAs(q.m_instances.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	protected abstract Color updateVerdict();

	protected List<Object> getValues(Object o)
	{
		if (!(o instanceof XmlElement))
		{
			throw new IllegalArgumentException("Input event must be an XML element");
		}
		XmlElement x = (XmlElement) o;
		Collection<XmlElement> coll = m_pi.evaluate(x);
		List<Object> vals = new ArrayList<>(coll.size());
		for (XmlElement xe : coll)
		{
			vals.add(cast(xe));
		}
		return vals;
	}

	public static Object cast(XmlElement xe)
	{
		if (!(xe instanceof TextElement))
		{
			return xe;
		}
		TextElement te = (TextElement) xe;
		String text = te.getText().trim();
		try
		{
			Float f = Float.parseFloat(text);
			return f;
		}
		catch (NumberFormatException e)
		{
			return text;
		}
	}
}
