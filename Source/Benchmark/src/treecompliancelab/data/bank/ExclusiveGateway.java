package treecompliancelab.data.bank;

import ca.uqac.lif.synthia.Picker;

public class ExclusiveGateway<T> implements Gateway<T>
{
	protected final Picker<Float> m_floatSource;
	
	protected final Gateway<T>[] m_pickers;
	
	protected int m_chosenPicker;
	
	@SafeVarargs
	public ExclusiveGateway(Picker<Float> float_source, Gateway<T>... pickers)
	{
		super();
		m_floatSource = float_source;
		m_pickers = pickers;
		m_chosenPicker = -1;
	}
	
	@Override
	public void reset()
	{
		for (Gateway<T> p : m_pickers)
		{
			p.reset();
		}
		m_chosenPicker = -1;
		m_floatSource.reset();
	}
	
	@Override
	public void restart()
	{
		for (Gateway<T> p : m_pickers)
		{
			p.restart();
		}
		m_chosenPicker = -1;
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
		Gateway<T>[] pickers = new Gateway[m_pickers.length];
		for (int i = 0; i < m_pickers.length; i++)
		{
			pickers[i] = (Gateway<T>) m_pickers[i].duplicate(with_state);
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
