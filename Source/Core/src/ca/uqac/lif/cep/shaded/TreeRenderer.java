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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import ca.uqac.lif.cep.shaded.DotRenderer.Algorithm;
import ca.uqac.lif.cep.shaded.DotRenderer.Format;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

/**
 * Renders an evaluation tree as an image, using Graphviz in the background.
 * @author Sylvain Hallé
 */
public class TreeRenderer
{
	/**
	 * A flag determining if the polarity of a node is displayed in the tree.
	 */
	protected final boolean m_showPolarity;
	
	public TreeRenderer(boolean show_polarity)
	{
		super();
		m_showPolarity = show_polarity;
	}
	
	public void toImage(ShadedFunction f, String filename, Format format)
	{
		toImage(f, filename, format, null);
	}
	
	public void toImage(ShadedFunction f, String filename, Format format, String title)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		render(f, new PrintStream(baos), title);
		DotRenderer.toImage(Algorithm.DOT, baos.toString(), filename, format);
	}
	
	public void render(ShadedFunction f, PrintStream ps)
	{
		render(f, ps, null);
	}

	public void render(ShadedFunction f, PrintStream ps, String title)
	{
		ps.println("digraph G {");
		if (title != null)
		{
			ps.println("  labelloc=\"t\";");
			ps.println("  label=\"" + title + "\";");
		}
		ps.println("  nodesep=0.125");
		ps.println("  ranksep=0.25;");
		ps.println("  node [shape=\"rectangle\",height=0.3,width=0.3,fixedsize=\"true\",style=\"filled\"];");
		ps.println("  edge [arrowhead=\"none\"];");
		renderRecursive(f, ps, -1, new NodeCounter());
		ps.println("}");
	}

	protected void renderRecursive(ShadedFunction n, PrintStream ps, long parent, NodeCounter c)
	{
		long id = c.get();
		ps.print("  " + id + " [label=<" + n.getSymbol());
		if (m_showPolarity && n instanceof Polarized)
		{
			Polarized p = (Polarized) n;
			if (p.getPolarity() == Polarized.Polarity.NEGATIVE)
			{
				ps.print("<sup>-</sup>");
			}
			else
			{
				ps.print("<sup>+</sup>");
			}
		}
		ps.print(">,fillcolor=");
		Object o_val = n.getValue();
		if (o_val instanceof Color)
		{
			Color col = (Color) o_val;
			if (col == Color.GREEN)
			{
				ps.println("\"green\"];");
			}
			else if (col == Color.RED)
			{
				ps.println("\"red\"];");
			}
			else
			{
				ps.println("\"grey\"];");
			}
		}
		else
		{
			ps.println("\"white\"];");
		}
		if (parent >= 0)
		{
			ps.println(parent + " -> " + id + ";");
		}
		c.increment();
		for (int i = 0; i < n.getArity(); i++)
		{
			ShadedFunction child = n.getOperand(i);
			renderRecursive(child, ps, id, c);
		}
	}

	protected static class NodeCounter
	{
		protected long m_count = 0;

		public long get()
		{
			return m_count;
		}

		public long increment()
		{
			return ++m_count;
		}
	}
}
