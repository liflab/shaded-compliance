package treecompliancelab;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.synthia.NoMoreElementException;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.PickerException;
import ca.uqac.lif.xml.XmlElement;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

/**
 * A {@link LogPicker} that produces a log entries by reading them from a file.
 * Events in the file are delineated by two regular expressions, one for the
 * beginning of the event and one for the end of the event. Any text between
 * the two regular expressions is considered to be part of the event. Any text
 * between the end of an event and the beginning of the next event is ignored.
 */
public class FileLogPicker implements LogPicker
{
	/**
	 * The index of the next input stream to read from.
	 */
	protected int m_index;
	
	/**
	 * The names of the files to read from
	 */
	protected final String[] m_filenames;
	
	/**
	 * The file system giving access to the files to read from.
	 */
	protected final FileSystem m_fs;
	
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
		super();
		m_filenames = filenames;
		m_index = 0;
		m_start = start;
		m_end = end;
		m_fs = fs;
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
	public boolean isDone()
	{
		return m_index >= m_filenames.length;
	}

	@Override
	public Picker<List<XmlElement>> duplicate(boolean with_state)
	{
		throw new UnsupportedOperationException("This picker cannot be duplicated");
	}

	@Override
	public List<XmlElement> pick()
	{
		if (m_index >= m_filenames.length)
		{
			throw new NoMoreElementException();
		}
		try
		{
			InputStream is = m_fs.readFrom(m_filenames[m_index]);
			List<XmlElement> log = readFile(is);
			is.close();
			m_index++;
			return log;
		}
		catch (XmlParseException e)
		{
			throw new PickerException(e);
		}
		catch (FileSystemException e)
		{
			throw new PickerException(e);
		}
		catch (IOException e)
		{
			throw new PickerException(e);
		}
	}
	
	/**
	 * Reads a log file from an input stream.
	 * @param is The input stream to read from
	 * @return The list of XML elements in the file
	 * @throws XmlParseException If the XML could not be parsed
	 */
	protected List<XmlElement> readFile(InputStream is) throws XmlParseException
	{
		List<XmlElement> log = new ArrayList<XmlElement>();
		boolean in_event = false;
		Scanner scanner = new Scanner(is);
		StringBuilder current_event = new StringBuilder();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.matches(m_start))
			{
				in_event = true;
			}
			if (in_event)
			{
				current_event.append(line);
			}
			if (line.matches(m_end))
			{
				in_event = false;
				XmlElement event = XmlElement.parse(current_event.toString());
				log.add(event);
				current_event.setLength(0);
			}
		}
		scanner.close();
		return log;
	}

	@Override
	public void reset()
	{
		throw new UnsupportedOperationException("This picker cannot be reset");
	}
}
