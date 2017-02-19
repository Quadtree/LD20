package rv.actor;

import rv.Dialog;
import rv.Game;
import rv.item.BreakerCannon;
import rv.item.ShockbombLauncher;
import rv.item.SpreadBlaster;

public class Shopkeeper3 extends ShopkeeperRobot {

	public Shopkeeper3(float x, float y) {
		super(x, y);
		reset();
		
		Game.s.actors.add(new SpreadBlaster(x - 1.4f, y + 1.4f, true));
		Game.s.actors.add(new BreakerCannon(x, y + 2.f, true));
		Game.s.actors.add(new ShockbombLauncher(x + 1.4f, y + 1.4f, true));
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
			d.addMessage("Shopkeeper Beta: Take whatever you want! Save the town!");
		} else if(Game.s.plot == 5){
			d.addMessage(Game.s.player.getName() + ": The town is under attack! Give me all your wares so I can defend!");
			d.addMessage("Shopkeeper Beta: I don't see anyone. What, are you just trying to get free stuff?");
		} else {
			d.addMessage("Shopkeeper Beta: Take a look! You won't find better prices anywhere!");
		}
		
		Game.s.actors.add(d);
	}
}
