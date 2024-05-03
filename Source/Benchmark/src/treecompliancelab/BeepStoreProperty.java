/*
    A tree-based process compliance library
    Copyright (C) 2024 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package treecompliancelab;

import static ca.uqac.lif.cep.shaded.ShadedAnd.and;
import static ca.uqac.lif.cep.shaded.ShadedComparison.eq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.geq;
import static ca.uqac.lif.cep.shaded.ShadedComparison.leq;
import static ca.uqac.lif.cep.shaded.ShadedEquivalence.iff;
import static ca.uqac.lif.cep.shaded.ShadedF.F;
import static ca.uqac.lif.cep.shaded.ShadedForAll.all;
import static ca.uqac.lif.cep.shaded.ShadedG.G;
import static ca.uqac.lif.cep.shaded.ShadedFetchAttribute.fetch;
import static ca.uqac.lif.cep.shaded.ShadedFetchPath.path;
import static ca.uqac.lif.cep.shaded.ShadedH.H;
import static ca.uqac.lif.cep.shaded.ShadedHasPath.hasPath;
import static ca.uqac.lif.cep.shaded.ShadedImplies.implies;
import static ca.uqac.lif.cep.shaded.ShadedNot.not;
import static ca.uqac.lif.cep.shaded.ShadedO.O;
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
	
	public static final String PAGE_IF_RESULTS = "Page if results";
	
	public static final String HAS_KEY = "Has key";
	
	public static final String ONCE_ITEM_SEARCH = "Once ItemSearch";
	
	public static final String MAX_CARTS = "Maximum carts";
	
	public static final String NO_CART_BEFORE_LOGIN = "No cart before login";
	
	public static final String NO_DUPLICATE_ITEM = "No duplicate item";
	
	public static final String NO_LOGIN_TWICE = "No login twice";
	
	public static String[] getProperties()
	{
		return new String[] { HAS_KEY, MAX_CARTS, NO_CART_BEFORE_LOGIN,
				NO_DUPLICATE_ITEM, NO_LOGIN_TWICE, ONCE_LOGIN,
				ONCE_ITEM_SEARCH, PAGE_IF_RESULTS, PAGE_INTERVAL };
	}

	public static ShadedConnective get(String name)
	{
		switch (name)
		{
			case HAS_KEY:
				return hasKey();
			case MAX_CARTS:
				return maximumCarts();
			case NO_CART_BEFORE_LOGIN:
				return noCartOpBeforeLogin();
			case NO_DUPLICATE_ITEM:
				return noDuplicateItems();
			case NO_LOGIN_TWICE:
				return noLoginTwice();
			case ONCE_LOGIN:
				return onceLogin();
			case ONCE_ITEM_SEARCH:
				return onceItemSearch();
			case PAGE_IF_RESULTS:
				return pageIfResults();
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
	 * Generates the condition stipulating that every message has a SessionKey.
	 * This corresponds to property P1 of the paper referenced above.
	 * @return The property
	 */
	protected static ShadedConnective hasKey()
	{
		return G (hasPath("Message/SessionKey"));
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
									geq (path("Message/Page/text()"), 1)
								)
				));
	}
	
	/**
	 * Generates the condition stipulating that in the ItemSearch message, the
	 * element Page is mandatory if the element Results is present; otherwise,
	 * it is forbidden. This corresponds to property P4 of the paper referenced
	 * above.
	 * @return The property
	 */
	protected static ShadedConnective pageIfResults()
	{
		return G (implies(eq(path("Message/Action/text()"), "ItemSearch"),
				iff(hasPath("Message/Results"), hasPath("Message/Page"))));
	}
	
	/**
	 * Generates the condition stipulating that the Login message must not be
	 * reset if a LoginResponse is observed.
	 * This corresponds to property P5 of the paper referenced above.
	 * @return The property
	 */
	protected static ShadedConnective noLoginTwice()
	{
		return G (implies (
				eq(path("Message/Action/text()"), "LoginResponse"),
				X (G (not(eq(path("Message/Action/text()"), "Login"))))));
	}
	
	protected static ShadedConnective noCartOpBeforeLogin()
	{
		return implies (
				eq(path("Message/Action/text()"), "CartCreate"),
				(O (eq(path("Message/Action/text()"), "ItemSearch"))));
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
