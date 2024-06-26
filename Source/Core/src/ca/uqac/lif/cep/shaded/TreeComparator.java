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

public interface TreeComparator
{
	public boolean inRelation(ShadedFunction f1, ShadedFunction f2);
	
	/**
	 * Exception used internally to signal that the container thread running
	 * the method has been interrupted.
	 */
	public static class LoopInterruptedException extends InterruptedException
	{
		private static final long serialVersionUID = 1L;
	}
}
