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
package ca.uqac.lif.cep.shaded;

public abstract class ShadedLtlOperator extends ShadedNaryConnective
{
	protected final ShadedConnective m_phi;
	
	public ShadedLtlOperator(ShadedConnective phi)
	{
		super();
		m_phi = phi;
	}
	
	@Override
	protected void copyInto(ShadedNaryConnective other, boolean with_state)
	{
		other.m_polarity = m_polarity;
		if (with_state)
		{
			other.m_color = m_color;
			for (ShadedConnective op : m_operands)
			{
				other.m_operands.add(op.duplicate(with_state));
			}
		}
	}
}
