package ca.uqac.lif.cep.shaded;

import ca.uqac.lif.cep.io.CommandRunner;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.fs.FileUtils;
import ca.uqac.lif.fs.HardDisk;

public class DotRenderer
{
	public static enum Algorithm {DOT, NEATO}
	
	public static void toImage(Algorithm alg, String dot_content, String filename)
	{
		byte[] image = render(alg, dot_content);
		try
		{
			HardDisk hd = new HardDisk("/").open();
			FileUtils.writeBytesTo(hd, image, filename);
			hd.close();
		}
		catch (FileSystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static byte[] render(Algorithm alg, String dot_content)
	{
		CommandRunner runner = new CommandRunner(new String[] {alg == Algorithm.DOT ? "dot" : "neato", "-Tpng"}, dot_content.getBytes());
		runner.run();
		return runner.getBytes();
	}
}
