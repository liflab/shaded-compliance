package ca.uqac.lif.cep.shaded;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import ca.uqac.lif.cep.shaded.DotRenderer.Algorithm;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class TreeRenderer
{
	public TreeRenderer()
	{
		super();
	}
	
	public static void toImage(ShadedFunction f, String filename)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		render(f, new PrintStream(baos));
		DotRenderer.toImage(Algorithm.DOT, baos.toString(), filename);
	}

	public static void render(ShadedFunction f, PrintStream ps)
	{
		ps.println("digraph G {");
		ps.println("nodesep=0.125");
		ps.println("ranksep=0.25;");
		ps.println("node [shape=\"rectangle\",height=0.3,width=0.3,fixedsize=\"true\",style=\"filled\"];");
		ps.println("edge [arrowhead=\"none\"];");
		renderRecursive(f, ps, -1, new NodeCounter());
		ps.println("}");
	}

	protected static void renderRecursive(ShadedFunction n, PrintStream ps, long parent, NodeCounter c)
	{
		long id = c.get();
		ps.print(id + " [label=<" + n.getSymbol());
		if (n instanceof Polarized)
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
