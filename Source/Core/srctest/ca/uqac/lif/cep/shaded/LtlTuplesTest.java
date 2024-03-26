package ca.uqac.lif.cep.shaded;

import org.junit.Test;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedBinaryFunction.delta;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedNot.not;

import java.util.HashMap;
import java.util.Map;

public class LtlTuplesTest
{
	public static TreeRenderer renderer = new TreeRenderer();
	
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
		renderer.render(phi, System.out);
	}
	
	@Test
	public void test2()
	{
		ShadedFunction phi = and(
				leq(delta(fetch("a"), 1), 0),
				leq(delta(fetch("a"), 1), 1),
				leq(delta(fetch("a"), 1), 2));
		phi.update(map("a", 1.5));
		renderer.render(phi, System.out);
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
