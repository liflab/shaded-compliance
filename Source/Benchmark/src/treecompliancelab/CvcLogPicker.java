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
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.shaded.CommandLine;
import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.synthia.Picker;

public class CvcLogPicker extends MultiLogPicker<Map<String,Object>>
{
	public CvcLogPicker(FileSystem fs, String ... filenames)
	{
		super(fs, filenames);
	}
	
	public CvcLogPicker(FileSystem fs, List<String> filenames)
	{
		super(fs, filenames);
	}

	@Override
	public Picker<List<Map<String,Object>>> duplicate(boolean with_state)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected List<Map<String, Object>> readFile(InputStream is)
	{
		return CommandLine.readCsvTrace(is);
	}
}
