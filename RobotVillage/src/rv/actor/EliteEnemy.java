package rv.actor;

import rv.Game;
import rv.item.BoltGun;
import rv.projectile.Bolt;

public class EliteEnemy extends BasicEnemy {

	public EliteEnemy(float x, float y) {
		super(x, y);
		reset();
	}
	
	@Override
	public void reset() {
		hp = 8;
	}

	@Override
	public void update() {
		
		if(body.getPosition().sub(Game.s.player.getPosition()).lengthSquared() > 32*32) return;
		
		if(shotCooldown > 0) shotCooldown--;
		
		aim(Game.s.player.getPosition());
		
		if(testLOSTo(Game.s.player.getPosition()))
		{
			if(shotCooldown <= 0)
			{
				setMuzzleBlastTimer(3);
				shotCooldown = 20;
				Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing(), 4.5f, "evilshot", true, 1.f));
				Game.s.playAudio("boltfire");
			}
		}
		robotUpdate();
	}
	
	@Override
	public void notifyDestroyed() {
		if(Game.s.rand.nextInt(4) == 0) Game.s.actors.add(new BoltGun(body.getPosition().x, body.getPosition().y, false));
			
		Game.s.player.modMoney(60);
			
		super.notifyDestroyed();
	}
}
