package ca.uqac.lif.cep.shaded;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.enumerative.AllBooleans;
import ca.uqac.lif.synthia.enumerative.AllPickers;
import ca.uqac.lif.synthia.relative.PickIf;

public class CombinationPicker implements Bounded<List<Integer>>
{
	/**
	 * Number of elements to choose from.
	 */
	protected final int m_n;
	
	/**
	 * Number of elements to choose.
	 */
	protected final int m_k;
	
	/**
	 * Queue of combinations to enumerate.
	 */
	protected final Queue<List<Integer>> m_toEnumerate;

	public CombinationPicker(int n, int k)
	{
		super();
		m_n = n;
		m_k = k;
		m_toEnumerate = new ArrayDeque<>();
		populate();
	}
	
	@Override
	public CombinationPicker duplicate(boolean with_state)
	{
		return null;
	}

	@Override
	public List<Integer> pick()
	{
		
	}

	@Override
	public void reset()
	{
		populate();
	}

	@Override
	public boolean isDone()
	{
		return m_toEnumerate.isEmpty();
	}
	
	protected void populate()
	{
		m_toEnumerate.clear();
		Bounded<?>[] bools = new Bounded[m_n];
		for (int i = 0; i < m_n; i++)
		{
			bools[i] = new AllBooleans();
		}
		AllPickers all = new AllPickers(bools);
		while (!all.isDone())
		{
			Object[] subset = all.pick();
			List<Integer> l_subset = new ArrayList<>();
			for (int i = 0; i < subset.length; i++)
			{
				if (Boolean.TRUE.equals(subset[i]))
				{
					l_subset.add(i);
				}
			}
			if (l_subset.size() != m_k)
			{
				continue;
			}
			addPermutations(l_subset);
		}
	}
	
	protected void addPermutations(List<Integer> c)
	{
		m_toEnumerate.add(toList(c));
		int i = 0;
		while (i < c.length)
		{
			if ()
	}
	
	protected static List<Integer> toList(Object[] array)
	{
		List<Integer> list = new ArrayList<>();
		for (Object o : array)
		{
			list.add((Integer) o);
		}
		return list;
	}

}