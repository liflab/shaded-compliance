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

import static ca.uqac.lif.labpal.region.ProductRegion.product;
import static ca.uqac.lif.labpal.region.ExtensionDomain.extension;

import static treecompliancelab.TreeComparisonExperiment.AVG_TIME;
import static treecompliancelab.TreeComparisonExperiment.CONDITION;
import static treecompliancelab.TreeComparisonExperiment.LOG_SIZE_MAX;
import static treecompliancelab.TreeComparisonExperiment.LOG_SIZE_MIN;
import static treecompliancelab.TreeComparisonExperiment.NUM_PAIRS;
import static treecompliancelab.TreeComparisonExperiment.SUBSUMED;
import static treecompliancelab.TreeComparisonExperiment.TIME;
import static treecompliancelab.TreeComparisonExperiment.TREE_SIZE;
import static treecompliancelab.TreeComparisonExperiment.TREE_SIZE_MAX;
import static treecompliancelab.TreeComparisonExperiment.TREE_SIZE_MIN;
import static treecompliancelab.TreeComparisonExperiment.SCENARIO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.Subsumption;
import ca.uqac.lif.fs.Chroot;
import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.fs.FileUtils;
import ca.uqac.lif.fs.JarFile;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.plot.Plot;
import ca.uqac.lif.labpal.region.Region;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.spreadsheet.chart.Chart.Axis;
import ca.uqac.lif.spreadsheet.chart.Scatterplot;
import ca.uqac.lif.spreadsheet.chart.gnuplot.GnuplotScatterplot;
import ca.uqac.lif.xml.XmlElement;

public class MainLab extends Laboratory
{
	@Override
	public void setup()
	{
		ExperimentTable results = new ExperimentTable(SCENARIO, LOG_SIZE_MIN, LOG_SIZE_MAX, CONDITION, TREE_SIZE_MIN, TREE_SIZE_MAX, AVG_TIME, NUM_PAIRS, SUBSUMED);
		add(results);
		try
		{
			FileSystem main_fs = new JarFile(MainLab.class).open();

			// Beep Store
			{
				BeepStoreProperty props = new BeepStoreProperty();
				TreeAbstractionFactory abstractions = new TreeAbstractionFactory();
				
				FileSystem fs = new Chroot(main_fs, "data/beepstore");
				List<String> filenames = FileUtils.ls(fs, "", "log-(\\d|1\\d|2\\d)\\.xml");
				//List<String> filenames = Arrays.asList("log-5.xml", "log-6.xml");
				Region big_r = product(
						extension("Property", (Object[]) props.getProperties()),
						extension("Abstraction", (Object[]) abstractions.getAbstractions())
				);
				for (Region r : big_r.all("Property", "Abstraction"))
				{
					String property = r.asPoint().getString("Property");
					String abstraction = r.asPoint().getString("Abstraction");
					LogPairPicker<XmlElement> picker = new LogPairPicker<XmlElement>(new FileLogPicker("<Message>", "</Message>", fs, filenames));
					TreeComparisonExperiment<XmlElement> experiment = new TreeComparisonExperiment<XmlElement>(
							"Beep Store", props.get(property), new Subsumption(), abstractions.get(abstraction), picker);
					add(experiment);
					results.add(experiment);
					ExperimentTable et = new ExperimentTable(TREE_SIZE, TIME);
					et.add(experiment);
					add(et);
					Plot plot = new Plot(et, new GnuplotScatterplot().setTitle("Time vs tree size, property " + property).setCaption(Axis.X, "Tree size").setCaption(Axis.Y, "Time (ms)").withLines(false));
					add(plot);
				}
			}
			// CVC Procedure
			{
				CvcProperty props = new CvcProperty();
				TreeAbstractionFactory abstractions = new TreeAbstractionFactory();
				FileSystem fs = new Chroot(main_fs, "data/cvc");
				List<String> filenames = FileUtils.ls(fs, "", ".*\\.csv");
				Region big_r = product(
						extension("Property", (Object[]) props.getProperties()),
						extension("Abstraction", (Object[]) abstractions.getAbstractions())
				);
				for (Region r : big_r.all("Property", "Abstraction"))
				{
					String property = r.asPoint().getString("Property");
					String abstraction = r.asPoint().getString("Abstraction");
					LogPairPicker<Map<String,Object>> picker = new LogPairPicker<>(new CvcLogPicker(fs, filenames));
					TreeComparisonExperiment<Map<String,Object>> experiment = new TreeComparisonExperiment<Map<String,Object>>(
							"CVC Procedure", props.get(property), new Subsumption(), abstractions.get(abstraction), picker);
					add(experiment);
					results.add(experiment);
				}
			}
		}
		catch (FileSystemException e)
		{
			// Should not happen
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		initialize(args, MainLab.class);
	}
}
