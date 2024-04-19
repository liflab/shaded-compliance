package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
import java.util.Arrays;
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
			return isSubsumed(c1, c2, pol_1);
		}
		if (m_compareNonConnectives)
		{
			return f1.sameAs(f2);
		}
		return false;
	}

	protected boolean isSubsumed(ShadedFunction f1, ShadedFunction f2, Polarity pol)
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

	protected boolean hasMapping(ShadedFunction f1, ShadedFunction f2, Color col, boolean inverted)
	{
		List<ShadedFunction> children_from = new ArrayList<>();
		List<ShadedFunction> children_to = new ArrayList<>();
		ShadedFunction from = f1, to = f2;
		boolean has_white = false;
		for (int i = 0; i < from.getArity(); i++)
		{
			ShadedFunction child = from.getOperand(i);
			has_white |= m_compareNonConnectives && !(child instanceof ShadedConnective);
			if ((m_compareNonConnectives && !(child instanceof ShadedConnective)) || (child instanceof ShadedConnective && child.getValue() == col))
			{
				children_from.add(child);
			}
		}
		for (int i = 0; i < to.getArity(); i++)
		{
			ShadedFunction child = to.getOperand(i);
			if ((m_compareNonConnectives && !(child instanceof ShadedConnective)) || ((child instanceof ShadedConnective) && colorSubsumes(col, (Color) child.getValue(), inverted)))
			{
				children_to.add(child);
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
		System.out.println(children_from.size() + "->" + children_to.size());
		if (has_white)
		{
			if (children_from.size() != children_to.size())
			{
				return false;
			}
			for (int i = 0; i < children_from.size(); i++)
			{
				if (!children_from.get(i).sameAs(children_to.get(i)))
				{
					return false;
				}
			}
			return true;
		}
		NewerInjectionPicker picker = new NewerInjectionPicker(children_from.size(), children_to.size());
		while (!picker.isDone())
		{
			Integer[] mapping = picker.pick();
			boolean subsumed = true;
			System.out.println(Arrays.toString(mapping));
			for (int i = 0; i < mapping.length - 1; i++)
			{
				ShadedFunction child_1 = children_from.get(i);
				ShadedFunction child_2 = children_to.get(mapping[i]);
				if ((!inverted && !inRelation(child_1, child_2)) || (inverted && !inRelation(child_2, child_1)))
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
		}
		return false;
	}
	
	protected static boolean colorSubsumes(Color c1, Color c2, boolean inverted)
	{
		if (!inverted)
		{
			return c1 != Color.GREEN || c2 != Color.GREEN;
		}
		else
		{
			return c2 != Color.GREEN || c1 != Color.GREEN;
		}
	}
}
