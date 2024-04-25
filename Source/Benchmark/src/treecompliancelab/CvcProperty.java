package treecompliancelab;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedForAll.all;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedFetchPath.path;
import static ca.uqac.lif.cep.shaded.ShadedImplies.implies;
import static ca.uqac.lif.cep.shaded.ShadedNot.not;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;
import static ca.uqac.lif.cep.shaded.ShadedQuantifiedVariable.v;
import static ca.uqac.lif.cep.shaded.ShadedX.X;

import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.ShadedBinaryFunction.ShadedAbsoluteDifference;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;

@SuppressWarnings("unused")
public class CvcProperty
{
	public static final String MAX_DURATION = "Max duration";
	
	public static final String LIFECYCLE = "Procedure lifecycle";
	
	public static ShadedConnective get(String name)
	{
		switch (name)
		{
			case MAX_DURATION:
				return maxDuration();
			case LIFECYCLE:
				return procedureLifecycle();
			default:
				return null;
		}
	}
	
	protected static ShadedConnective maxDuration()
	{
		return G (leq (new ShadedAbsoluteDifference(fetch("VIDEOEND"), fetch("VIDEOSTART")), 50));
	}
	
	protected static ShadedConnective procedureLifecycle()
	{
		return and (
				G (
				implies(
						eq (fetch("STAGE"), "Install Catheter"),
						X (eq (fetch("STAGE"), "Install Catheter"))
				)
		),
				G (
						implies(
								eq (fetch("STAGE"), "Ultrasound Preparation"),
								X (or (
										eq (fetch("STAGE"), "Ultrasound Preparation"),
										eq (fetch("STAGE"), "Locate Structures")
										)
								)
						)
				)
		);
	}
}
