package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LatticeGenerator
{
	public LatticeGenerator()
	{
		super();
	}

	public static ShadedGraph getLattice(List<ShadedConnective> elements)
	{
		ShadedGraph g = exploreLattice(elements);
		ShadedGraph g_merged = mergeEdges(g);
		ShadedGraph g_trans = g_merged.getTransitiveClosure();
		removeTransitiveEdges(g_merged, g_trans);
		return g_merged;
	}

	public static void removeTransitiveEdges(ShadedGraph g, ShadedGraph g_trans)
	{
		boolean[][] graph = g.m_adjacency;
		boolean[][] transitiveClosure = g_trans.m_adjacency;
		int n = graph.length; // Number of vertices in the graph
		// Remove transitive edges
		for (int i = 0; i < n; i++) 
		{
			for (int j = 0; j < n; j++) 
			{
				if (g.m_adjacency[i][j])
				{ // If there's a direct edge from i to j
					for (int k = 0; k < n; k++)
					{
						// If there's another path from i to j through k
						if (k != i && k != j && transitiveClosure[i][k] && transitiveClosure[k][j]) 
						{
							g.m_adjacency[i][j] = false; // Remove the direct edge from i to j
							break; // No need to check further once an alternate path is found
						}
					}
				}
			}
		}
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
	
	public static ShadedGraph mergeEdges(ShadedGraph g)
	{
		Set<Integer> redundant_edges = new HashSet<>();
		Map<Integer,Integer> multiplicity = new HashMap<>();
		for (int i = 0; i < g.size(); i++)
		{
			int mul = 1;
			for (int j = i + 1; j < g.size(); j++)
			{
				if (g.m_adjacency[i][j] && g.m_adjacency[j][i])
				{
					redundant_edges.add(j);
					mul++;
				}
			}
			multiplicity.put(i, mul);
		}
		List<ShadedConnective> new_elems = new ArrayList<>();
		for (int i = 0; i < g.size(); i++)
		{
			if (!redundant_edges.contains(i))
			{
				new_elems.add(g.m_fromId.get(i));
			}
		}
		ShadedGraph r_g = new ShadedGraph(new_elems);
		int i_cnt = 0;
		for (int i = 0; i < g.size(); i++)
		{
			if (redundant_edges.contains(i))
			{
				continue;
			}
			int j_cnt = 0;
			for (int j = 0; j < g.size(); j++)
			{
				if (redundant_edges.contains(j))
				{
					continue;
				}
				r_g.m_adjacency[i_cnt][j_cnt] = g.m_adjacency[i][j];
				r_g.m_multiplicity.put(i_cnt, multiplicity.get(i));
				j_cnt++;
			}
			i_cnt++;
		}
		return r_g;
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
