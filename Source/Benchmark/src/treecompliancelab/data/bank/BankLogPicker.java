package treecompliancelab.data.bank;

import java.util.List;
import java.util.Map;

import ca.uqac.lif.synthia.Picker;
import treecompliancelab.LogPicker;

public class BankLogPicker implements LogPicker<Map<String,Object>>
{
	protected final int m_numLogs;
	
	protected int m_logCount;
	
	public BankLogPicker(int num_logs)
	{
		super();
		m_numLogs = num_logs;
		m_logCount = 0;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset()
	{
		m_logCount = 0;
	}
}
