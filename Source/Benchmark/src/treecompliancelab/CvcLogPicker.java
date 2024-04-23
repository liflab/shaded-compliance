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
