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
package ca.uqac.lif.cep.shaded;

import java.util.ArrayList;
import java.util.Scanner;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.Builds;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder;

public class ShadedParser extends ParseTreeObjectBuilder<ShadedConnective>
{
	/**
	 * The parser used to parse the input.
	 */
	protected static final BnfParser s_parser = new BnfParser();
	
	static
	{
		Scanner scanner = new Scanner(ShadedParser.class.getResourceAsStream("ltl.bnf"));
		try
		{
			s_parser.setGrammar(scanner);
			//FileOutputStream fos = new FileOutputStream("/tmp/debug.txt");
			s_parser.setDebugMode(false);
		}
		catch (InvalidGrammarException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.close();
	}
	
	public static ShadedConnective parse(String s)
	{
		ParseNode tree;
		try
		{
			tree = s_parser.parse(s);
			if (tree == null)
			{
				return null;
			}
			ShadedParser parser = new ShadedParser();
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
	
	@Builds(rule="<phi>", pop=true, clean=true)
	public ShadedConnective handlePhi(Object ... parts)
	{
		return (ShadedConnective) parts[0];
	}

	@Builds(rule="<F>", pop=true, clean=true)
	public ShadedConnective handleF(Object... parts)
	{
		ShadedConnective inside = (ShadedConnective) parts[0];
		return new ShadedF(inside);
	}

	@Builds(rule="<G>", pop=true, clean=true)
	public ShadedConnective handleG(Object... parts)
	{
		ShadedConnective inside = (ShadedConnective) parts[0];
		return new ShadedG(inside);
	}

	@Builds(rule="<X>", pop=true, clean=true)
	public ShadedConnective handleX(Object... parts)
	{
		ShadedConnective inside = (ShadedConnective) parts[0];
		return new ShadedX(inside);
	}

	@Builds(rule="<not>", pop=true, clean=true)
	public ShadedConnective handleNot(Object... parts)
	{
		ShadedConnective inside = (ShadedConnective) parts[0];
		return new ShadedNot(inside);
	}

	@Builds(rule="<eq>", pop=true, clean=true)
	public ShadedConnective handleEq(Object... parts)
	{
		ShadedFunction left = (ShadedFunction) parts[0];
		ShadedFunction right = (ShadedFunction) parts[1];
		return new ShadedComparison.ShadedEquals(left, right);
	}

	@Builds(rule="<string>", pop=true, clean=false)
	public ShadedConstant handleString(Object... parts)
	{
		String s = parts[0].toString();
		s = s.replaceAll("\"", "");
		return new ShadedConstant(s);
	}

	@Builds(rule="<num>", pop=true, clean=false)
	public ShadedConstant handleNumber(Object... parts)
	{
		String s = parts[0].toString();
		return new ShadedConstant(Integer.parseInt(s.trim()));
	}

	@Builds(rule="<param>", pop=true, clean=false)
	public ShadedFetchAttribute handleParam(Object... parts)
	{
		String s = parts[0].toString().trim();
		return new ShadedFetchAttribute(s);
	}

	@Builds(rule="<implies>", pop=true, clean=true)
	public ShadedConnective handleImplies(Object... parts)
	{
		ShadedConnective left = (ShadedConnective) parts[0];
		ShadedConnective right = (ShadedConnective) parts[1];
		return new ShadedOr(new ShadedNot(left), right);
	}

	@Builds(rule="<land>", pop=true, clean=true)
	public ConnectiveList handleLand(Object... parts)
	{
		if (!(parts[0] instanceof ShadedAnd))
		{
			ConnectiveList list = new ConnectiveList();
			list.addAll(((ShadedAnd) parts[0]).m_operands);
			return list;
		}
		ConnectiveList list = new ConnectiveList();
		list.add((ShadedConnective) parts[0]);
		return list;
	}
	
	@Builds(rule="<lor>", pop=true, clean=true)
	public ConnectiveList handleLor(Object... parts)
	{
		if (!(parts[0] instanceof ShadedOr))
		{
			ConnectiveList list = new ConnectiveList();
			list.addAll(((ShadedOr) parts[0]).m_operands);
			return list;
		}
		ConnectiveList list = new ConnectiveList();
		list.add((ShadedConnective) parts[0]);
		return list;
	}

	@Builds(rule="<and>", pop=true, clean=true)
	public ShadedAnd handleAnd(Object... parts)
	{
		ShadedConnective left = (ShadedConnective) parts[0];
		if (parts[1] instanceof ConnectiveList)	
		{
			ShadedAnd and = new ShadedAnd(left);
			ConnectiveList list = (ConnectiveList) parts[1];
			for (ShadedConnective c : list)
			{
				and.addOperand(c);
			}
			return and;
		}
		return new ShadedAnd(left, (ShadedConnective) parts[1]);
	}
	
	@Builds(rule="<or>", pop=true, clean=true)
	public ShadedOr handleOr(Object... parts)
	{
		ShadedConnective left = (ShadedConnective) parts[0];
		if (parts[1] instanceof ConnectiveList)	
		{
			ShadedOr or = new ShadedOr(left);
			ConnectiveList list = (ConnectiveList) parts[1];
			for (ShadedConnective c : list)
			{
				or.addOperand(c);
			}
			return or;
		}
		return new ShadedOr(left, (ShadedConnective) parts[1]);
	}
	
	protected static class ConnectiveList extends ArrayList<ShadedConnective>
	{
		private static final long serialVersionUID = 1L;
	}
}
