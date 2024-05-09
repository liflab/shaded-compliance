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
 * Applies another tree abstraction only for nodes situated at a specific
 * depth. For all other nodes, the tree is left unchanged.
 * @author Sylvain Hallé
 */
public class TriggerAtDepth implements TreeAbstraction
{
	/**
	 * Creates a new instance of this abstraction
	 * 
	 * @param depth
	 *          The depth at which the abstraction should be applied
	 * @param t
	 *          The abstraction to apply
	 * @return The new abstraction
	 */
	public static TriggerAtDepth atDepth(int depth, TreeAbstraction t)
	{
		return new TriggerAtDepth(depth, t);
	}
	
	/**
	 * The depth at which the abstraction should be applied.
	 */
	protected final int m_depth;
	
	/**
	 * The abstraction to apply.
	 */
	protected final TreeAbstraction m_t;
	
	public TriggerAtDepth(int depth, TreeAbstraction t)
	{
		super();
		m_depth = depth;
		m_t = t;
	}
	
	@Override
	public ShadedFunction apply(ShadedFunction f)
	{
		return apply(f, 2);
	}
	
	protected ShadedFunction apply(ShadedFunction f, int depth)
	{
		ShadedFunction f_dup = f.cloneNode();
		if (depth == m_depth)
		{
			for (int i = 0; i < f.getArity(); i++)
			{
				ShadedFunction f_op = f.getOperand(i);
				ShadedFunction f_op_abs = m_t.apply(f_op);
				f_dup.addOperand(f_op_abs);
			}
		}
		else
		{
			for (int i = 0; i < f.getArity(); i++)
			{
				ShadedFunction f_op = f.getOperand(i);
				ShadedFunction f_op_abs = apply(f_op, depth + 1);
				f_dup.addOperand(f_op_abs);
			}
		}
		return f_dup;
	}

}
