package tictactoe;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.LatticeGenerator;
import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.ShadedFunction;
import ca.uqac.lif.cep.shaded.ShadedGraph;
import ca.uqac.lif.cep.shaded.Subsumption;
import ca.uqac.lif.cep.shaded.abstraction.TreeAbstraction;
import ca.uqac.lif.cep.shaded.abstraction.TriggerAtDepth;
import ca.uqac.lif.cep.shaded.abstraction.TruncateRoot;
import ca.uqac.lif.cep.shaded.DotRenderer.Format;

public class SimpleLattice
{

	public static void main(String[] args)
	{
		//ShadedConnective phi = or(G (eq(fetch("a"), 0)), G (and (eq(fetch("a"), 1), eq(fetch("b"), 0))));
		ShadedConnective phi = or(G (eq(fetch("a"), 0)), G (eq(fetch("b"), 0)));
		TreeAbstraction abs = new TriggerAtDepth(3, new TruncateRoot());
		List<ShadedFunction> elements = new ArrayList<>();
		for (int a1 = 0; a1 <= 2; a1++)
		{
			for (int b1 = 0; b1 <= 2; b1++)
			{
				for (int a2 = 0; a2 <= 1; a2++)
				{
					for (int b2 = 0; b2 <= 1; b2++)
					{
						for (int a3 = 0; a3 <= 1; a3++)
						{
							for (int b3 = 0; b3 <= 1; b3++)
							{
								ShadedFunction conf = phi.duplicate();
								conf.update(map("a", a1, "b", b1));
								conf.update(map("a", a2, "b", b2));
								conf.update(map("a", a3, "b", b3));
								if (abs != null)
								{
									conf = abs.apply(conf);
								}
								elements.add(conf);
							}
						}
					}
				}
			}
		}
		System.out.println(elements.size());
		LatticeGenerator gen = new LatticeGenerator(new Subsumption(true));
		ShadedGraph g = gen.getLattice(elements);
		g.toImage("/tmp/simple-lattice.png", Format.PNG);
		gen.dumpTrees("/tmp/G1", elements);
	}
	
	public static Map<String,Object> map(Object ... objects)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < objects.length; i += 2)
		{
			map.put((String) objects[i], objects[i + 1]);
		}
		return map;
	}

}
