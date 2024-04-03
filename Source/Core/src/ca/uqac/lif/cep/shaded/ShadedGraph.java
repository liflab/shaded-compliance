package ca.uqac.lif.cep.shaded;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

/**
 * A graph representing the partial order between a set of shaded nodes.
 * 
 * @author Sylvain Hallé
 */
public class ShadedGraph
{
	protected List<ShadedConnective> m_orderedElements;

	protected Map<ShadedConnective,Integer> m_toId;

	protected Map<Integer,ShadedConnective> m_fromId;
	
	protected Map<Integer,Integer> m_multiplicity;

	protected boolean[][] m_adjacency;

	public ShadedGraph(List<ShadedConnective> elements)
	{
		super();
		m_orderedElements = elements;
		m_toId = new HashMap<>(elements.size());
		m_fromId = new HashMap<>(elements.size());
		m_adjacency = new boolean[elements.size()][elements.size()];
		m_multiplicity = new HashMap<>(elements.size());
		for (int i = 0; i < elements.size(); i++)
		{
			m_toId.put(elements.get(i), i);
			m_fromId.put(i, elements.get(i));
			m_multiplicity.put(i, 1);
		}
	}
	
	public int size()
	{
		return m_adjacency.length;
	}

	/**
	 * Adds an edge from one shaded connective to another in the graph.
	 * @param from The source connective
	 * @param to The destination connective
	 */
	public void addEdge(ShadedConnective from, ShadedConnective to)
	{
		Integer from_id = m_toId.get(from);
		Integer to_id = m_toId.get(to);
		if (from_id == null || to_id == null)
		{
			return;
		}
		m_adjacency[from_id][to_id] = true;
	}

	/**
	 * Returns a new shaded graph that represents the transitive closure of this graph.
	 * It implements the Floyd-Warshall algorithm, as described in this
	 * <a href="https://www.geeksforgeeks.org/transitive-closure-of-a-graph/">post</a>.
	 * 
	 * @return The transitive closure of this graph
	 */
	public ShadedGraph getTransitiveClosure()
	{
		ShadedGraph closure = new ShadedGraph(m_orderedElements);
		for (int i = 0; i < m_adjacency.length; i++)
		{
			for (int j = 0; j < m_adjacency.length; j++)
			{
				if (m_adjacency[i][j])
				{
					closure.addEdge(m_fromId.get(i), m_fromId.get(j));
				}
			}
		}
		for (int i = 0; i < m_adjacency.length; i++)
		{
			for (int j = 0; j < m_adjacency.length; j++)
			{
				for (int k = 0; k < m_adjacency.length; k++)
				{
					if (closure.m_adjacency[i][j] && closure.m_adjacency[j][k])
					{
						closure.m_adjacency[i][k] = true;
					}
				}
			}
		}
		return closure;
	}

	public void render(PrintStream ps)
	{
		ps.println("digraph G {");
		ps.println("  nodesep=0.125");
		ps.println("  ranksep=0.25;");
		ps.println("  node [shape=\"circle\",height=0.3,width=0.3,fixedsize=\"true\",style=\"filled\"];");
		for (ShadedConnective connective : m_orderedElements)
		{
			Color color = connective.getValue();
			int id = m_toId.get(connective);
			int multiplicity = m_multiplicity.get(id);
			ps.print("  " + id + " [label=<" + multiplicity + ">,fillcolor=" + (color == Color.GREEN ? "\"green\"" : "\"red\"") + "];\n");
		}
		for (int i = 0; i < m_adjacency.length; i++)
		{
			for (int j = 0; j < m_adjacency.length; j++)
			{
				if (m_adjacency[i][j])
				{
					ps.println("  " + i + " -> " + j + ";");
				}
			}
		}
		ps.println("}");
	}
}
