package treecompliancelab.data.bank;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.Picker;

public class ExclusiveGateway<T> implements Bounded<T>
{
	protected final Picker<Float> m_floatSource;
	
	protected final Bounded<T>[] m_pickers;
	
	protected int m_chosenPicker;
	
	@SuppressWarnings("unchecked")
	public ExclusiveGateway(Picker<Float> float_source, Bounded<T>... pickers)
	{
		super();
		m_floatSource = float_source;
		m_pickers = pickers;
		m_chosenPicker = -1;
	}
	
	@Override
	public void reset()
	{
		for (Bounded<T> p : m_pickers)
		{
			p.reset();
		}
		m_chosenPicker = -1;
		m_floatSource.reset();
	}

	@Override
	public T pick()
	{
		if (m_chosenPicker == -1)
		{
			m_chosenPicker = (int) Math.floor(m_floatSource.pick() * m_pickers.length);
		}
		return m_pickers[m_chosenPicker].pick();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExclusiveGateway<T> duplicate(boolean with_state)
	{
		Bounded<T>[] pickers = new Bounded[m_pickers.length];
		for (int i = 0; i < m_pickers.length; i++)
		{
			pickers[i] = (Bounded<T>) m_pickers[i].duplicate(with_state);
		}
		return new ExclusiveGateway<>(m_floatSource.duplicate(with_state), pickers);
	}

	@Override
	public boolean isDone()
	{
		if (m_chosenPicker == -1)
		{
			m_chosenPicker = (int) Math.floor(m_floatSource.pick() * m_pickers.length);
		}
		return m_pickers[m_chosenPicker].isDone();
	}
}
