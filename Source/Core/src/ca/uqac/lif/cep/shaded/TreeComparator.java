package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class TreeComparator
{
	public static boolean isSubsumed(ShadedConnective f1, ShadedConnective f2)
	{
		Polarized.Polarity pol_1 = f1.getPolarity();
		Polarized.Polarity pol_2 = f2.getPolarity();
		if (pol_1 != pol_2)
		{
			// Comparing trees with different polarity is meaningless
			return false;
		}
		if (pol_1 == Polarized.Polarity.POSITIVE)
		{
			return isSubsumedPositive(f1, f2);
		}
		return isSubsumedNegative(f1, f2);
	}
	
	protected static boolean isSubsumedPositive(ShadedConnective f1, ShadedConnective f2)
	{
		if (f1.getValue() == Color.GREEN && f2.getValue() != Color.GREEN)
		{
			return false;
		}
		return isSubsumed(f1, f2, Color.GREEN);
	}
	
	protected static boolean isSubsumedNegative(ShadedConnective f1, ShadedConnective f2)
	{
		return isSubsumed(f2, f1, Color.RED);
	}
	
	protected static boolean isSubsumed(ShadedConnective f1, ShadedConnective f2, Color col)
	{
		List<ShadedConnective> children_1 = new ArrayList<ShadedConnective>();
		List<ShadedConnective> children_2 = new ArrayList<ShadedConnective>();
		for (int i = 0; i < f1.getArity(); i++)
		{
			ShadedFunction child = f1.getOperand(i);
			if (child instanceof ShadedConnective && child.getValue() == col)
			{
				children_1.add((ShadedConnective) child);
			}
		}
		for (int i = 0; i < f2.getArity(); i++)
		{
			ShadedFunction child = f2.getOperand(i);
			if (child instanceof ShadedConnective && child.getValue() == col)
			{
				children_2.add((ShadedConnective) child);
			}
		}
		if (children_1.size() > children_2.size())
		{
			// Impossible: every element of children_1 must be associated to
			// a distinct element in children_2
			return false;
		}
		if (children_1.size() == 0)
		{
			// No sub-tree to map to the other tree: fine
			return true;
		}
		InjectionPicker picker = new InjectionPicker(children_1.size(), children_2.size());
		while (!picker.isDone())
		{
			Integer[] mapping = picker.pick();
			boolean subsumed = true;
			for (int i = 0; i < mapping.length; i++)
			{
				ShadedConnective child_1 = children_1.get(i);
				ShadedConnective child_2 = children_2.get(mapping[i]);
				if (!isSubsumed(child_1, child_2))
				{
					subsumed = false;
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
}
