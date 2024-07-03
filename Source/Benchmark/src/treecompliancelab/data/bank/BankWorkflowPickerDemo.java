package treecompliancelab.data.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.synthia.random.RandomFloat;

public class BankWorkflowPickerDemo
{
	public static void main(String[] args)
	{
		BankWorkflowPicker bank = new BankWorkflowPicker(new RandomFloat());
		List<String> workflow = new ArrayList<>();
		while (!bank.isDone())
		{
			workflow.add(bank.pick());
		}
		BankPopulator pop = new BankPopulator();
		List<Map<String,Object>> trace = pop.populate(workflow);
		System.out.println(trace);
	}

}
