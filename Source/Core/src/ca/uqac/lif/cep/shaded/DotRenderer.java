package ca.uqac.lif.cep.shaded;

import ca.uqac.lif.cep.io.CommandRunner;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.fs.FileUtils;
import ca.uqac.lif.fs.HardDisk;

public class DotRenderer
{
	public static enum Algorithm {DOT, NEATO}

	public static enum Format {PNG, PDF, SVG}

	public static void toImage(Algorithm alg, String dot_content, String filename, Format format)
	{
		byte[] image = render(alg, dot_content, format);
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

	protected static byte[] render(Algorithm alg, String dot_content, Format format)
	{
		String fmt;
		switch (format)
		{
			case PNG:
				fmt = "png";
				break;
			case PDF:
				fmt = "pdf";
				break;
			case SVG:
				fmt = "svg";
				break;
			default:
				fmt = "png";
				break;
		}
		CommandRunner runner = new CommandRunner(new String[] {alg == Algorithm.DOT ? "dot" : "neato", "-T" + fmt}, dot_content.getBytes());
		runner.run();
		return runner.getBytes();
	}
}
