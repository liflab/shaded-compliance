package treecompliancelab.data.bank;

import ca.uqac.lif.synthia.sequence.Playback;

public class ActivitySequence<T> extends Playback<T> implements Gateway<T>
{
	@SuppressWarnings("unchecked")
	public ActivitySequence(T ... elements)
	{
		super(elements);
	}

	@Override
	public void restart()
	{
		reset();
	}
	
	@Override
	public ActivitySequence<T> setLoop(boolean b)
	{
		super.setLoop(b);
		return this;
	}
}
