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

import ca.uqac.lif.cep.shaded.abstraction.Identity;
import ca.uqac.lif.cep.shaded.abstraction.TreeAbstraction;
import ca.uqac.lif.cep.shaded.abstraction.TriggerAtDepth;
import ca.uqac.lif.cep.shaded.abstraction.TrimColor;
import ca.uqac.lif.cep.shaded.abstraction.TruncateRoot;
import ca.uqac.lif.labpal.Named;

/**
 * Factory for creating tree abstractions.
 * @author Sylvain Hallé
 */
public class TreeAbstractionFactory
{
	/**
	 * Name of the identity tree abstraction.
	 */
	public static final String IDENTITY = "Identity";
	
	/**
	 * Name of the tree abstraction that truncates the tree to depth 3.
	 */
	public static final String TRUNCATE_3 = "Truncate 3";
	
	/**
	 * Name of the tree abstraction that truncates the tree to depth 5.
	 */
	public static final String TRUNCATE_5 = "Truncate 5";
	
	/**
	 * Name of the tree abstraction that removes children of a node
	 * that have a different color.
	 */
	public static final String TRIM_COLOR = "Trim color";
	
	/**
	 * Gets the abstractions that this factory can create.
	 * @return An array of strings representing the names of the abstractions
	 */
	public String[] getAbstractions()
	{
		return new String[] { IDENTITY, TRUNCATE_3, TRUNCATE_5, TRIM_COLOR };
	}

	/**
	 * Gets a tree abstraction by its name.
	 * @param name The name of the tree abstraction
	 * @return The tree abstraction
	 */
	public NamedTreeAbstraction get(String name)
	{
		switch (name)
		{
			case IDENTITY:
				return new NamedTreeAbstraction()
				{
					@Override
					public String getName()
					{
						return IDENTITY;
					}
					
					@Override
					public TreeAbstraction getObject()
					{
						return new Identity();
					}
				};
				case TRUNCATE_3:
					return new NamedTreeAbstraction()
					{
						@Override
						public String getName()
						{
							return TRUNCATE_3;
						}

						@Override
						public TreeAbstraction getObject()
						{
							return new TriggerAtDepth(3, new TruncateRoot());
						}
					};
					case TRUNCATE_5:
						return new NamedTreeAbstraction()
						{
							@Override
							public String getName()
							{
								return TRUNCATE_5;
							}

							@Override
							public TreeAbstraction getObject()
							{
								return new TriggerAtDepth(5, new TruncateRoot());
							}
						};
					case TRIM_COLOR:
						return new NamedTreeAbstraction()
						{
							@Override
							public String getName()
							{
								return TRIM_COLOR;
							}

							@Override
							public TreeAbstraction getObject()
							{
								return new TrimColor();
							}
						};
		}
		return null;
	}

	/**
	 * A named tree abstraction.
	 */
	public static class NamedTreeAbstraction implements Named<TreeAbstraction>
	{
		@Override
		public String getName()
		{
			return null;
		}

		@Override
		public TreeAbstraction getObject()
		{
			return null;
		}
	}
}
