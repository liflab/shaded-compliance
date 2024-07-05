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
package treecompliancelab.data.cvc;

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
import treecompliancelab.PropertyFactory;
import treecompliancelab.PropertyFactory.NamedProperty;

@SuppressWarnings("unused")
public class CvcProperty implements PropertyFactory
{
	public static final String MAX_DURATION = "Max duration";
	
	public static final String LIFECYCLE = "Procedure lifecycle";
	
	/**
	 * Gets the properties that this factory can create.
	 * @return An array of strings representing the names of the properties
	 */
	public String[] getProperties()
	{
		return new String[] { MAX_DURATION, LIFECYCLE };
	}
	
	@Override
	public NamedProperty get(String name)
	{
		switch (name)
		{
			case MAX_DURATION:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return MAX_DURATION;
					}

					@Override
					public ShadedConnective getObject()
					{
						return maxDuration();
					}
				};
			case LIFECYCLE:
				return new NamedProperty()
				{
					@Override
					public String getName()
					{
						return LIFECYCLE;
					}

					@Override
					public ShadedConnective getObject()
					{
						return procedureLifecycle();
					}
				};
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
