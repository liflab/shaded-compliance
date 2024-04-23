package ca.uqac.lif.cep.shaded;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ca.uqac.lif.cep.shaded.DotRenderer.Format;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.fs.FileUtils;
import ca.uqac.lif.util.CliParser;
import ca.uqac.lif.util.CliParser.Argument;
import ca.uqac.lif.util.CliParser.ArgumentMap;

public class CommandLine
{
	public static void main(String[] args)
	{
		// Setup command line parser
		if (args.length < 1)
		{
			System.err.println("Usage: CommandLine <action> [options]");
			System.exit(1);
		}
		String action = args[0];
		int status = 1;
		switch (action)
		{
		case "compare":
			status = compare(Arrays.copyOfRange(args, 1, args.length));
			break;
		case "draw-tree":
			status = drawTrees(Arrays.copyOfRange(args, 1, args.length));
			break;
		case "draw-hasse":
			status = drawHasse(Arrays.copyOfRange(args, 1, args.length));
			break;

		}
		if (status != 0)
		{
			System.err.println("Error: action " + action + " failed");
		}
		System.exit(status);
	}

	protected static int compare(String[] args)
	{
		CliParser parser = new CliParser();
		parser.addArgument(new Argument().withLongName("property").withArgument("file").withDescription("Evaluate property in file"));
		parser.addArgument(new Argument().withLongName("in-format").withArgument("f").withDescription("Input traces in format f"));
		ArgumentMap params = parser.parse(args);
		ShadedConnective phi = getProperty(params);
		if (phi == null)
		{
			return 2;
		}
		List<String> filenames = params.getOthers();
		if (filenames.size() < 2)
		{
			System.err.println("Compare requires at least two filenames");
			return 1;
		}
		boolean[][] matrix = new boolean[filenames.size()][filenames.size()];
		for (int i = 0; i < filenames.size(); i++)
		{
			for (int j = 0; j < filenames.size(); j++)
			{
				matrix[i][j] = compareFiles(phi, filenames.get(i), filenames.get(j));
			}
		}
		printMatrix(matrix, System.out);
		return 0;
	}

	protected static int drawTrees(String[] args)
	{
		CliParser parser = new CliParser();
		parser.addArgument(new Argument().withLongName("property").withArgument("file").withDescription("Evaluate property in file"));
		parser.addArgument(new Argument().withLongName("in-format").withArgument("f").withDescription("Input traces in format f"));
		ArgumentMap params = parser.parse(args);
		ShadedConnective phi = getProperty(params);
		if (phi == null)
		{
			return 2;
		}
		List<String> filenames = params.getOthers();
		if (filenames.size() < 2)
		{
			System.err.println("Compare requires at least two filenames");
			return 1;
		}
		for (int i = 0; i < filenames.size(); i++)
		{
			drawTree(phi, filenames.get(i));
		}
		return 0;
	}

	protected static void drawTree(ShadedConnective phi, String filename)
	{
		ShadedConnective phi1 = feed(phi, filename);
		TreeRenderer renderer = new TreeRenderer(false);
		renderer.toImage(phi1, filename.replaceAll("csv", "png"), Format.PNG);
	}

	protected static boolean compareFiles(ShadedConnective phi, String filename1, String filename2)
	{
		Subsumption sub = new Subsumption();
		ShadedConnective phi1 = feed(phi, filename1);
		ShadedConnective phi2 = feed(phi, filename1);
		return sub.inRelation(phi1, phi2);
	}

	protected static List<?> readTrace(String filename)
	{
		if (filename.endsWith(".csv"))
		{
			return readCsvTrace(filename);
		}
		return null;
	}

	public static List<Map<String,Object>> readCsvTrace(InputStream is)
	{
		List<Map<String,Object>> trace = new ArrayList<Map<String,Object>>();
		Scanner scanner = new Scanner(is);
		String[] params = null;
		boolean first = true;
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			if (first)
			{
				String[] parts = line.split(",");
				params = new String[parts.length];
				for (int i = 0; i < parts.length; i++)
				{
					params[i] = parts[i].trim();
				}
				first = false;
			}
			else
			{
				String[] parts = line.split(",");
				Map<String,Object> e = new HashMap<String,Object>();
				for (int i = 0; i < params.length; i++)
				{
					e.put(params[i], parseObject(parts[i].trim()));
				}
				trace.add(e);
			}
		}
		scanner.close();
		return trace;
	}

	protected static List<Map<String,Object>> readCsvTrace(String filename)
	{
		try
		{
			FileInputStream fis = new FileInputStream(filename);
			return readCsvTrace(fis);
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
	}

	protected static void printMatrix(boolean[][] matrix, PrintStream out)
	{
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				out.print(matrix[i][j] ? "1" : "0");
			}
			out.println();
		}
	}

	protected static ShadedConnective readProperty(String filename) throws IOException, FileSystemException
	{
		File f = new File(filename);
		if (!f.exists() || !f.isFile())
		{
			return null;
		}
		FileInputStream fis = new FileInputStream(f);
		byte[] contents = FileUtils.toBytes(fis);
		fis.close();
		return ShadedParser.parse(new String(contents));
	}

	protected static int drawHasse(String[] args)
	{
		CliParser parser = new CliParser();
		parser.addArgument(new Argument().withLongName("property").withArgument("file").withDescription("Evaluate property in file"));
		parser.addArgument(new Argument().withLongName("in-format").withArgument("f").withDescription("Input traces in format f"));
		parser.addArgument(new Argument().withLongName("output").withArgument("file").withDescription("Output diagram to file"));
		ArgumentMap params = parser.parse(args);
		ShadedConnective phi = getProperty(params);
		if (phi == null)
		{
			return 2;
		}
		if (!params.hasOption("output"))
		{
			System.err.println("draw-hasse requires an output file");
			return 4;
		}
		List<String> filenames = params.getOthers();
		List<ShadedConnective> elements = new ArrayList<>();
		for (int i = 0; i < filenames.size(); i++)
		{
			elements.add(feed(phi, filenames.get(i)));
		}
		LatticeGenerator gen = new LatticeGenerator(new Subsumption(true));
		ShadedGraph g = gen.getLattice(elements);
		g.toImage(params.getOptionValue("output"), Format.PNG);
		return 0;
	}

	protected static ShadedConnective getProperty(ArgumentMap params)
	{
		if (params == null || !params.hasOption("property"))
		{
			System.err.println("Compare requires a property file");
			return null;
		}
		ShadedConnective phi;
		try
		{
			phi = readProperty(params.getOptionValue("property"));
		}
		catch (IOException | FileSystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if (phi == null)
		{
			System.err.println("Could not read property in " + params.getOptionValue("property"));
			return null;
		}
		return phi;
	}

	protected static ShadedConnective feed(ShadedConnective phi, String filename)
	{
		List<?> trace = readTrace(filename);
		ShadedConnective phi1 = phi.duplicate();
		for (Object e : trace)
		{
			phi1.update(e);
		}
		return phi1;
	}

	protected static Object parseObject(Object o)
	{
		if (o instanceof String)
		{
			try
			{
				return Integer.parseInt((String) o);
			}
			catch (NumberFormatException e)
			{
				return o;
			}
		}
		return o;
	}
}
