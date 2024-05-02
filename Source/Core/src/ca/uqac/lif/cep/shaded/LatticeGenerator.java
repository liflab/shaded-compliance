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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.cep.shaded.DotRenderer.Format;

public class LatticeGenerator
{
	protected final TreeComparator m_relation;

	protected final Map<Integer,Integer> m_classes;

	public LatticeGenerator(TreeComparator relation)
	{
		super();
		m_relation = relation;
		m_classes = new HashMap<>();
	}

	public ShadedGraph getLattice(List<ShadedConnective> elements)
	{
		ShadedGraph g_unique = createLattice(elements);
		//System.out.println("Original");
		g_unique.printMatrix(System.out);
		ShadedGraph g_trans = g_unique.getTransitiveClosure();
		removeTransitiveEdges(g_unique, g_trans);
		//System.out.println("Edge removal");
		g_unique.printMatrix(System.out);
		return g_unique;
	}

	protected ShadedGraph createLattice(List<ShadedConnective> elements)
	{
		boolean[][] adj = new boolean[elements.size()][elements.size()];
		Set<Integer> to_remove = new HashSet<>();
		Map<Integer,Integer> multiplicity = new HashMap<>();
		Map<Integer,Set<Integer>> original = new HashMap<>();
		for (int i = 0; i < elements.size(); i++)
		{
			int mul = 1;
			if (to_remove.contains(i))
			{
				continue;
			}
			Set<Integer> equiv = new HashSet<>();
			equiv.add(i);
			ShadedFunction f1 = elements.get(i);
			for (int j = i + 1; j < elements.size(); j++)
			{
				/*if (to_remove.contains(j))
				{
					continue;
				}*/
				ShadedFunction f2 = elements.get(j);
				adj[i][j] = m_relation.inRelation(f1, f2);
				//System.out.println(i + "," + j + ":" + adj[i][j]);
				adj[j][i] = m_relation.inRelation(f2, f1);
				//System.out.println(j + "," + i + ":" + adj[j][i]);
				if (adj[i][j] && adj[j][i])
				{
					to_remove.add(j);
					equiv.add(j);
					mul++;
				}
			}
			original.put(i, equiv);
			multiplicity.put(i, mul);
		}
		List<ShadedConnective> new_elements = new ArrayList<>();
		{
			int i_cnt = 0;
			for (int i = 0; i < elements.size(); i++)
			{
				if (!to_remove.contains(i))
				{
					new_elements.add(elements.get(i));
					Set<Integer> equiv = original.get(i);
					for (int x : equiv)
					{
						m_classes.put(x, i_cnt);
					}
					i_cnt++;
				}
			}
		}
		for (int x = 0; x < adj.length; x++)
		{
			for (int y = 0; y < adj.length; y++)
				System.out.print(adj[x][y] ? "1 " : "0 ");
			System.out.println();
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
	
	public void dumpTrees(String prefix, List<ShadedConnective> elements)
	{
		TreeRenderer renderer = new TreeRenderer(false);
		for (int i = 0; i < elements.size(); i++)
		{
			int class_nb = m_classes.get(i);
			ShadedConnective c = elements.get(i);
			renderer.toImage(c, prefix + "_" + i + "_C" + class_nb + ".png", Format.PNG, i + " class " + class_nb);
		}
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
}
