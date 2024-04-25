package treecompliancelab;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.util.BoundedBufferedPicker;

/**
 * Picks pairs of log entries from a source of log entries.
 */
public class LogPairPicker<T> extends BoundedBufferedPicker<List<T>[]>
{
	protected final Bounded<List<T>> m_source;

	public LogPairPicker(Bounded<List<T>> source)
	{
		super();
		m_source = source;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillQueue()
	{
		List<List<T>> logs = new ArrayList<List<T>>();
		while (!m_source.isDone())
		{
			logs.add(m_source.pick());
		}
		for (int i = 0; i < logs.size(); i++)
		{
			for (int j = i + 1; j < logs.size(); j++)
			{
				List<T>[] pair = new List[2];
				pair[0] = logs.get(i);
				pair[1] = logs.get(j);
				m_queue.add(pair);
			}
		}
	}
	
	public int countPairs()
	{
		return m_queue.size();
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_source.reset();
	}

	@Override
	public LogPairPicker<T> duplicate(boolean with_state)
	{
		throw new UnsupportedOperationException("This picker cannot be duplicated");
	}
}
