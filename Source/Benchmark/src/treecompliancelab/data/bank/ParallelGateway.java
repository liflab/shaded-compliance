package treecompliancelab.data.bank;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.NoMoreElementException;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.sequence.Knit;
import ca.uqac.lif.synthia.util.Constant;

public class ParallelGateway<T> extends Knit<T> implements Bounded<T>
{
	@SuppressWarnings("unchecked")
	public ParallelGateway(Picker<Float> float_source, Bounded<T> ... pickers)
	{
		super(new Constant<Picker<T>>(pickers[0]), new Constant<Boolean>(false), new Constant<Boolean>(false), float_source);
		for (Bounded<T> p : pickers)
		{
			m_instances.add(p);
		}
	}
	
	@Override
	public boolean isDone()
	{
		for (Picker<T> p : m_instances)
		{
			if (!((Bounded<T>) p).isDone())
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public T pick()
	{
		if (m_instances.isEmpty() || m_newInstance.pick())
		{
			// Spawn a new instance
			Picker<T> new_instance = m_instancePicker.pick();
			m_instances.add(new_instance);
		}
		for (int i = 0; i < s_maxTries && !m_instances.isEmpty(); i++)
		{
			int index = (int) Math.floor(((float) m_instances.size()) * m_floatSource.pick());
			Picker<T> current_instance = m_instances.get(index);
			try
			{
				return current_instance.pick();
			}
			catch (NoMoreElementException e)
			{
				m_instances.remove(index);
			}
		}
		throw new NoMoreElementException();
	}
}
