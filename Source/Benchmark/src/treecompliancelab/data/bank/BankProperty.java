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
package treecompliancelab.data.bank;

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

import ca.uqac.lif.cep.shaded.ShadedConnective;
import treecompliancelab.PropertyFactory;
import treecompliancelab.PropertyFactory.NamedProperty;

@SuppressWarnings("unused")
public class BankProperty implements PropertyFactory
{
	public static final String MANAGERS_VIP = "Managers for VIP";

	public static final String ARTICLE11_P1 = "Article 11 part 1";

	public static final String PAPER_C1 = "Condition 1";

	public static final String PAPER_C2 = "Condition 2";

	public static final String PAPER_C3 = "Condition 3";

	public static final String PAPER_C4 = "Condition 4";

	/**
	 * Gets the properties that this factory can create.
	 * @return An array of strings representing the names of the properties
	 */
	public String[] getProperties()
	{
		return new String[] { };
	}

	@Override
	public NamedProperty get(String name)
	{
		switch (name)
		{
			case PAPER_C1:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return PAPER_C1;
					}

					@Override
					public ShadedConnective getObject()
					{
						return paperCondition1();
					}
				};
			case PAPER_C2:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return PAPER_C2;
					}

					@Override
					public ShadedConnective getObject()
					{
						return paperCondition2();
					}
				};
			case PAPER_C3:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return PAPER_C3;
					}

					@Override
					public ShadedConnective getObject()
					{
						return paperCondition3();
					}
				};
			case PAPER_C4:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return PAPER_C4;
					}

					@Override
					public ShadedConnective getObject()
					{
						return paperCondition4();
					}
				};
			case MANAGERS_VIP:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return MANAGERS_VIP;
					}

					@Override
					public ShadedConnective getObject()
					{
						return managersVip();
					}
				};
			case ARTICLE11_P1:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return ARTICLE11_P1;
					}

					@Override
					public ShadedConnective getObject()
					{
						return article11part1();
					}
				};
			default:
				return null;
		}
	}

	protected static ShadedConnective managersVip()
	{
		return implies(
				eq(fetch("status"), "vip"),
				G (
						eq(fetch("level"), "senior")
						)
				);
	}

	protected static ShadedConnective article11part1()
	{
		return and(
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
	}

	protected static ShadedConnective paperCondition1()
	{
		return H(
				implies(
						eq(fetch("action"), "N"),
						and(
								O(eq(fetch("action"), "B")),
								O(eq(fetch("action"), "E"))
								)
						)
				);
	}

	protected static ShadedConnective paperCondition2()
	{
		return implies(
				eq(fetch("status"), "vip"),
				G (
						implies(
								eq(fetch("action"), "N"),
								leq(fetch("duration"), 2)
								)
						)
				);
	}

	protected static ShadedConnective paperCondition3()
	{
		return or(
				F (and(eq(fetch("level"), "manager"), eq(fetch("action"), "I"))),
				F (and(eq(fetch("level"), "manager"), eq(fetch("action"), "N"))),
				F (and(eq(fetch("level"), "manager"), eq(fetch("action"), "O")))
		);
	}

	protected static ShadedConnective paperCondition4()
	{
		return H(
				implies(
						eq(fetch("action"), "U"),
						Y (eq(fetch("action"), "T"))
						)
				);
	}
}
