/*
    A tree-based process compliance library
    Copyright (C) 2024 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.shaded;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.NoMoreElementException;

public class InjectionPicker implements Bounded<Integer[]>
{
	/**
	 * Number of elements to choose from.
	 */
	protected final int m_n;

	/**
	 * Number of elements to choose.
	 */
	protected final int m_k;

	protected StackFrame[] m_stack;

	protected Set<Integer>[] m_forbidden;

	protected boolean m_done = false;

	@SuppressWarnings("unchecked")
	public InjectionPicker(int k, int n)
	{
		super();
		if (n < k)
		{
			throw new IllegalArgumentException("n must be greater than or equal to k");
		}
		m_n = n;
		m_k = k;
		m_stack = new StackFrame[k];
		m_forbidden = new HashSet[k];
		reset();
	}

	@Override
	public Integer[] pick()
	{
		if (isDone())
		{
			throw new NoMoreElementException();
		}
		Integer[] next_injection = new Integer[m_k];
		for (int i = 0; i < m_k; i++)
		{
			next_injection[i] = m_stack[i].getValue();
		}
		findNextInjection();
		return next_injection;
	}

	@Override
	public boolean isDone()
	{
		if (m_done)
		{
			return true;
		}
		if (!isValid())
		{
			// If the current injection is not valid, we need to find the next one
			findNextInjection();
		}
		return m_done;
	}

	public void forbid(int index, int value)
	{
		m_forbidden[index].add(value);
		if (!isValid())
		{
			// If the current injection is not valid, we need to find the next one
			findNextInjection();
		}
	}

	/**
	 * Moves the state of the stack to the next valid injection.
	 */
	protected void findNextInjection()
	{
		if (m_done)
		{
			return;
		}
		do
		{
			for (int i = m_k - 1; i >= 0; i--)
			{
				m_stack[i] = m_stack[i].next();
				if (i == 0 && m_stack[i] == null)
				{
					m_done = true;
					break;
				}
				if (m_stack[i] != null)
				{
					for (int j = i + 1; j < m_k; j++)
					{
						m_stack[j] = m_stack[j - 1].nextNeighbor();
						if (m_stack[j] == null)
						{
							m_done = true;
							break;
						}
					}
					break;
				}
			}
		} while (!m_done && !isValid());
	}

	protected boolean isValid()
	{
		if (m_stack[0] == null)
		{
			return false;
		}
		for (int i = 0; i < m_k; i++)
		{
			if (m_stack[i] == null)
			{
				return false;
			}
			if (m_forbidden[i].contains(m_stack[i].getValue()))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public InjectionPicker duplicate(boolean with_state)
	{
		throw new UnsupportedOperationException("This picker cannot be duplicated");
	}

	@Override
	public void reset()
	{
		for (int i = 0; i < m_k; i++)
		{
			m_forbidden[i] = new HashSet<Integer>();
		}
		m_stack[0] = new StackFrame(0, 0);
		for (int i = 1; i < m_stack.length; i++)
		{
			m_stack[i] = m_stack[i - 1].nextNeighbor();
		}
	}

	protected class StackFrame
	{
		/**
		 * The set of indices that have already been taken.
		 */
		private final Set<Integer> m_taken;

		private final int m_value;

		private final int m_index;

		public StackFrame(int index, int value, Set<Integer> taken)
		{
			super();
			m_index = index;
			m_value = value;
			m_taken = new HashSet<>(taken);
		}

		public StackFrame(int index, int value)
		{
			super();
			m_index = index;
			m_value = value;
			m_taken = new HashSet<>(1);
		}
		public int getValue()
		{
			return m_value;
		}

		public Set<Integer> getTaken()
		{
			return m_taken;
		}

		public StackFrame next()
		{
			for (int i = m_value + 1; i < m_n; i++)
			{
				if (!m_taken.contains(i) && !m_forbidden[m_index].contains(i))
				{
					StackFrame next = new StackFrame(m_index, i, m_taken);
					return next;
				}
			}
			return null;
		}

		public StackFrame nextNeighbor()
		{
			for (int i = 0; i < m_n; i++)
			{
				if (!m_taken.contains(i) && i != m_value && !m_forbidden[m_index + 1].contains(i))
				{
					StackFrame next = new StackFrame(m_index + 1, i);
					next.m_taken.addAll(m_taken);
					next.m_taken.add(m_value);
					return next;
				}
			}
			return null;
		}

		@Override
		public String toString()
		{
			return m_value + "(" + m_taken + ")";
		}
	}
}
