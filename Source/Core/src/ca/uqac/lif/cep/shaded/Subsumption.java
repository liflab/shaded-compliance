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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.cep.shaded.Polarized.Polarity;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class Subsumption implements TreeComparator
{
	protected final boolean m_compareNonConnectives;

	public Subsumption(boolean compare_non_connectives)
	{
		super();
		m_compareNonConnectives = compare_non_connectives;
	}

	public Subsumption()
	{
		this(false);
	}

	@Override
	public boolean inRelation(ShadedFunction f1, ShadedFunction f2)
	{
		if ((f1 instanceof ShadedConnective) != (f2 instanceof ShadedConnective))
		{
			// Comparison works only between two connectives or two *non* connectives
			return false;
		}
		if (f1 instanceof ShadedConnective && f2 instanceof ShadedConnective)
		{
			ShadedConnective c1 = (ShadedConnective) f1;
			ShadedConnective c2 = (ShadedConnective) f2;
			Polarized.Polarity pol_1 = c1.getPolarity();
			Polarized.Polarity pol_2 = c2.getPolarity();
			if (pol_1 != pol_2)
			{
				// Comparing trees with different polarity is meaningless
				return false;
			}
			try
			{
				return isSubsumed(c1, c2, pol_1);
			}
			catch (LoopInterruptedException e)
			{
				return false;
			}
		}
		if (m_compareNonConnectives)
		{
			return f1.sameAs(f2);
		}
		return false;
	}

	protected boolean isSubsumed(ShadedFunction f1, ShadedFunction f2, Polarity pol) throws LoopInterruptedException
	{
		// In the following, all color references in comments are for the positive polarity
		// Invert the colors to reason about the negative polarity
		Color col1, col2;
		if (pol == Polarity.POSITIVE)
		{
			col1 = Color.GREEN;
			col2 = Color.RED;
		}
		else
		{
			col1 = Color.RED;
			col2 = Color.GREEN;
		}
		if (f1.getValue() == col1 && f2.getValue() == col2)
		{
			// Color mismatch: f1 is green and f2 is red
			return false;
		}
		if (f1.getValue() == col1)
		{
			// All green children of f1 must be subsumed by some child of f2
			return hasMapping(f1, f2, col1, false);
		}
		else
		{
			// All red children of f2 must subsume some child of f1
			return hasMapping(f2, f1, col2, true);
		}
	}

	protected boolean hasMapping(ShadedFunction f1, ShadedFunction f2, Color col, boolean inverted) throws LoopInterruptedException
	{
		List<EquivalenceClass> children_from = new ArrayList<>();
		List<EquivalenceClass> children_to = new ArrayList<>();
		ShadedFunction from = f1, to = f2;
		boolean has_white = false;
		for (int i = 0; i < from.getArity(); i++)
		{
			ShadedFunction child = from.getOperand(i);
			has_white |= m_compareNonConnectives && !(child instanceof ShadedConnective);
			if ((m_compareNonConnectives && !(child instanceof ShadedConnective)) || (child instanceof ShadedConnective && child.getValue() == col))
			{
				addChild(children_from, child);
			}
		}
		for (int i = 0; i < to.getArity(); i++)
		{
			ShadedFunction child = to.getOperand(i);
			if ((m_compareNonConnectives && !(child instanceof ShadedConnective)) || ((child instanceof ShadedConnective) && colorSubsumes(col, (Color) child.getValue(), inverted)))
			{
				addChild(children_to, child);
			}
		}
		if (children_from.size() > children_to.size())
		{
			// Impossible: every element of children_1 must be associated to
			// a distinct element in children_2
			return false;
		}
		if (children_from.size() == 0)
		{
			// No sub-tree to map to the other tree: fine
			return true;
		}
		//System.out.println(children_from.size() + "->" + children_to.size());
		if (has_white)
		{
			if (children_from.size() != children_to.size())
			{
				return false;
			}
			for (int i = 0; i < children_from.size(); i++)
			{
				EquivalenceClass eq1 = children_from.get(i);
				EquivalenceClass eq2 = children_to.get(i);
				if (!eq1.m_representative.sameAs(eq2.m_representative))
				{
					return false;
				}
			}
			return true;
		}
		InjectionPicker picker = new InjectionPicker(children_from.size(), children_to.size());
		while (!picker.isDone())
		{
			if (Thread.currentThread().isInterrupted())
			{
				throw new LoopInterruptedException();
			}
			Integer[] mapping = picker.pick();
			boolean subsumed = true;
			for (int i = 0; i < mapping.length; i++)
			{
				EquivalenceClass child_1 = children_from.get(i);
				EquivalenceClass child_2 = children_to.get(mapping[i]);
				if ((child_1.m_cardinality > child_2.m_cardinality) || (!inverted && (!child_1.mapsTo(child_2))) || (inverted &&  !child_2.mapsTo(child_1)))
				{
					subsumed = false;
					//System.out.println("Forbid " + i + " " + mapping[i]);
					picker.forbid(i, mapping[i]);
					break;
				}
			}
			if (subsumed)
			{
				return true;
			}
			/*if (isBoolean(f1) || isBoolean(f2))
			{
				// The only mapping to consider for Boolean nodes is the one where
				// the n-th child of f1 is mapped to the n-th child of f2. If this
				// mapping does not work, we stop there.
				break;
			}*/
		}
		return false;
	}
	
	protected static boolean isBoolean(ShadedFunction f)
	{
		return f instanceof ShadedOr || f instanceof ShadedAnd || f instanceof ShadedNot;
	}
	
	protected static boolean colorSubsumes(Color c1, Color c2, boolean inverted)
	{
		if (!inverted)
		{
			return c1 != Color.GREEN || c2 == Color.GREEN;
		}
		else
		{
			return c2 != Color.GREEN || c1 == Color.GREEN;
		}
	}
	
	protected void addChild(List<EquivalenceClass> classes, ShadedFunction f)
	{
		for (EquivalenceClass eq : classes)
		{
			if (eq.inClass(f))
			{
				eq.increment();
				return;
			}
		}
		classes.add(new EquivalenceClass(f));
	}
	
	protected class EquivalenceClass
	{
		protected int m_cardinality;
		
		protected final ShadedFunction m_representative;
		
		public EquivalenceClass(ShadedFunction c)
		{
			super();
			m_cardinality = 1;
			m_representative = c;
		}
		
		public boolean inClass(ShadedFunction c)
		{
			return m_representative.sameAs(c);
		}
		
		@Override
		public String toString()
		{
			return m_cardinality + ":" + m_representative;
		}
		
		public void increment()
		{
			m_cardinality++;
		}
		
		public boolean mapsTo(EquivalenceClass c)
		{
			return inRelation(m_representative, c.m_representative);
		}
	}
	
	/**
	 * Exception used internally to signal that the container thread running
	 * the method has been interrupted.
	 */
	protected static class LoopInterruptedException extends InterruptedException
	{
		private static final long serialVersionUID = 1L;
	}
}
