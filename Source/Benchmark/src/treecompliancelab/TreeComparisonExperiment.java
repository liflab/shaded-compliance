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
package treecompliancelab;

import java.util.List;

import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.TreeComparator;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.labpal.experiment.Experiment;
import ca.uqac.lif.labpal.util.Stopwatch;

public class TreeComparisonExperiment<T> extends Experiment
{
	/**
	 * Name of parameter "Average time".
	 */
	public static final String AVG_TIME = "Average time";
	
	/**
	 * Name of parameter "Condition".
	 */
	public static final String CONDITION = "Condition";

	/**
	 * Name of parameter "Minimum log size".
	 */
	public static final String LOG_SIZE_MIN = "Log size (min)";
	
	/**
	 * Name of parameter "Maximum log size".
	 */
	public static final String LOG_SIZE_MAX = "Log size (max)";
	
	/**
	 * Name of parameter "Minimum tree size".
	 */
	public static final String TREE_SIZE_MIN = "Tree size (min)";
	
	/**
	 * Name of parameter "Maximum tree size".
	 */
	public static final String TREE_SIZE_MAX = "Tree size (max)";
	
	/**
	 * Name of parameter "Number of pairs".
	 */
	public static final String NUM_PAIRS = "Number of pairs";
	
	/**
	 * Name of parameter "Subsumed".
	 */
	public static final String SUBSUMED = "Subsumed";
	
	/**
	 * Name of parameter "Scenario".
	 */
	public static final String SCENARIO = "Scenario";
	
	/**
	 * Name of parameter "Time".
	 */
	public static final String TIME = "Time";

	/**
	 * Name of parameter "Tree size".
	 */
	public static final String TREE_SIZE = "Tree size";
	
	/**
	 * A source of log pairs. This source will be enumerated, and each produced
	 * pair will be submitted to the tree comparator.
	 */
	protected final LogPairPicker<T> m_picker;

	/**
	 * The object used to compare evaluation trees.
	 */
	protected final TreeComparator m_comparator;

	/**
	 * The condition to evaluate on each log.
	 */
	protected final ShadedConnective m_condition;

	/**
	 * Creates a new tree comparison experiment.
	 * @param scenario
	 * @param condition_name
	 * @param condition
	 * @param comparator
	 * @param picker
	 */
	public TreeComparisonExperiment(String scenario, String condition_name, ShadedConnective condition, TreeComparator comparator, LogPairPicker<T> picker)
	{
		super();
		describe(SCENARIO, "The scenario the condition and event traces come from");
		describe(CONDITION, "The condition evaluated on each event trace");
		describe(LOG_SIZE_MIN, "The size of the smallest log");
		describe(LOG_SIZE_MAX, "The size of the largest log");
		describe(TREE_SIZE_MIN, "The size of the smallest tree");
		describe(TREE_SIZE_MAX, "The size of the largest tree");
		describe(AVG_TIME, "The average time taken to compare the evaluation trees of a log pair (in ms)");
		describe(SUBSUMED, "The number of pairs of logs for which the subsumption relation holds");
		describe(NUM_PAIRS, "The number of pairs of logs considered");
		describe(TIME, "The time taken to evaluate each log pair (in ms)");
		describe(TREE_SIZE, "The cumulative size of each pair of trees (in ms)");
		writeInput(SCENARIO, scenario);
		writeInput(CONDITION, condition_name);
		writeOutput(TIME, new JsonList());
		writeOutput(TREE_SIZE, new JsonList());
		m_condition = condition;
		m_comparator = comparator;
		m_picker = picker;
	}

	@Override
	public void execute()
	{
		Stopwatch sw = new Stopwatch();
		int pair_nb = 0;
		int log_size_min = Integer.MAX_VALUE, log_size_max = 0;
		int tree_size_min = Integer.MAX_VALUE, tree_size_max = 0;
		int subsumed = 0;
		long total_time = 0;
		int total_pairs = 0;
		JsonList l_time = (JsonList) read(TIME);
		JsonList l_size = (JsonList) read(TREE_SIZE);
		while (!m_picker.isDone())
		{
			List<T>[] pair = m_picker.pick();
			ShadedConnective tree1 = m_condition.duplicate();
			log_size_min = Math.min(log_size_min, pair[0].size());
			log_size_min = Math.min(log_size_min, pair[1].size());
			log_size_max = Math.max(log_size_max, pair[0].size());
			log_size_max = Math.max(log_size_max, pair[1].size());
			sw.start();
			for (T e : pair[0])
			{
				tree1.update(e);
			}
			ShadedConnective tree2 = m_condition.duplicate();
			for (T e : pair[1])
			{
				tree2.update(e);
			}
			if (total_pairs == 0)
			{
				// We count pairs here, since the picker generates the pairs
				// only on its first call to pick
				total_pairs = m_picker.countPairs();
			}
			setProgression((float) pair_nb / (float) total_pairs);
			tree1.trim();
			tree2.trim();
			/*if (tree1.getValue() == Color.GREEN && tree2.getValue() == Color.RED)
			{
				// No point in comparing them, the result is instantaneous
				continue;
			}*/		
			//TreeRenderer tr = new TreeRenderer(false);
			//tr.toImage(tree1, "/tmp/" + pair_nb + "-1.png", Format.PNG);
			//tr.toImage(tree1, "/tmp/" + pair_nb + "-2.png", Format.PNG);
			boolean b = m_comparator.inRelation(tree1, tree2);
			sw.stop();
			subsumed += b ? 1 : 0;
			int size1 = tree1.size(), size2 = tree2.size();
			tree_size_min = Math.min(tree_size_min, size1);
			tree_size_min = Math.min(tree_size_min, size2);
			tree_size_max = Math.max(tree_size_max, size1);
			tree_size_max = Math.max(tree_size_max, size2);
			long time = sw.getDuration();
			l_time.add(time);
			l_size.add(size1 + size2);
			total_time += time;
			pair_nb++;
		}
		writeOutput(LOG_SIZE_MIN, log_size_min);
		writeOutput(LOG_SIZE_MAX, log_size_max);
		writeOutput(TREE_SIZE_MIN, tree_size_min);
		writeOutput(TREE_SIZE_MAX, tree_size_max);
		writeOutput(AVG_TIME, (float) total_time / (float) pair_nb);
		writeOutput(SUBSUMED, subsumed);
		writeOutput(NUM_PAIRS, pair_nb);
	}
}
