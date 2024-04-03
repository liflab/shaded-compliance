package tictactoe;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.Subsumption;
import ca.uqac.lif.cep.shaded.TreeComparator;
import ca.uqac.lif.cep.shaded.TreeRenderer;

/**
 * Evaluates the winning "X" condition for tic-tac-toe on two different grid
 * configurations. Both configurations contain the same number of "X" symbols,
 * and both are winning configurations for "X". However, the first
 * configuration has two winning lines (both diagonals), while the second
 * configuration has only one winning line (one vertical line). As a
 * result, the evaluation tree for the first configuration subsumes the
 * evaluation tree for the second configuration.
 * <p>
 * One can observe that:
 * <ol>
 * <li>subsumption is independent on the actual lines that are winning, but
 * only on the number of such lines</li>
 * <li>the winning condition itself makes no explicit mention of counting</li>
 * </ol>
 */
public class CompareBoards1
{
	public static void main(String[] args)
	{
		ShadedConnective condition = or(
				and(eq(fetch("A1"), "X"), eq(fetch("A2"), "X"), eq(fetch("A3"), "X")),
				and(eq(fetch("B1"), "X"), eq(fetch("B2"), "X"), eq(fetch("B3"), "X")),
				and(eq(fetch("C1"), "X"), eq(fetch("C2"), "X"), eq(fetch("C3"), "X")),
				and(eq(fetch("A1"), "X"), eq(fetch("B1"), "X"), eq(fetch("C1"), "X")),
				and(eq(fetch("A2"), "X"), eq(fetch("B2"), "X"), eq(fetch("C2"), "X")),
				and(eq(fetch("A3"), "X"), eq(fetch("B3"), "X"), eq(fetch("C3"), "X")),
				and(eq(fetch("A1"), "X"), eq(fetch("B2"), "X"), eq(fetch("C3"), "X")),
				and(eq(fetch("A3"), "X"), eq(fetch("B2"), "X"), eq(fetch("C1"), "X")));
		ShadedConnective grid1 = condition.duplicate().update(grid("XOXOXOXOX"));
		ShadedConnective grid2 = condition.duplicate().update(grid("XOXXOOXXO"));
		TreeRenderer.toImage(grid1, "/tmp/grid1.png");
		TreeRenderer.toImage(grid2, "/tmp/grid2.png");
		Subsumption comp = new Subsumption(false);
		System.out.println(comp.inRelation(grid1, grid2));
		System.out.println(comp.inRelation(grid2, grid1));
	}
	
	/**
	 * Convert a string to a map representing a tic-tac-toe grid.
	 * @param s A string of 9 characters, X, O or any other character
	 * @return A map with keys A1, A2, A3, B1, B2, B3, C1, C2, C3
	 */
	public static Map<String,Object> grid(String s)
	{
		s = s.toUpperCase();
		s = s.replaceAll("[^XO ]", "");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("A1", s.substring(0, 1));
		map.put("A2", s.substring(1, 2));
		map.put("A3", s.substring(2, 3));
		map.put("B1", s.substring(3, 4));
		map.put("B2", s.substring(4, 5));
		map.put("B3", s.substring(5, 6));
		map.put("C1", s.substring(6, 7));
		map.put("C2", s.substring(7, 8));
		map.put("C3", s.substring(8, 9));
		return map;
	}

}
