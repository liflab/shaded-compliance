package treecompliancelab.data.bank;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.uqac.lif.synthia.random.RandomFloat;

public class ParallelGatewayTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void test1()
	{
		ParallelGateway<String> interleave = new ParallelGateway<>(new RandomFloat(), new ActivitySequence<String>("a", "b", "c").setLoop(false), new ActivitySequence<String>("d", "e", "f").setLoop(false));
		List<String> interleft = new ArrayList<>();
		while (!interleave.isDone())
		{
			String s = interleave.pick();
			interleft.add(s);
		}
		assertEquals(6, interleft.size());
		assertTrue(interleft.contains("a"));
		assertTrue(interleft.contains("b"));
		assertTrue(interleft.contains("c"));
		assertTrue(interleft.contains("d"));
		assertTrue(interleft.contains("e"));
		assertTrue(interleft.contains("f"));
	}
}
