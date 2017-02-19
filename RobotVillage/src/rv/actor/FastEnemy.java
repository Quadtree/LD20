package rv.actor;

import rv.Game;
import rv.item.Turbo;

public class FastEnemy extends BasicEnemy{

	public FastEnemy(float x, float y) {
		super(x, y);
	}

	@Override
	public void update() {
		
		if(body.getPosition().sub(Game.s.player.getPosition()).lengthSquared() > 32*32) return;
		
		if(testLOSTo(Game.s.player.getPosition()))
		{
			if(Game.s.rand.nextFloat() < 0.01f)
			{
				if(!left)
				{
					left = true;
					right = false;
				} else
				{
					left = false;
					right = true;
				}
			}
		} else {
			left = false;
			right = false;
		}
		super.update();
	}
	
	@Override
	public void notifyDestroyed() {
		if(Game.s.rand.nextInt(15) == 0) Game.s.actors.add(new Turbo(body.getPosition().x, body.getPosition().y, false));
			
		Game.s.player.modMoney(20);
			
		super.notifyDestroyed();
	}
}
