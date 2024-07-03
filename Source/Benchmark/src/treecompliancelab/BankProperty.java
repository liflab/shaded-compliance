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

import ca.uqac.lif.cep.shaded.ShadedConnective;
import treecompliancelab.PropertyFactory.NamedProperty;

@SuppressWarnings("unused")
public class BankProperty implements PropertyFactory
{
	public static final String MANAGERS_VIP = "Managers for VIP";
	
	public static final String ARTICLE11_P1 = "Article 11 part 1";
	
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
}
