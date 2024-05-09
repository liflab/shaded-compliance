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
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;
import ca.uqac.lif.cep.shaded.ShadedFunction;

/**
 * Trims a tree by removing all children that do not have the same color as
 * the root.
 * @author Sylvain Hallé
 */
public class TrimColor implements TreeAbstraction
{
	/**
	 * Creates a new instance of this abstraction
	 * 
	 * @return The new abstraction
	 */
	public static TrimColor trimColor()
	{
		return new TrimColor();
	}
	
	@Override
	public ShadedFunction apply(ShadedFunction f)
	{
		ShadedFunction clone = f.cloneNode();
		if (clone instanceof ShadedConnective)
		{
			Color target_color = ((ShadedConnective) clone).getValue();
			for (int i = 0; i < f.getArity(); i++)
			{
				ShadedFunction f_op = f.getOperand(i);
				if (f_op instanceof ShadedConnective)
				{
					Color op_color = ((ShadedConnective) f_op).getValue();
					if (op_color == target_color)
					{
						clone.addOperand(f_op.duplicate(true));
					}
				}
				else
				{
					clone.addOperand(f_op.duplicate(true));
				}
			}
		}
		return clone;
	}

}
