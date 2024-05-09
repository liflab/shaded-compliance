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
package ca.uqac.lif.cep.shaded.abstraction;

import ca.uqac.lif.cep.shaded.ShadedFunction;

/**
 * Composes multiple tree abstractions, by applying them successively on an
 * evaluation tree.
 * @author Sylvain Hallé
 */
public class Compose implements TreeAbstraction
{
	/**
	 * Creates a new instance of the composition of multiple abstractions.
	 * 
	 * @param abstractions
	 *          The abstractions to compose
	 * @return The instance
	 */
	public static Compose compose(TreeAbstraction... abstractions)
	{
		return new Compose(abstractions);
	}
	
	/**
	 * The abstractions to compose.
	 */
	protected final TreeAbstraction[] m_abstractions;

	/**
	 * Creates a new instance of the composition of multiple abstractions.
	 * @param abstractions The abstractions to compose
	 */
	public Compose(TreeAbstraction... abstractions)
	{
		super();
		m_abstractions = abstractions;
	}

	@Override
	public ShadedFunction apply(ShadedFunction f)
	{
		ShadedFunction out_f = f;
		for (TreeAbstraction a : m_abstractions)
		{
			out_f = a.apply(out_f);
		}
		return out_f;
	}
}
