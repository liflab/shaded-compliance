package treecompliancelab;

import java.util.List;

import ca.uqac.lif.cep.shaded.Subsumption;
import ca.uqac.lif.fs.Chroot;
import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.fs.FileUtils;
import ca.uqac.lif.fs.JarFile;
import ca.uqac.lif.labpal.Laboratory;

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
				List<String> filenames = FileUtils.ls(fs, "", "log-1.\\.xml");
				System.out.println(filenames);
				//List<String> filenames = fs.ls();
				LogPairPicker picker = new LogPairPicker(new FileLogPicker("<Message>", "</Message>", fs, filenames));
				TreeComparisonExperiment experiment = new TreeComparisonExperiment(
						BeepStoreProperty.ONCE_LOGIN, BeepStoreProperty.get(
								BeepStoreProperty.ONCE_LOGIN), new Subsumption(), picker);
				add(experiment);
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
