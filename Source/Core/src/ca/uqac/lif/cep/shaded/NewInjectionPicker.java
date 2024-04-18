package ca.uqac.lif.cep.shaded;

import java.util.BitSet;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.NoMoreElementException;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.enumerative.AllIntegers;
import ca.uqac.lif.synthia.enumerative.AllPickers;

public class NewInjectionPicker implements Bounded<Integer[]>
{
	/**
	 * Number of elements to choose from.
	 */
	protected final int m_n;

	/**
	 * Number of elements to choose.
	 */
	protected final int m_k;
	
	protected final AllPickers m_indexPicker;
	
	protected Integer[] m_nextInjection;
	
	protected final BitSet m_bitset;

	public NewInjectionPicker(int k, int n)
	{
		super();
		if (n < k)
		{
			throw new IllegalArgumentException("n must be greater than or equal to k");
		}
		m_n = n;
		m_k = k;
		m_bitset = new BitSet(m_k);
		Bounded<?>[] indices = new Bounded[m_k];
		for (int i = 0; i < indices.length; i++)
		{
			indices[i] = new AllIntegers(0, m_n - 1);
		}
		m_indexPicker = new AllPickers(indices);
		m_nextInjection = new Integer[m_k];
		findNextInjection();
	}

	@Override
	public Picker<Integer[]> duplicate(boolean with_state)
	{
		throw new UnsupportedOperationException("This picker cannot be duplicated");
	}

	@Override
	public Integer[] pick()
	{
		Integer[] next_injection = m_nextInjection;
		if (next_injection == null)
		{
			throw new NoMoreElementException();
		}
		findNextInjection();
		return next_injection;
	}

	@Override
	public void reset()
	{
		m_indexPicker.reset();
	}

	@Override
	public boolean isDone()
	{
		if (m_nextInjection != null)
		{
			return false;
		}
		findNextInjection();
		return m_nextInjection == null;
	}
	
	protected void findNextInjection()
	{
		while (!m_indexPicker.isDone())
		{
			Object[] new_injection = m_indexPicker.pick();
			m_bitset.clear();
			boolean valid = true;
			for (Object o : new_injection)
			{
				int n = (Integer) o;
				if (m_bitset.get(n))
				{
					valid = false;
					break;
				}
				m_bitset.set(n, true);
			}
			if (valid)
			{
				m_nextInjection = new Integer[m_k];
				for (int i = 0; i < new_injection.length; i++)
				{
					m_nextInjection[i] = (Integer) new_injection[i];
				}
				return;
			}
		}
		m_nextInjection = null;
	}
}
