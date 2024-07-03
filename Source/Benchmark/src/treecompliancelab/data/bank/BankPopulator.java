package treecompliancelab.data.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.random.RandomInteger;
import ca.uqac.lif.synthia.util.Tick;

public class BankPopulator
{
	protected final Picker<Integer> m_smallPicker = new RandomInteger(0, 100);
	
	protected final Picker<Integer> m_largePicker = new RandomInteger(101, 1000);
	
	public List<Map<String,Object>> populate(List<String> workflow)
	{
		List<Map<String,Object>> events = new ArrayList<Map<String,Object>>();
		float last = 0;
		Tick timestamp = new Tick(0, new RandomInteger(1, 10));
		boolean is_vip = workflow.contains("C");
		boolean is_valid = !workflow.contains("Q");
		boolean is_large = workflow.contains("V");
		for (String s : workflow)
		{
			Map<String,Object> event = new HashMap<String,Object>();
			event.put("action", s);
			float ts = timestamp.pick().floatValue();
			float delay = ts - last;
			last = ts;
			event.put("delay", delay);
			switch (s)
			{
				case "O":
					event.put("valid", is_valid);
					break;
				case "T":
					event.put("amount", is_large ? m_largePicker.pick() : m_smallPicker.pick());
					break;
			}
			event.put("status", is_vip ? "vip" : "normal");
			event.put("level", is_vip ? "senior" : "normal");
			events.add(event);
		}
		return events;
	}
}
