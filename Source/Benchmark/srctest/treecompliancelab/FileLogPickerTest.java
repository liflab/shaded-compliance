package treecompliancelab;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ca.uqac.lif.fs.FileSystem;
import ca.uqac.lif.fs.FileSystemException;
import ca.uqac.lif.fs.JarFile;
import ca.uqac.lif.xml.XmlElement;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

/**
 * Unit tests for the {@link FileLogPicker} class.
 */
public class FileLogPickerTest
{
	@Test
	public void test1() throws XmlParseException, FileSystemException
	{
		FileSystem fs = new JarFile(FileLogPicker.class).open();
		FileLogPicker picker = new FileLogPicker("<Message>", "</Message>", fs, "data/beepstore/log-1.xml");
		assertFalse(picker.isDone());
		List<XmlElement> log = picker.pick();
		assertNotNull(log);
		assertEquals(10, log.size());
		fs.close();
	}
	
	@Test
	public void test2() throws XmlParseException, FileSystemException
	{
		FileSystem fs = new JarFile(FileLogPicker.class).open();
		FileLogPicker picker = new FileLogPicker("<Message>", "</Message>", fs, "data/beepstore/log-0.xml", "data/beepstore/log-1.xml");
		assertFalse(picker.isDone());
		List<XmlElement> log = picker.pick();
		assertNotNull(log);
		assertEquals(10, log.size());
		assertFalse(picker.isDone());
		log = picker.pick();
		assertNotNull(log);
		assertEquals(10, log.size());
	}
}
