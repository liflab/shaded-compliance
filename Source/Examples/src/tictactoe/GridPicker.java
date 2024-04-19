package tictactoe;

import ca.uqac.lif.synthia.Bounded;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.enumerative.AllPickers;
import ca.uqac.lif.synthia.relative.PickIf;
import ca.uqac.lif.synthia.sequence.Playback;

public class GridPicker extends PickIf<String>
{
	public GridPicker()
	{
		super(new GPicker());
	}
	
	@Override
	protected boolean select(String s)
	{
		int cnt_x = 0, cnt_o = 0;
		for (int i = 0; i < s.length(); i++)
		{
			cnt_x += s.charAt(i) == 'X' ? 1 : 0;
			cnt_o += s.charAt(i) == 'O' ? 1 : 0;
		}
		//return cnt_x == 5 && cnt_o == 4;
		
		if (Math.abs(cnt_x - cnt_o) > 1)
		{
			return false;
		}
		//return true;
		return won(s, 'X') || won(s, 'O');
		
	}
	
	protected static boolean won(String s, char player)
	{
		if (s.charAt(0) == player && s.charAt(1) == player && s.charAt(2) == player)
			return true;
		if (s.charAt(3) == player && s.charAt(4) == player && s.charAt(5) == player)
			return true;
		if (s.charAt(6) == player && s.charAt(7) == player && s.charAt(8) == player)
			return true;
		if (s.charAt(0) == player && s.charAt(3) == player && s.charAt(6) == player)
			return true;
		if (s.charAt(1) == player && s.charAt(4) == player && s.charAt(7) == player)
			return true;
		if (s.charAt(2) == player && s.charAt(5) == player && s.charAt(8) == player)
			return true;
		if (s.charAt(0) == player && s.charAt(4) == player && s.charAt(8) == player)
			return true;
		if (s.charAt(2) == player && s.charAt(4) == player && s.charAt(6) == player)
			return true;
		return false;
	}
	
	protected static class GPicker implements Picker<String>
	{
		protected AllPickers m_picker;
		
		@SuppressWarnings("rawtypes")
		public GPicker()
		{
			super();
			Bounded[] bools = new Bounded[9];
			for (int i = 0; i < bools.length; i++)
			{
				bools[i] = new Playback<String>("X", "O", " ").setLoop(false);
			}
			m_picker = new AllPickers(bools);
		}

		@Override
		public Picker<String> duplicate(boolean with_state)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String pick()
		{
			Object[] objs = m_picker.pick();
			String out = "";
			for (Object o : objs)
			{
				String s = (String) o;
				out += s;
			}
			return out;
		}

		@Override
		public void reset()
		{
			m_picker.reset();
		}
	}
}
