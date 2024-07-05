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

import ca.uqac.lif.synthia.Picker;

public class BankWorkflowPicker extends LinearPath<String>
{
	@SuppressWarnings("unchecked")
	public BankWorkflowPicker(Picker<Float> float_source)
	{
		super(
				new ActivitySequence<String>("A").setLoop(false),
				new ParallelGateway<String>(float_source,
						new LinearPath<String>(
								new ActivitySequence<String>("B").setLoop(false),
								new ExclusiveGateway<String>(float_source,
										new LinearPath<String>(
												new ActivitySequence<String>("C").setLoop(false),
												new ActivitySequence<String>("D").setLoop(false)
												),
										new EpsilonGateway<String>()
								)
						),
						new LinearPath<String>(
								new ActivitySequence<String>("E").setLoop(false),
								new ActivitySequence<String>("F").setLoop(false),
								new ExclusiveGateway<String>(float_source,
										new LinearPath<String>(
												new ActivitySequence<String>("G").setLoop(false),
												new ActivitySequence<String>("H").setLoop(false)
												),
										new EpsilonGateway<String>()
								)
						)
				),
				new ActivitySequence<String>("I").setLoop(false),
				new ParallelGateway<String>(float_source,
						new ActivitySequence<String>("J").setLoop(false),
						new LinearPath<String>(
								new ActivitySequence<String>("K").setLoop(false),
								new ParallelGateway<String>(float_source,
										new ActivitySequence<String>("L").setLoop(false),
										new ActivitySequence<String>("M").setLoop(false)
								)
						)
				),
				new ActivitySequence<String>("N").setLoop(false),
				new ActivitySequence<String>("O").setLoop(false),
				new ExclusiveGateway<String>(float_source,
						new LinearPath<String>(
								new ActivitySequence<String>("P").setLoop(false),
								new ActivitySequence<String>("R").setLoop(false),
								new ParallelGateway<String>(float_source,
										new ActivitySequence<String>("S").setLoop(false),
										new LinearPath<String>(
												new ActivitySequence<String>("T").setLoop(false),
												new ExclusiveGateway<String>(float_source,
														new ActivitySequence<String>("U").setLoop(false),
														new ActivitySequence<String>("V").setLoop(false)
												)
										)
								)
						),
						new ActivitySequence<String>("Q").setLoop(false)
				),
				new ActivitySequence<String>("W").setLoop(false)
		);
	}
}
