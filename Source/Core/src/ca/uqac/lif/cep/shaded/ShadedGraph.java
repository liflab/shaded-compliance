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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.DotRenderer.Algorithm;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;

/**
 * A graph representing the partial order between a set of shaded nodes.
 * 
 * @author Sylvain Hallé
 */
public class ShadedGraph
{
	protected List<ShadedFunction> m_orderedElements;

	protected Map<ShadedFunction,Integer> m_toId;

	protected Map<Integer,ShadedFunction> m_fromId;

	protected Map<Integer,Integer> m_multiplicity;

	protected boolean[][] m_adjacency;

	public ShadedGraph(List<ShadedFunction> elements)
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
	
	public void printMatrix(PrintStream ps)
	{
		for (int i = 0; i < m_adjacency.length; i++)
		{
			for (int j = 0; j < m_adjacency[i].length; j++)
			{
				if (j > 0)
					ps.print(" ");
				ps.print(m_adjacency[i][j] ? "1" : "0");
			}
			ps.println();
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
	public void addEdge(ShadedFunction from, ShadedFunction to)
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
	
	public void toImage(String filename, DotRenderer.Format format)
	{
		toImage(filename, format, null);
	}

	public void toImage(String filename, DotRenderer.Format format, String title)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		toDot(new PrintStream(baos), title);
		DotRenderer.toImage(Algorithm.DOT, baos.toString(), filename, format);
	}

	public void dumpNodes(String prefix)
	{
		TreeRenderer tr = new TreeRenderer(false);
		for (int i = 0; i < m_orderedElements.size(); i++)
		{
			ShadedFunction s = m_orderedElements.get(i);
			tr.toImage(s, prefix + i + ".png", DotRenderer.Format.PNG);
		}
	}

	public void toDot(PrintStream ps, String title)
	{
		ps.println("digraph G {");
		ps.println("  rankdir=BT;");
		if (title != null)
		{
			ps.println("  labelloc=\"t\";");
			ps.println("  label=\"" + title + "\";");
		}
		ps.println("  edge [dir=none];");
		ps.println("  splines=false;");
		ps.println("  node [shape=\"circle\",height=0.3,width=0.3,fixedsize=\"true\",style=\"filled\"];");
		for (ShadedFunction connective : m_orderedElements)
		{
			Color color = connective instanceof ShadedConnective ? ((ShadedConnective) connective).getValue() : null;
			int id = m_toId.get(connective);
			int multiplicity = m_multiplicity.get(id);
			ps.print("  " + id + " [label=<" + id + "\u00d7" + multiplicity + ">,fillcolor=" + (color == Color.GREEN ? "\"green\"" : "\"red\"") + "];\n");
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
