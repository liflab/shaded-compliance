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
	protected final TreeComparator m_relation;
	
	public LatticeGenerator(TreeComparator relation)
	{
		super();
		m_relation = relation;
	}

	public ShadedGraph getLattice(List<ShadedConnective> elements)
	{
		ShadedGraph g_unique = createLattice(elements);
		ShadedGraph g_trans = g_unique.getTransitiveClosure();
		removeTransitiveEdges(g_unique, g_trans);
		return g_unique;
	}
	
	protected ShadedGraph createLattice(List<ShadedConnective> elements)
	{
		boolean[][] adj = new boolean[elements.size()][elements.size()];
		Set<Integer> to_remove = new HashSet<>();
		Map<Integer,Integer> multiplicity = new HashMap<>();
		for (int i = 0; i < elements.size(); i++)
		{
			int mul = 1;
			if (to_remove.contains(i))
			{
				continue;
			}
			ShadedFunction f1 = elements.get(i);
			for (int j = i + 1; j < elements.size(); j++)
			{
				if (to_remove.contains(j))
				{
					continue;
				}
				ShadedFunction f2 = elements.get(j);
				adj[i][j] = m_relation.inRelation(f1, f2);
				adj[j][i] = m_relation.inRelation(f2, f1);
				if (adj[i][j] && adj[j][i])
				{
					to_remove.add(j);
					mul++;
				}
			}
			multiplicity.put(i, mul);
		}
		List<ShadedConnective> new_elements = new ArrayList<>();
		for (int i = 0; i < elements.size(); i++)
		{
			if (!to_remove.contains(i))
			{
				new_elements.add(elements.get(i));
			}
		}
		ShadedGraph new_g = new ShadedGraph(new_elements);
		int i_cnt = 0;
		for (int i = 0; i < elements.size(); i++)
		{
			if (to_remove.contains(i))
			{
				continue;
			}
			new_g.m_multiplicity.put(i_cnt, multiplicity.get(i));
			int j_cnt = 0;
			for (int j = 0; j < elements.size(); j++)
			{
				if (to_remove.contains(j))
				{
					continue;
				}
				new_g.m_adjacency[i_cnt][j_cnt] = adj[i][j];
				j_cnt++;
			}
			i_cnt++;
		}
		return new_g;
	}
	
	protected ShadedGraph removeEquivalents(ShadedGraph g)
	{
		Map<Integer,Integer> multiplicity = new HashMap<>();
		List<ShadedConnective> new_elements = new ArrayList<>();
		int cnt = 0;
		new_elements.add(g.m_orderedElements.get(0));
		Set<Integer> to_remove = new HashSet<>();
		for (int i = 0; i < g.m_orderedElements.size(); i++)
		{
			if (to_remove.contains(i))
			{
				continue;
			}
			int mul = 1;
			ShadedFunction f1 = g.m_orderedElements.get(i);
			for (int j = i + 1; j < g.m_orderedElements.size(); j++)
			{
				ShadedFunction f2 = g.m_orderedElements.get(j);
				if (!to_remove.contains(j) && m_relation.inRelation(f1, f2) && m_relation.inRelation(f2, f1))
				{
					// f2 is equivalent to f1
					mul++;
					to_remove.add(j);
				}
			}
			multiplicity.put(cnt, mul);
			cnt++;
		}
		for (int i = 0; i < g.m_orderedElements.size(); i++)
		{
			if (!to_remove.contains(i))
			{
				new_elements.add(g.m_orderedElements.get(i));
			}
		}
		ShadedGraph new_g = new ShadedGraph(new_elements);
		System.out.println("Elements after duplicates removed: " + new_elements.size());
		new_g.m_multiplicity.putAll(multiplicity);
		return new_g;
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

	public ShadedGraph exploreLattice(List<ShadedConnective> elements)
	{
		ShadedGraph g = new ShadedGraph(elements);
		for (int i = 0; i < elements.size(); i++)
		{
			if (i % 10 == 0)
			{
				System.out.println("Processing element " + i + " of " + elements.size());
			}
			for (int j = 0; j < elements.size(); j++)
			{
				if (j == i)
				{
					continue;
				}
				if (m_relation.inRelation(elements.get(i), elements.get(j)))
				{
					g.addEdge(elements.get(i), elements.get(j));
				}
			}
		}
		return g;
	}
	
	public void exploreLattice(ShadedGraph g)
	{
		for (int i = 0; i < g.m_orderedElements.size(); i++)
		{
			if (i % 10 == 0)
			{
				System.out.println("Processing element " + i + " of " + g.m_orderedElements.size());
			}
			for (int j = 0; j < g.m_orderedElements.size(); j++)
			{
				if (j == i)
				{
					continue;
				}
				if (m_relation.inRelation(g.m_orderedElements.get(i), g.m_orderedElements.get(j)))
				{
					g.addEdge(g.m_orderedElements.get(i), g.m_orderedElements.get(j));
				}
			}
		}
	}

	public ShadedGraph exploreLattice(ShadedConnective ... elements)
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
