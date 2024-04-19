package treecompliancelab;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedFetchPath.path;
import static ca.uqac.lif.cep.shaded.ShadedNot.not;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;

import ca.uqac.lif.cep.shaded.ShadedConnective;

@SuppressWarnings("unused")
public class BeepStoreProperty
{
	public static final String ONCE_LOGIN = "Once Login";
	
	public static final String ONCE_ITEM_SEARCH = "Once ItemSearch";
	
	public static final String MAX_CARTS = "Maximum carts";

	public static ShadedConnective get(String name)
	{
		switch (name)
		{
			case ONCE_LOGIN:
				return onceLogin();
			case ONCE_ITEM_SEARCH:
				return onceItemSearch();
			case MAX_CARTS:
				return maximumCarts();
			default:
				return null;
		}
	}
	
	protected static ShadedConnective onceLogin()
	{
		return F(eq(path("Message/Action/text()"), "Login"));
	}
	
	protected static ShadedConnective onceItemSearch()
	{
		return F(eq(path("Message/Action/text()"), "ItemSearch"));
	}
	
	protected static ShadedConnective maximumCarts()
	{
		return G(
				or(
						not(eq(path("Message/Action/text()"), "CartCreateResponse")),
						leq(path("Message/CartId/text()"), 1)
				));
	}
}
