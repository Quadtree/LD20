package info.quadtree.rv.actor;

import info.quadtree.rv.Dialog;
import info.quadtree.rv.Game;
import info.quadtree.rv.item.RepairCartridge;
import info.quadtree.rv.item.Shield;
import info.quadtree.rv.item.Turbo;

public class Shopkeeper2 extends ShopkeeperRobot {

	RepairCartridge rc;
	
	public Shopkeeper2(float x, float y) {
		super(x, y);
		reset();
		
		rc = new RepairCartridge(x - 1.4f, y + 1.4f, true, 2);
		
		Game.s.actors.add(rc);
		
		Game.s.actors.add(new Shield(x, y + 2.f, true));
		Game.s.actors.add(new Turbo(x + 1.4f, y + 1.4f, true));
	}
	
	@Override
	public void reset() {
		hp = 15;
	}

	@Override
	public void startConversation() {
		Dialog d = new Dialog();
		
		if(Game.s.plot == 6)
		{
			d.addMessage("Shopkeeper Gamma: ARGH! I knew this would happen!");
		} else if(Game.s.plot == 5)
		{
			d.addMessage(Game.s.player.getName() + ": The town is under attack! Give me all your wares so I can defend!");
			d.addMessage("Shopkeeper Gamma: I don't see anyone. What, are you just trying to get free stuff?");
		} else {
			d.addMessage("Shopkeeper Gamma: Have a look. Seems like noone ever comes here any more.");
			d.addMessage("Shopkeeper Gamma: Must be all the violence.");
		}
		
		Game.s.actors.add(d);
	}

	@Override
	public void update() {
		if(Game.s.plot < 6)
			rc.setCharges(2);
		
		super.update();
	}
}
