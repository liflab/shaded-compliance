package ca.uqac.lif.cep.shaded;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import ca.uqac.lif.cep.shaded.DotRenderer.Algorithm;
import ca.uqac.lif.cep.shaded.DotRenderer.Format;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

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
