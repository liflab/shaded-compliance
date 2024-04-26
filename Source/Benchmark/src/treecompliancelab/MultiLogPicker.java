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
