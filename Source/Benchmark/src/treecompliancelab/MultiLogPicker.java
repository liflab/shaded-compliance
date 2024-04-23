package treecompliancelab;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.synthia.NoMoreElementException;
import ca.uqac.lif.synthia.PickerException;

public abstract class MultiLogPicker<T> implements LogPicker<T>
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
	 * Creates a new file log picker.
	 * @param streams The names of the files to read from
	 */
	public MultiLogPicker(FileSystem fs, String ... filenames)
	{
		super();
		m_filenames = filenames;
		m_index = 0;
		m_fs = fs;
	}
	
	/**
	 * Creates a new file log picker.
	 * @param start The regular expression that indicates the beginning of an
	 * event
	 * @param end The regular expression that indicates the end of an event
	 * @param streams The names of the files to read from
	 */
	public MultiLogPicker(FileSystem fs, List<String> filenames)
	{
		this(fs, filenames.toArray(new String[filenames.size()]));
	}
	
	@Override
	public List<T> pick()
	{
		if (m_index >= m_filenames.length)
		{
			throw new NoMoreElementException();
		}
		try
		{
			InputStream is = m_fs.readFrom(m_filenames[m_index]);
			List<T> log = readFile(is);
			is.close();
			m_index++;
			return log;
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
	
	@Override
	public void reset()
	{
		m_index = 0;
	}
	
	@Override
	public boolean isDone()
	{
		return m_index >= m_filenames.length;
	}
	
	protected abstract List<T> readFile(InputStream is);


}
