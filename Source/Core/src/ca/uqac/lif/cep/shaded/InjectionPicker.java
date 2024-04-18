package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.enumerative.AllBooleans;
import ca.uqac.lif.synthia.enumerative.AllPickers;
import ca.uqac.lif.synthia.util.BoundedBufferedPicker;

/**
 * Enumerates all possible injections from a set of <i>k</i> elements to a set of
 * <i>n</i> elements. An injection is a function that maps each element of the
 * first set to a unique element of the second set. If elements of both sets are
 * in the form [1, &hellip;, <i>k</i>] and [1, &hellip;, <i>n</i>],
 * respectively, an injection be represented as a vector &langle;x<sub>1</sub>,
 * &hellip;, <i>x</i><sub><i>n</i></sub>&rangle;, where
 * <i>x</i><sub><i>i</i></sub> is the element of the second set associated to
 * element <i>i</i> in the first. Therefore, the number of injections is
 * given by <i>k</i>! &times; C(<i>n</i>,<i>k</i>). Indeed, there are
 * C(<i>n</i>,<i>k</i>) ways of picking <i>k</i> elements from a set of
 * <i>n</i>, and  <i>k</i>! ways of ordering each of them.
 */
public class InjectionPicker extends BoundedBufferedPicker<Integer[]>
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
	 * The list of permutation "templates". A template is a permutation of the
	 * numbers from 0 to m_k.
	 */
	protected final List<Integer[]> m_templates;

	protected final AllPickers m_allBools;

	public InjectionPicker(int k, int n)
	{
		super();
		if (n < k)
		{
			throw new IllegalArgumentException("n must be greater than or equal to k");
		}
		m_n = n;
		m_k = k;
		m_templates = populateTemplates(m_k);
		Bounded<?>[] bools = new Bounded[m_n];
		for (int i = 0; i < m_n; i++)
		{
			bools[i] = new AllBooleans();
		}
		m_allBools = new AllPickers(bools);
	}

	@Override
	public InjectionPicker duplicate(boolean with_state)
	{
		return null;
	}

	@Override
	protected void fillQueue()
	{
		if (m_allBools.isDone())
		{
			return;
		}
		while (!m_allBools.isDone())
		{
			Object[] subset = m_allBools.pick();
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
			break;
		}
	}

	/**
	 * Populates the list of templates with all possible <i>k</i>-permutations of
	 * {0, &hellip;, <i>k</i>}. For example, if <i>k</i> = 3, the list will contain
	 * the following arrays:
	 * <ul>
	 * <li>[0, 1, 2]</li>
	 * <li>[0, 2, 1]</li>
	 * <li>[1, 0, 2]</li>
	 * <li>[1, 2, 0]</li>
	 * <li>[2, 0, 1]</li>
	 * <li>[2, 1, 0]</li>
	 * </ul>
	 * The method implements a
	 * <a href="https://www.baeldung.com/cs/array-generate-all-permutations">non-recursive
	 * version of Heap's algorithm</a>.
	 * @param k The value of <i>k</i> in the definition above
	 * @return The list of permutation templates
	 */
	protected static List<Integer[]> populateTemplates(int k)
	{
		List<Integer[]> templates = new ArrayList<>();
		List<Integer> c = new ArrayList<>(k);
		List<Integer> A = new ArrayList<>(k);
		for (int i = 1; i <= k; i++)
		{
			c.add(0);
			A.add(i - 1);
		}
		templates.add(toArray(A));
		int i = 0;
		while (i < k)
		{
			if (c.get(i) < i)
			{
				if (i % 2 == 0)
				{
					Collections.swap(A, 0, i);
				}
				else
				{
					Collections.swap(A, c.get(i), i);
				}
				templates.add(toArray(A));
				c.set(i, c.get(i) + 1);
				i = 0;
			}
			else
			{
				c.set(i, 0);
				i++;
			}
		}
		return templates;
	}

	/**
	 * Generates all permutations of a list of integers. 
	 * @param c
	 */
	protected void addPermutations(List<Integer> c)
	{
		for (Integer[] template : m_templates)
		{
			Integer[] mapping = new Integer[template.length];
			for (int i = 0; i < template.length; i++)
			{
				mapping[i] = c.get(template[i]);
			}
			m_queue.add(mapping);
		}
		System.out.println(m_templates.size() + " " + this);
	}

	/**
	 * Converts a list of integers to an array of integers.
	 * @param list The list to convert
	 * @return The array
	 */
	protected static Integer[] toArray(List<Integer> list)
	{
		return list.toArray(new Integer[list.size()]);
	}
}