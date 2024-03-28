package ca.uqac.lif.cep.shaded;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	public static ShadedGraph exploreLattice(List<ShadedConnective> elements)
	{
		ShadedGraph g = new ShadedGraph(elements);
		for (int i = 0; i < elements.size(); i++)
		{
			for (int j = 0; j < elements.size(); j++)
			{
				if (j == i)
				{
					continue;
				}
				if (TreeComparator.isSubsumed(elements.get(i), elements.get(j)))
				{
					g.addEdge(elements.get(i), elements.get(j));
				}
			}
		}
		return g;
	}
	
	public static ShadedGraph exploreLattice(ShadedConnective ... elements)
	{
		return exploreLattice(Arrays.asList(elements));
	}
	
	/* TODO:
	 * 1. Merge edges that are mutually subsumed
	 * 2. Calculate transitive closure
	 */
	
	
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
