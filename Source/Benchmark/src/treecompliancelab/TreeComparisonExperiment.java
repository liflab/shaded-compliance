package treecompliancelab;

import java.util.List;

import ca.uqac.lif.cep.shaded.ShadedConnective;
import ca.uqac.lif.cep.shaded.TreeComparator;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.labpal.experiment.Experiment;
import ca.uqac.lif.labpal.util.Stopwatch;
import ca.uqac.lif.xml.XmlElement;

public class TreeComparisonExperiment extends Experiment
{
	public static final String CONDITION = "Condition";

	public static final String LOG_SIZE = "Log size";

	public static final String TIME = "Time";

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
		describe(LOG_SIZE, "The cumulative size of the compared logs");
		describe(TIME, "The time taken to compare the evaluation trees of a log pair");
		writeInput(CONDITION, condition_name);
		m_condition = condition;
		m_comparator = comparator;
		m_picker = picker;
	}

	@Override
	public void execute()
	{
		Stopwatch sw = new Stopwatch();
		JsonList l_time = new JsonList();
		JsonList l_log_size = new JsonList();
		writeOutput(TIME, l_time);
		writeOutput(LOG_SIZE, l_log_size);
		while (!m_picker.isDone())
		{
			List<XmlElement>[] pair = m_picker.pick();
			sw.start();
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
			m_comparator.inRelation(tree1, tree2);
			sw.stop();
			long time = sw.getDuration();
			int log_size = pair[0].size() + pair[1].size();
			l_time.add(time);
			l_log_size.add(log_size);
		}
	}
}
