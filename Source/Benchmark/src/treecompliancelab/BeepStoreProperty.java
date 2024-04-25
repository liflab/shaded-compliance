package treecompliancelab;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.geq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedForAll.all;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedFetchPath.path;
import static ca.uqac.lif.cep.shaded.ShadedNot.not;
import static ca.uqac.lif.cep.shaded.ShadedOr.implies;
import static ca.uqac.lif.cep.shaded.ShadedOr.or;
import static ca.uqac.lif.cep.shaded.ShadedQuantifiedVariable.v;
import static ca.uqac.lif.cep.shaded.ShadedX.X;

import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.xml.XPathExpression.XPathParseException;

/**
 * Part of the properties listed here are taken from:
 * <blockquote>
 * S. Hallé, R. Villemaire. (2012). Constraint-Based Invocation of Stateful Web
 * Services: The Beep Store (Case Study). In PESOS@ICSE 2012: 61-62.
 * </blockquote>
 */
@SuppressWarnings("unused")
public class BeepStoreProperty
{
	public static final String ONCE_LOGIN = "Once Login";
	
	public static final String PAGE_INTERVAL = "Page interval";
	
	public static final String ONCE_ITEM_SEARCH = "Once ItemSearch";
	
	public static final String MAX_CARTS = "Maximum carts";
	
	public static final String NO_DUPLICATE_ITEM = "No duplicate item";

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
			case NO_DUPLICATE_ITEM:
				return noDuplicateItems();
			case PAGE_INTERVAL:
				return pageInterval();
			default:
				return null;
		}
	}
	
	protected static ShadedConnective onceLogin()
	{
		return F (eq (path("Message/Action/text()"), "Login"));
	}
	
	protected static ShadedConnective onceItemSearch()
	{
		return F (eq (path("Message/Action/text()"), "ItemSearch"));
	}
	
	/**
	 * Generates the condition stipulating that in the ItemSearch message, the
	 * element Page must be an integer between 1 and 20. This corresponds to
	 * property P3 of the paper referenced above.
	 * @return The property
	 */
	protected static ShadedConnective pageInterval()
	{
		return G (
				implies (
						eq (path("Message/Action/text()"), "ItemSearch"),
						and (
									leq (path("Message/Page/text()"), 20),
									geq (path("Message/PageId/text()"), 1)
								)
				));
	}
	
	protected static ShadedConnective maximumCarts()
	{
		return G (
				implies (
						eq (path("Message/Action/text()"), "CartCreateResponse"),
						leq (path("Message/CartId/text()"), 1)
				));
	}
	
	protected static ShadedConnective noDuplicateItems()
	{
		try
		{
			return G(
					implies(
							eq (path("Message/Action/text()"), "CartAdd"),
							all ("i1", "Message/Items/Item/ItemId",
									X (
											G (
													implies(
															eq (path("Message/Action/text()"), "CartAdd"),
															all ("i2", "Message/Items/Item/ItemId",
																	not (eq (v("i1"), v("i2")))
															)
													)
											)
									)
							)
					)
			);
		}
		catch (XPathParseException e)
		{
			return null;
		}
	}
}
