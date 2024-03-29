package ca.uqac.lif.cep.shaded;

import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.LtlTuplesTest.map;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LatticeGeneratorTest
{
	@Test
	public void test1()
	{
		List<ShadedConnective> nodes = new ArrayList<>();
		ShadedFunction phi = G(eq(fetch("a"), 1));
		{
			ShadedConnective phi_prime = (ShadedConnective) phi.duplicate();
			nodes.add(phi_prime);
			phi_prime.update(map("a", 1));
		}
		{
			ShadedConnective phi_prime = (ShadedConnective) phi.duplicate();
			nodes.add(phi_prime);
			phi_prime.update(map("a", 1));
			phi_prime.update(map("a", 1));
		}
		{
			ShadedConnective phi_prime = (ShadedConnective) phi.duplicate();
			nodes.add(phi_prime);
			phi_prime.update(map("a", 0));
		}
		{
			ShadedConnective phi_prime = (ShadedConnective) phi.duplicate();
			nodes.add(phi_prime);
			phi_prime.update(map("a", 0));
			phi_prime.update(map("a", 1));
		}
		{
			ShadedConnective phi_prime = (ShadedConnective) phi.duplicate();
			nodes.add(phi_prime);
			phi_prime.update(map("a", 1));
			phi_prime.update(map("a", 0));
		}
		ShadedGraph g = LatticeGenerator.getLattice(nodes);
		g.render(System.out);
	}
}
