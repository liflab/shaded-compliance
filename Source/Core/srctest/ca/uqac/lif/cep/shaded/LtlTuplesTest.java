package ca.uqac.lif.cep.shaded;

import org.junit.Test;

import ca.uqac.lif.cep.shaded.DotRenderer.Format;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedH.H;
import static ca.uqac.lif.cep.shaded.ShadedBinaryFunction.delta;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedImplies.implies;
import static ca.uqac.lif.cep.shaded.ShadedNot.not;
import static ca.uqac.lif.cep.shaded.ShadedO.O;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class LtlTuplesTest
{	
	protected static final TreeRenderer s_renderer = new TreeRenderer(false);
	
	@Test
	public void test1()
	{
		ShadedFunction phi = not(G(eq(fetch("a"), 1)));
		phi.update(map("a", 1));
		System.out.println(phi.getValue());
		phi.update(map("a", 1));
		System.out.println(phi.getValue());
		phi.update(map("a", 2));
		System.out.println(phi.getValue());
		s_renderer.render(phi, System.out);
	}
	
	@Test
	public void test2()
	{
		ShadedFunction phi = and(
				leq(delta(fetch("a"), 1), 0),
				leq(delta(fetch("a"), 1), 1),
				leq(delta(fetch("a"), 1), 2));
		phi.update(map("a", 1.5));
		s_renderer.render(phi, System.out);
	}
	
	@Test
	public void test3()
	{
		ShadedFunction phi = H(implies(eq(fetch("a"), 1), O(eq(fetch("a"), 0))));
		phi.update(map("a", 2));
		System.out.println(phi.getValue());
		phi.update(map("a", 1));
		System.out.println(phi.getValue());
		phi.update(map("a", 0));
		System.out.println(phi.getValue());
		s_renderer.toImage(phi, "/tmp/H.png", Format.PNG);
		assertEquals(Color.RED, phi.getValue());
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
