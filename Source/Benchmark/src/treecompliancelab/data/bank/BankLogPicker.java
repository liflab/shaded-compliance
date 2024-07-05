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
package treecompliancelab.data.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.synthia.Picker;
import treecompliancelab.LogPicker;

public class BankLogPicker implements LogPicker<Map<String,Object>>
{
	protected final int m_numLogs;
	
	protected int m_logCount;
	
	protected BankWorkflowPicker m_workflowPicker;
	
	public BankLogPicker(int num_logs, Picker<Float> float_source)
	{
		super();
		m_numLogs = num_logs;
		m_logCount = 0;
		m_workflowPicker = new BankWorkflowPicker(float_source);
	}
	
	@Override
	public boolean isDone()
	{
		return m_logCount >= m_numLogs;
	}

	@Override
	public Picker<List<Map<String,Object>>> duplicate(boolean with_state)
	{
		// In the context, not necessary to implement
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Map<String, Object>> pick()
	{
		List<String> workflow = new ArrayList<>();
		m_workflowPicker.restart();
		while (!m_workflowPicker.isDone())
		{
			workflow.add(m_workflowPicker.pick());
		}
		BankPopulator pop = new BankPopulator();
		List<Map<String,Object>> trace = pop.populate(workflow);
		m_logCount++;
		return trace;
	}

	@Override
	public void reset()
	{
		m_logCount = 0;
		m_workflowPicker.reset();
	}
}
