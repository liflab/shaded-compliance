package treecompliancelab;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.util.BoundedBufferedPicker;
import ca.uqac.lif.xml.XmlElement;

/**
 * Picks pairs of log entries from a source of log entries.
 */
public class LogPairPicker extends BoundedBufferedPicker<List<XmlElement>[]>
{
	protected final Bounded<List<XmlElement>> m_source;

	public LogPairPicker(Bounded<List<XmlElement>> source)
	{
		super();
		m_source = source;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillQueue()
	{
		List<List<XmlElement>> logs = new ArrayList<List<XmlElement>>();
		while (!m_source.isDone())
		{
			logs.add(m_source.pick());
		}
		for (int i = 0; i < logs.size(); i++)
		{
			for (int j = i + 1; j < logs.size(); j++)
			{
				List<XmlElement>[] pair = new List[2];
				pair[0] = logs.get(i);
				pair[1] = logs.get(j);
				m_queue.add(pair);
			}
		}
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_source.reset();
	}

	@Override
	public LogPairPicker duplicate(boolean with_state)
	{
		throw new UnsupportedOperationException("This picker cannot be duplicated");
	}
}
