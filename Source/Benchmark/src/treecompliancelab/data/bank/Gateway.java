package treecompliancelab.data.bank;

import ca.uqac.lif.synthia.Bounded;

public interface Gateway<T> extends Bounded<T>
{
	public void restart();
}
