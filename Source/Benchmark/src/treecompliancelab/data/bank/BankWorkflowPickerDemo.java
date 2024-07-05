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
