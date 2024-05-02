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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.xml.XmlElement;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

/**
 * A {@link LogPicker} that produces a log entries by reading them from a file.
 * Events in the file are delineated by two regular expressions, one for the
 * beginning of the event and one for the end of the event. Any text between
 * the two regular expressions is considered to be part of the event. Any text
 * between the end of an event and the beginning of the next event is ignored.
 */
public class FileLogPicker extends MultiLogPicker<XmlElement>
{
	/**
	 * The regular expression that indicates the beginning of an event.
	 */
	protected final String m_start;

	/**
	 * The regular expression that indicates the end of an event.
	 */
	protected final String m_end;

	/**
	 * Creates a new file log picker.
	 * @param start The regular expression that indicates the beginning of an
	 * event
	 * @param end The regular expression that indicates the end of an event
	 * @param streams The names of the files to read from
	 */
	public FileLogPicker(String start, String end, FileSystem fs, String ... filenames)
	{
		super(fs, filenames);
		m_start = start;
		m_end = end;
	}

	/**
	 * Creates a new file log picker.
	 * @param start The regular expression that indicates the beginning of an
	 * event
	 * @param end The regular expression that indicates the end of an event
	 * @param streams The names of the files to read from
	 */
	public FileLogPicker(String start, String end, FileSystem fs, List<String> filenames)
	{
		this(start, end, fs, filenames.toArray(new String[filenames.size()]));
	}

	@Override
	public Picker<List<XmlElement>> duplicate(boolean with_state)
	{
		throw new UnsupportedOperationException("This picker cannot be duplicated");
	}
	
	public static List<XmlElement> readFile(InputStream is, String start, String end)
	{
		List<XmlElement> log = new ArrayList<XmlElement>();
		boolean in_event = false;
		Scanner scanner = new Scanner(is);
		StringBuilder current_event = new StringBuilder();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.matches(start))
			{
				in_event = true;
			}
			if (in_event)
			{
				current_event.append(line);
			}
			if (line.matches(end))
			{
				in_event = false;
				XmlElement event;
				try
				{
					event = XmlElement.parse(current_event.toString());
					log.add(event);
					current_event.setLength(0);
				}
				catch (XmlParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		scanner.close();
		return log;
	}

	/**
	 * Reads a log file from an input stream.
	 * @param is The input stream to read from
	 * @return The list of XML elements in the file
	 * @throws XmlParseException If the XML could not be parsed
	 */
	protected List<XmlElement> readFile(InputStream is)
	{
		return readFile(is, m_start, m_end);
	}

	@Override
	public void reset()
	{
		throw new UnsupportedOperationException("This picker cannot be reset");
	}
}
