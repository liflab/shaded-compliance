package treecompliancelab;

import static ca.uqac.lif.labpal.region.ProductRegion.product;
import static ca.uqac.lif.labpal.region.ExtensionDomain.extension;

import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.Subsumption;
import ca.uqac.lif.fs.Chroot;
import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.fs.FileUtils;
import ca.uqac.lif.fs.JarFile;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.region.Region;
import ca.uqac.lif.xml.XmlElement;

public class MainLab extends Laboratory
{
	@Override
	public void setup()
	{
		try
		{
			FileSystem main_fs = new JarFile(MainLab.class).open();

			// Beep Store
			{
				FileSystem fs = new Chroot(main_fs, "data/beepstore");
				List<String> filenames = FileUtils.ls(fs, "", "log.*\\.xml");
				Region big_r = product(extension("Property", BeepStoreProperty.ONCE_ITEM_SEARCH, BeepStoreProperty.MAX_CARTS, BeepStoreProperty.NO_DUPLICATE_ITEM));
				for (Region r : big_r.all("Property"))
				{
					String property = r.asPoint().getString("Property");
					LogPairPicker<XmlElement> picker = new LogPairPicker<XmlElement>(new FileLogPicker("<Message>", "</Message>", fs, filenames));
					TreeComparisonExperiment<XmlElement> experiment = new TreeComparisonExperiment<>(
							property, BeepStoreProperty.get(property), new Subsumption(), picker);
					add(experiment);
				}
			}
			// CVC Procedure
			{
				FileSystem fs = new Chroot(main_fs, "data/cvc");
				List<String> filenames = FileUtils.ls(fs, "", ".*\\.csv");
				Region big_r = product(extension("Property", CvcProperty.MAX_DURATION, CvcProperty.LIFECYCLE));
				for (Region r : big_r.all("Property"))
				{
					String property = r.asPoint().getString("Property");
					LogPairPicker<Map<String,Object>> picker = new LogPairPicker<>(new CvcLogPicker(fs, filenames));
					TreeComparisonExperiment<Map<String,Object>> experiment = new TreeComparisonExperiment<>(
							property, CvcProperty.get(property), new Subsumption(), picker);
					add(experiment);
				}
			}
		}
		catch (FileSystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		initialize(args, MainLab.class);
	}
}
