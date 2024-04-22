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

public class TreeComparisonExperiment extends Experiment
{
	public static final String CONDITION = "Condition";

	public static final String LOG_SIZE = "Log size";
	
	public static final String TREE_SIZE = "Tree size";

	public static final String TIME = "Time";
	
	public static final String SUBSUMED = "Subsumed";

	protected final LogPairPicker m_picker;

	protected final TreeComparator m_comparator;

	/**
	 * The condition to evaluate on each log.
	 */
	protected final ShadedConnective m_condition;

	public TreeComparisonExperiment(String condition_name, ShadedConnective condition, TreeComparator comparator, LogPairPicker picker)
	{
		super();
		describe(CONDITION, "The condition evaluated on each event trace");
		describe(LOG_SIZE, "The cumulative size of the compared traces");
		describe(TREE_SIZE, "The cumulative size of the compared evaluation trees");
		describe(TIME, "The time taken to compare the evaluation trees of a log pair (in ms)");
		describe(SUBSUMED, "Whether the subsumption relation holds for the pair of traces");
		writeInput(CONDITION, condition_name);
		m_condition = condition;
		m_comparator = comparator;
		m_picker = picker;
	}

	@Override
	public void execute()
	{
		Stopwatch sw = new Stopwatch();
		JsonList l_time = new JsonList(), l_log_size = new JsonList(), l_tree_size = new JsonList(), l_subsumed = new JsonList();
		writeOutput(TIME, l_time);
		writeOutput(LOG_SIZE, l_log_size);
		writeOutput(TREE_SIZE, l_tree_size);
		writeOutput(SUBSUMED, l_subsumed);
		int pair_nb = 0;
		while (!m_picker.isDone())
		{
			List<XmlElement>[] pair = m_picker.pick();
			ShadedConnective tree1 = m_condition.duplicate();
			for (XmlElement e : pair[0])
			{
				tree1.update(e);
			}
			ShadedConnective tree2 = m_condition.duplicate();
			for (XmlElement e : pair[1])
			{
				tree2.update(e);
			}
			if (tree1.getValue() == Color.GREEN && tree2.getValue() == Color.RED)
			{
				// No point in comparing them, the result is instantaneous
				continue;
			}
			int tree_size = tree1.size() + tree2.size();
			l_tree_size.add(tree_size);
			int log_size = pair[0].size() + pair[1].size();
			l_log_size.add(log_size);
			sw.start();
			boolean b = m_comparator.inRelation(tree1, tree2);
			sw.stop();
			long time = sw.getDuration();
			l_time.add(time);
			l_subsumed.add(b ? 1 : 0);
			
			TreeRenderer tr = new TreeRenderer(false);
			tr.toImage(tree1, "/tmp/" + pair_nb + "-1.png", Format.PNG);
			tr.toImage(tree1, "/tmp/" + pair_nb + "-2.png", Format.PNG);
			
			pair_nb++;
			//break;
		}
	}
}
