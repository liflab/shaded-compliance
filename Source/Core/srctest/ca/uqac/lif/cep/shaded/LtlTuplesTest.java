package ca.uqac.lif.cep.shaded;

import org.junit.Test;

import static ca.uqac.lif.cep.shaded.ShadedG.G;

import java.util.HashMap;
import java.util.Map;

import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;

public class LtlTuplesTest
{
	public static TreeRenderer renderer = new TreeRenderer();
	
	@Test
	public void test1()
	{
		ShadedFunction phi = G(eq(new ShadedFetchAttribute("a"), 1));
		phi.update(map("a", 1));
		System.out.println(phi.getValue());
		phi.update(map("a", 1));
		System.out.println(phi.getValue());
		phi.update(map("a", 2));
		System.out.println(phi.getValue());
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
