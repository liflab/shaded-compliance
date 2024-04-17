package treecompliancelab;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedFetchPath.path;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;

import ca.uqac.lif.cep.shaded.ShadedConnective;

@SuppressWarnings("unused")
public class BeepStoreProperty
{
	public static final String ONCE_LOGIN = "Once login";

	public static ShadedConnective get(String name)
	{
		switch (name)
		{
			case ONCE_LOGIN:
				return onceLogin();
			default:
				return null;
		}
	}
	
	protected static ShadedConnective onceLogin()
	{
		return F(eq(path("Message/Action"), "Login"));
	}
}