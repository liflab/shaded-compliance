package ca.uqac.lif.cep.shaded;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

public class LatticeGenerator
{
	public LatticeGenerator()
	{
		super();
	}
	
	public static Map<ShadedConnective,LatticeNode<ShadedConnective>> exploreLattice(List<ShadedConnective> elements)
	{
		return exploreLattice(elements.toArray(new ShadedConnective[elements.size()]));
	}
	
	public static Map<ShadedConnective,LatticeNode<ShadedConnective>> exploreLattice(ShadedConnective ... elements)
	{
		Map<ShadedConnective,LatticeNode<ShadedConnective>> nodes = new HashMap<>();
		for (int i = 0; i < elements.length; i++)
		{
			ShadedConnective element = elements[i];
			LatticeNode<ShadedConnective> node = new LatticeNode<>(element, i);
			nodes.put(element, node);
		}
		for (int i = 0; i < elements.length; i++)
		{
			LatticeNode<ShadedConnective> node = nodes.get(elements[i]);
			for (int j = 0; j < elements.length; j++)
			{
				if (j == i)
				{
					continue;
				}
				if (TreeComparator.isSubsumed(elements[i], elements[j]))
				{
					node.addEdge(nodes.get(elements[j]));
				}
			}
		}
		return nodes;
	}
	
	/* TODO:
	 * 1. Merge edges that are mutually subsumed
	 * 2. Calculate transitive closure
	 */
	
	public static void render(Map<ShadedConnective,LatticeNode<ShadedConnective>> lattice, PrintStream ps)
	{
		ps.println("digraph G {");
		ps.println("nodesep=0.125");
		ps.println("ranksep=0.25;");
		ps.println("node [shape=\"circle\",height=0.3,width=0.3,fixedsize=\"true\",style=\"filled\"];");
		for (LatticeNode<ShadedConnective> node : lattice.values())
		{
			ShadedConnective connective = node.getValue();
			Color color = connective.getValue();
			ps.print(node.getId() + " [label=<" + node.getId() + ">,fillcolor=" + (color == Color.GREEN ? "\"green\"" : "\"red\"") + "];\n");
			for (LatticeNode<ShadedConnective> edge : node.m_edges)
			{
				ps.println(node.getId() + " -> " + edge.getId() + ";");
			}
		}
		ps.println("}");
	}
	
	
	public static class LatticeNode<T>
	{
		protected final T m_value;
		
		protected final List<LatticeNode<T>> m_edges;
		
		protected final int m_id;
		
		public LatticeNode(T value, int id)
		{
			super();
			m_id = id;
			m_value = value;
			m_edges = new ArrayList<>();
		}
		
		public int getId()
		{
			return m_id;
		}
		
		public void addEdge(LatticeNode<T> node)
		{
			m_edges.add(node);
		}
		
		public T getValue()
		{
			return m_value;
		}
		
		public void deleteEdge(LatticeNode<T> node)
		{
			m_edges.remove(node);
		}
	}
}
