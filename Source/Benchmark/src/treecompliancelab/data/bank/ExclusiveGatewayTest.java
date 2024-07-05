package treecompliancelab.data.bank;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.uqac.lif.synthia.random.RandomFloat;

public class ExclusiveGatewayTest
{
	@Test
	public void test1()
	{
		ExclusiveGateway<Integer> eg = new ExclusiveGateway<>(new RandomFloat(), new ActivitySequence<Integer>(new Integer[] {1, 2, 3}).setLoop(false), new ActivitySequence<Integer>(new Integer[] {4, 5, 6}).setLoop(false));
		List<Integer> list = new ArrayList<>();
		while (!eg.isDone())
		{
			list.add(eg.pick());
		}
		assertEquals(3, list.size());
		assertNotEquals(list.contains(1), list.contains(4));
	}
}
