package info.quadtree.rv.actor;

import info.quadtree.rv.Dialog;
import info.quadtree.rv.Game;

public class Villager extends AlliedRobot {

	public Villager(float x, float y) {
		super(x, y);
		reset();
	}
	
	@Override
	public void reset() {
		hp = 5;
	}

	@Override
	public void startConversation() {
		
		Dialog d = new Dialog();
		d.addMessage("Villager: Welcome to our humble village.");
		d.addMessage(Game.s.player.getName() + ": Always glad to help!");
		
		Game.s.actors.add(d);
		
		super.startConversation();
	}
}
