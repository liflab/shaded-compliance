package tictactoe;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.LatticeGenerator;
import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.ShadedGraph;
import ca.uqac.lif.cep.shaded.Subsumption;
import ca.uqac.lif.synthia.NoMoreElementException;
import ca.uqac.lif.synthia.enumerative.Bound;

public class GameLattice
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
		Bound<String> picker = new Bound<String>(new GridPicker(), 10000);
		List<ShadedConnective> elements = new ArrayList<>();
		while (!picker.isDone())
		{
			try
			{
				Map<String,Object> grid = CompareBoards1.grid(picker.pick());
				ShadedConnective conf = condition.duplicate().update(grid);
				elements.add(conf);
			}
			catch (NoMoreElementException e)
			{
				break;
			}
		}
		System.out.println(elements.size());
		LatticeGenerator gen = new LatticeGenerator(new Subsumption(false));
		ShadedGraph g = gen.getLattice(elements);
		g.toImage("/tmp/ttt1.png");
	}

}
