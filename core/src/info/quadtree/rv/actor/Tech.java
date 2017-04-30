package info.quadtree.rv.actor;

import info.quadtree.rv.Dialog;
import info.quadtree.rv.Game;

public class Tech extends AlliedRobot {

	public Tech(float x, float y) {
		super(x, y);
		reset();
	}
	
	@Override
	public void reset() {
		hp = 10;
	}

	@Override
	public void startConversation() {
		Dialog d = new Dialog();
		
		if(Game.s.plot == 0)
		{
			d.addMessage("Tech: You should talk to the mayor first.");
			d.addMessage("Tech: He's in that huge building to the east of mine.");
			d.addMessage(Game.s.player.getName() + ": Right.");
		} else if(Game.s.plot == 1)
		{
			d.addMessage(Game.s.player.getName() + ": The mayor told me to talk to you.");
			d.addMessage("Tech: Right, yeah. I've been studying the attacks.");
			d.addMessage("Tech: Although he's never attacked the village directly, they attackers seem");
			d.addMessage("to have a leader.");
			d.addMessage("Tech: I've seen him around the cave to the west.");
			d.addMessage("Tech: Destroy him and bring me his core. That might help us figure out who");
			d.addMessage("is behind these attacks");
			d.addMessage(Game.s.player.getName() + ": You don't think its just him?");
			d.addMessage("Tech: No. Call it a hunch or whatever.");
			d.addMessage(Game.s.player.getName() + ": Okay.");
			Game.s.plot = 2;
		} else if(Game.s.plot == 2)
		{
			d.addMessage("Tech: Go to that cave to the west, destroy the commander and bring me the core.");
		} else if(Game.s.plot == 3)
		{
			d.addMessage(Game.s.player.getName() + ": I got the core. Here it is.");
			d.addMessage("Tech: Ah, excellent. Give me a moment to study it.");
			d.addMessage("Tech plugs the core into a nearby console");
			d.addMessage("Tech waits a moment.");
			d.addMessage("Tech: Interesting.");
			d.addMessage(Game.s.player.getName() + ": What?");
			d.addMessage("Tech: Now that I've studied this core's radio protocols, I can see another signal.");
			d.addMessage("Tech: I think it may be controlling all the attacking robots.");
			d.addMessage("Tech: Its coming from a cave to the east. Go in there, find the big boss, and eliminate him.");
			d.addMessage(Game.s.player.getName() + ": Just like that, eh?");
			
			Game.s.plot = 4;
		} else if(Game.s.plot == 4)
		{
			d.addMessage("Tech: Go to that cave to the east, and finish off the big boss.");
		} else if(Game.s.plot == 5)
		{
			d.addMessage("Tech: From INSIDE the town?! There must be a mole around here somewhere!");
		}
		else if(Game.s.plot == 6)
		{
			d.addMessage("Tech: Good luck!");
		} else if(Game.s.plot == 7)
		{
			d.addMessage("Tech: Thanks! You really saved us there.");
		}
		
		Game.s.actors.add(d);
		
		super.startConversation();
	}
	
	@Override
	protected String getTopPrefix() {
		return "toptech";
	}
}
