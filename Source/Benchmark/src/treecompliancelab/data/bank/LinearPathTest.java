package treecompliancelab.data.bank;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LinearPathTest
{
	@Test
	public void test1()
	{
		LinearPath<Integer> lp = new LinearPath<>(new ActivitySequence<Integer>(new Integer[] {1, 2, 3}).setLoop(false), new ActivitySequence<Integer>(new Integer[] {4, 5, 6}).setLoop(false));
		List<Integer> list = new ArrayList<>();
		while (!lp.isDone())
		{
			list.add(lp.pick());
		}
		assertEquals(6, list.size());
		assertEquals(1, list.get(0).intValue());
		assertEquals(2, list.get(1).intValue());
		assertEquals(3, list.get(2).intValue());
		assertEquals(4, list.get(3).intValue());
		assertEquals(5, list.get(4).intValue());
		assertEquals(6, list.get(5).intValue());
	}
}
