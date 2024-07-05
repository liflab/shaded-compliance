package treecompliancelab.data.bank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.uqac.lif.synthia.NoMoreElementException;

public class LinearPath<T> implements Gateway<T>
{
	protected List<Gateway<T>> m_list;
	
	protected int m_currentIndex;
	
	@SafeVarargs
	public LinearPath(Gateway<T> ... boundeds)
	{
		this(Arrays.asList(boundeds));
	}
	
	public LinearPath(List<Gateway<T>> boundeds)
	{
		super();
		m_list = new ArrayList<Gateway<T>>();
		m_list.addAll(boundeds);
		m_currentIndex = 0;
	}

	@Override
	public void reset()
	{
		m_currentIndex = 0;
		for (Gateway<?> b : m_list)
		{
			b.reset();
		}
	}
	
	@Override
	public void restart()
	{
		m_currentIndex = 0;
		for (Gateway<?> b : m_list)
		{
			b.restart();
		}
	}

	@Override
	public T pick()
	{
		while (m_currentIndex < m_list.size() && m_list.get(m_currentIndex).isDone())
		{
			m_currentIndex++;
		}
		if (m_currentIndex >= m_list.size())
		{
			throw new NoMoreElementException();
		}
		return m_list.get(m_currentIndex).pick();
	}

	@Override
	public LinearPath<T> duplicate(boolean with_state)
	{
		List<Gateway<T>> list = new ArrayList<Gateway<T>>();
		for (Gateway<T> b : m_list)
		{
			list.add((Gateway<T>) b.duplicate(with_state));
		}
		return new LinearPath<T>(list);
	}

	@Override
	public boolean isDone()
	{
		while (m_currentIndex < m_list.size() && m_list.get(m_currentIndex).isDone())
		{
			m_currentIndex++;
		}
		if (m_currentIndex >= m_list.size())
		{
			return true;
		}
		return false;
	}
}
