/*
    A tree-based process compliance library
    Copyright (C) 2024 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
