package treecompliancelab;

import java.util.List;

import ca.uqac.lif.cep.shaded.DotRenderer.Format;
import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.ShadedConnective.Color;
import ca.uqac.lif.cep.shaded.TreeComparator;
import ca.uqac.lif.cep.shaded.TreeRenderer;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.labpal.experiment.Experiment;
import ca.uqac.lif.labpal.util.Stopwatch;
import ca.uqac.lif.xml.XmlElement;

public class TreeComparisonExperiment<T> extends Experiment
{
	public static final String CONDITION = "Condition";

	public static final String LOG_SIZE_MIN = "Log size (min)";
	
	public static final String LOG_SIZE_MAX = "Log size (max)";
	
	public static final String TREE_SIZE_MIN = "Tree size (min)";
	
	public static final String TREE_SIZE_MAX = "Tree size (max)";
	
	public static final String NUM_PAIRS = "Number of pairs";

	public static final String TIME = "Average time";
	
	public static final String SUBSUMED = "Subsumed";

	protected final LogPairPicker<T> m_picker;

	protected final TreeComparator m_comparator;

	/**
	 * The condition to evaluate on each log.
	 */
	protected final ShadedConnective m_condition;

	public TreeComparisonExperiment(String condition_name, ShadedConnective condition, TreeComparator comparator, LogPairPicker<T> picker)
	{
		super();
		describe(CONDITION, "The condition evaluated on each event trace");
		describe(LOG_SIZE_MIN, "The size of the smallest log");
		describe(LOG_SIZE_MAX, "The size of the largest log");
		describe(TREE_SIZE_MIN, "The size of the smallest tree");
		describe(TREE_SIZE_MAX, "The size of the largest tree");
		describe(TIME, "The average time taken to compare the evaluation trees of a log pair (in ms)");
		describe(SUBSUMED, "The number of pairs of logs for which the subsumption relation holds");
		describe(NUM_PAIRS, "The numbe of pairs of logs considered");
		writeInput(CONDITION, condition_name);
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
			/*if (tree1.getValue() == Color.GREEN && tree2.getValue() == Color.RED)
			{
				// No point in comparing them, the result is instantaneous
				continue;
			}*/		
			boolean b = m_comparator.inRelation(tree1, tree2);
			sw.stop();
			subsumed += b ? 1 : 0;
			tree_size_min = Math.min(tree_size_min, tree1.size());
			tree_size_min = Math.min(tree_size_min, tree2.size());
			tree_size_max = Math.max(tree_size_max, tree1.size());
			tree_size_max = Math.max(tree_size_max, tree2.size());
			long time = sw.getDuration();
			total_time += time;
			/*
			TreeRenderer tr = new TreeRenderer(false);
			tr.toImage(tree1, "/tmp/" + pair_nb + "-1.png", Format.PNG);
			tr.toImage(tree1, "/tmp/" + pair_nb + "-2.png", Format.PNG);
			*/
			pair_nb++;
			//break;
		}
		writeOutput(LOG_SIZE_MIN, log_size_min);
		writeOutput(LOG_SIZE_MAX, log_size_max);
		writeOutput(TREE_SIZE_MIN, tree_size_min);
		writeOutput(TREE_SIZE_MAX, tree_size_max);
		writeOutput(TIME, (float) total_time / (float) pair_nb);
		writeOutput(SUBSUMED, subsumed);
		writeOutput(NUM_PAIRS, pair_nb);
	}
}
