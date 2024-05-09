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

import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.labpal.Named;

/**
 * Factory for creating properties.
 * @author Sylvain Hallé
 */
public interface PropertyFactory
{
	/**
	 * Gets a property by its name.
	 * 
	 * @param name
	 *          The name of the property
	 * @return The property
	 */
	public Named<ShadedConnective> get(String name);
	
	/**
	 * A named property.
	 */
	public static class NamedProperty implements Named<ShadedConnective>
	{
		@Override
		public String getName()
		{
			return null;
		}

		@Override
		public ShadedConnective getObject()
		{
			return null;
		}
	}
}
