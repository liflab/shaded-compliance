package treecompliancelab.data.bank;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.sequence.Playback;

public class ExclusiveGatewayTest
{
	@Test
	public void test1()
	{
		ExclusiveGateway<Integer> eg = new ExclusiveGateway<>(new RandomFloat(), new Playback<Integer>(new Integer[] {1, 2, 3}).setLoop(false), new Playback<Integer>(new Integer[] {4, 5, 6}).setLoop(false));
		List<Integer> list = new ArrayList<>();
		while (!eg.isDone())
		{
			list.add(eg.pick());
		}
		assertEquals(3, list.size());
		assertNotEquals(list.contains(1), list.contains(4));
	}
}
