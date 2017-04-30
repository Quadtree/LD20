package info.quadtree.rv.actor;

import info.quadtree.rv.Dialog;
import info.quadtree.rv.Game;

public class Mayor extends AlliedRobot {

	public Mayor(float x, float y) {
		super(x, y);
		reset();
	}
	
	@Override
	public void reset() {
		hp = 25;
	}

	@Override
	public void startConversation() {
		if(Game.s.plot == 0)
		{
			Dialog d = new Dialog();
			
			d.addMessage("Mayor: Ah, you must be one of those mercenaries we called for.");
			d.addMessage(Game.s.player.getName() + ": Yup.");
			d.addMessage("Mayor: We've been having a series of attacks from the hills recently.");
			d.addMessage("Mayor: I'm prepared to offer a bounty for any enemies you can destroy");
			d.addMessage("Mayor: My assistant, our technician can tell you more.");
			d.addMessage("Mayor: He's in the building to the west of here.");
			d.addMessage(Game.s.player.getName() + ": Okay.");
			
			Game.s.plot = 1;
			
			Game.s.actors.add(d);
		} else if(Game.s.plot == 6)
		{
			Dialog d = new Dialog();
			
			d.addMessage("Mayor: Aaaah! Stop him! Help!");
			
			Game.s.actors.add(d);
		} else if(Game.s.plot == 7)
		{
			Dialog d = new Dialog();
			
			d.addMessage("Mayor: Excellent work! You saved our town!");
			d.addMessage("Mayor: We'll probably have to hold a parade in your honor!");
			
			Game.s.actors.add(d);
		}
		else
		{
			Dialog d = new Dialog();
			
			d.addMessage("Mayor: Go talk to my assistant about the mission.");
			d.addMessage("Mayor: He's in the building to the west of here.");
			d.addMessage(Game.s.player.getName() + ": Right.");
			
			Game.s.actors.add(d);
		}
		
		super.startConversation();
	}

	@Override
	protected String getTopPrefix() {
		return "topmayor";
	}
}
