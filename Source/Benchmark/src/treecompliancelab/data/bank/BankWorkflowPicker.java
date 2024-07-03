package treecompliancelab.data.bank;

import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.sequence.Playback;
import ca.uqac.lif.synthia.util.NothingPicker;

public class BankWorkflowPicker extends LinearPath<String>
{
	@SuppressWarnings("unchecked")
	public BankWorkflowPicker(Picker<Float> float_source)
	{
		super(
				new Playback<String>("A").setLoop(false),
				new ParallelGateway<String>(float_source,
						new LinearPath<String>(
								new Playback<String>("B").setLoop(false),
								new ExclusiveGateway<String>(float_source,
										new LinearPath<String>(
												new Playback<String>("C").setLoop(false),
												new Playback<String>("D").setLoop(false)
												),
										new NothingPicker<String>()
								)
						),
						new LinearPath<String>(
								new Playback<String>("E").setLoop(false),
								new Playback<String>("F").setLoop(false),
								new ExclusiveGateway<String>(float_source,
										new LinearPath<String>(
												new Playback<String>("G").setLoop(false),
												new Playback<String>("H").setLoop(false)
												),
										new NothingPicker<String>()
								)
						)
				),
				new Playback<String>("I").setLoop(false),
				new ParallelGateway<String>(float_source,
						new Playback<String>("J").setLoop(false),
						new LinearPath<String>(
								new Playback<String>("K").setLoop(false),
								new ParallelGateway<String>(float_source,
										new Playback<String>("L").setLoop(false),
										new Playback<String>("M").setLoop(false)
								)
						)
				),
				new Playback<String>("N").setLoop(false),
				new Playback<String>("O").setLoop(false),
				new ExclusiveGateway<String>(float_source,
						new LinearPath<String>(
								new Playback<String>("P").setLoop(false),
								new Playback<String>("R").setLoop(false),
								new ParallelGateway<String>(float_source,
										new Playback<String>("S").setLoop(false),
										new LinearPath<String>(
												new Playback<String>("T").setLoop(false),
												new ExclusiveGateway<String>(float_source,
														new Playback<String>("U").setLoop(false),
														new Playback<String>("V").setLoop(false)
												)
										)
								)
						),
						new Playback<String>("Q").setLoop(false)
				),
				new Playback<String>("W").setLoop(false)
		);
	}
}
