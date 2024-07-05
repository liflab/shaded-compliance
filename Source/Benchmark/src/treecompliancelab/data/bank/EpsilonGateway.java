package treecompliancelab.data.bank;

import ca.uqac.lif.synthia.util.NothingPicker;

public class EpsilonGateway<T> extends NothingPicker<T> implements Gateway<T>
{
	@Override
	public void restart()
	{
		// Nothing to do
	}
}
