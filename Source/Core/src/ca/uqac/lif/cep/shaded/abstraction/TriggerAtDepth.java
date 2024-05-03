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
	public static TriggerAtDepth atDepth(int depth, TreeAbstraction t)
	{
		return new TriggerAtDepth(depth, t);
	}
	
	protected final int m_depth;
	
	protected final TreeAbstraction m_t;
	
	public TriggerAtDepth(int depth, TreeAbstraction t)
	{
		super();
		m_depth = depth;
		m_t = t;
	}
	
	@Override
	public ShadedFunction abstractify(ShadedFunction f)
	{
		return abstractify(f, 0);
	}
	
	protected ShadedFunction abstractify(ShadedFunction f, int depth)
	{
		ShadedFunction f_dup = f.cloneNode();
		if (depth == m_depth)
		{
			for (int i = 0; i < f.getArity(); i++)
			{
				ShadedFunction f_op = f.getOperand(i);
				ShadedFunction f_op_abs = m_t.abstractify(f_op);
				f_dup.addOperand(f_op_abs);
			}
		}
		else
		{
			for (int i = 0; i < f.getArity(); i++)
			{
				ShadedFunction f_op = f.getOperand(i);
				ShadedFunction f_op_abs = abstractify(f_op);
				f_dup.addOperand(f_op_abs);
			}
		}
		return f_dup;
	}

}
