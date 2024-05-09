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

import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.ShadedConstant;
import ca.uqac.lif.cep.shaded.ShadedFalse;
import ca.uqac.lif.cep.shaded.ShadedFunction;
import ca.uqac.lif.cep.shaded.ShadedTrue;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

/**
 * Truncates the tree at the root level, i.e. removes all the children of the
 * root, and replaces the root itself by a constant which is either the
 * color of the original root, or {@code null} if the root is an uncolored
 * node.
 * @author Sylvain Hallé
 */
public class TruncateRoot implements TreeAbstraction
{
	public static TruncateRoot truncate()
	{
		return new TruncateRoot();
	}
	
	@Override
	public ShadedFunction apply(ShadedFunction f)
	{
		if (!(f instanceof ShadedConnective))
		{
			return new ShadedConstant(null);
		}
		ShadedConnective f_c = (ShadedConnective) f;
		if (f_c.getValue() == Color.GREEN)
		{
			return new ShadedTrue();
		}
		return new ShadedFalse();
	}
}
