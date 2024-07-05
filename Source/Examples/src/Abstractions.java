import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedForAll.all;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedH.H;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedFetchPath.path;
import static ca.uqac.lif.cep.shaded.ShadedImplies.implies;
import static ca.uqac.lif.cep.shaded.ShadedNot.not;
import static ca.uqac.lif.cep.shaded.ShadedO.O;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;
import static ca.uqac.lif.cep.shaded.ShadedQuantifiedVariable.v;
import static ca.uqac.lif.cep.shaded.ShadedX.X;
import static ca.uqac.lif.cep.shaded.ShadedY.Y;

import ca.uqac.lif.cep.shaded.abstraction.TreeAbstraction;
import ca.uqac.lif.cep.shaded.abstraction.TrimColor;

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.cep.shaded.DotRenderer;
import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.ShadedFunction;
import ca.uqac.lif.cep.shaded.TreeRenderer;

@SuppressWarnings("unused")
public class Abstractions
{
	protected static final TreeRenderer s_renderer = new TreeRenderer(false);
	
	public static void main(String[] args)
	{
		ShadedConnective phi = and(
				H (
						implies(
								eq(fetch("action"), "N"),
								O (
										eq(fetch("action"), "B")
										)
								)
						),
				H (
						implies(
								eq(fetch("action"), "N"),
								O (
										eq(fetch("action"), "M")
										)
								)
						)
				);
		phi.update(getMap("action", "A"));
		phi.update(getMap("action", "B"));
		phi.update(getMap("action", "C"));
		phi.update(getMap("action", "M"));
		phi.update(getMap("action", "N"));
		phi.update(getMap("action", "O"));
		phi.update(getMap("action", "P"));
		TreeAbstraction tau = new TrimColor();
		ShadedFunction phi_prime = tau.apply(phi);
		s_renderer.toImage(phi_prime, "/tmp/grid1.png", DotRenderer.Format.PNG);
	}
	
	public static Map<String,Object> getMap(String attribute, Object value)
	{
		Map<String,Object> m = new HashMap<String,Object>();
		m.put(attribute, value);
		return m;
	}

}
