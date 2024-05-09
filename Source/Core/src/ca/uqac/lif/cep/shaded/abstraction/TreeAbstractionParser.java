package ca.uqac.lif.cep.shaded.abstraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.Builds;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

public class TreeAbstractionParser extends ParseTreeObjectBuilder<TreeAbstraction>
{
	/**
	 * The parser used to parse the input.
	 */
	protected static final BnfParser s_parser = new BnfParser();
	
	static
	{
		Scanner scanner = new Scanner(TreeAbstractionParser.class.getResourceAsStream("abstractions.bnf"));
		try
		{
			s_parser.setGrammar(scanner);
			s_parser.setDebugMode(false);
		}
		catch (InvalidGrammarException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.close();
	}
	
	public static TreeAbstraction parse(String s)
	{
		ParseNode tree;
		try
		{
			tree = s_parser.parse(s);
			if (tree == null)
			{
				return null;
			}
			TreeAbstractionParser parser = new TreeAbstractionParser();
			return parser.build(tree);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (BuildException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Builds(rule="<compose>", pop=true, clean=true)
	public TreeAbstraction handleCompose(Object ... parts)
	{
		List<TreeAbstraction> list = (List<TreeAbstraction>) parts[0];
		return new Compose(list);
	}
	
	@SuppressWarnings("unchecked")
	@Builds(rule="<taulist>", pop=true, clean=true)
	public List<TreeAbstraction> handleTauList(Object... parts)
	{
		if (parts.length == 1)
		{
			List<TreeAbstraction> list = new ArrayList<>();
			list.add((TreeAbstraction) parts[0]);
			return list;
		}
		TreeAbstraction abs = (TreeAbstraction) parts[0];
		List<TreeAbstraction> list = (List<TreeAbstraction>) parts[1];
    list.add(0, abs);
    return list;
	}
	
	@Builds(rule="<truncate>", pop=true, clean=true)
	public TreeAbstraction handleTruncate(Object ... parts)
	{
		return new TruncateRoot();
	}
	
	@Builds(rule="<trimcolor>", pop=true, clean=true)
	public TreeAbstraction handleTrim(Object... parts)
	{
		return new TrimColor();
	}
	
	@Builds(rule="<depth>", pop=true, clean=true)
	public TreeAbstraction handleDepth(Object... parts)
	{
		int depth = Integer.parseInt(parts[0].toString());
		TreeAbstraction abs = (TreeAbstraction) parts[1];
		return new TriggerAtDepth(depth, abs);
	}
}
