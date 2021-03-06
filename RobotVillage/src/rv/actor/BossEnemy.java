package rv.actor;

import rv.Game;
import rv.item.Core;
import rv.item.Turbo;
import rv.item.VulcanCannon;
import rv.projectile.Bolt;
import sv.vfx.Spark;

public class BossEnemy extends BasicEnemy {

	public BossEnemy(float x, float y) {
		super(x, y);
		reset();
	}
	
	@Override
	public void reset() {
		hp = 20;
	}

	@Override
	protected float getSize() {
		return 2.5f;
	}

	@Override
	public void notifyDestroyed() {
		
		Game.s.actors.add(new Core(body.getPosition().x, body.getPosition().y));
		
		Game.s.player.modMoney(700);
		
		for(int i=0;i<18;i++)
		{
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 1.3f, 0.015f, 15.f, "evilshot"));
		}
		
		if(Game.s.rand.nextInt(4) != 0)
			Game.s.actors.add(new VulcanCannon(body.getPosition().x, body.getPosition().y, false));
		else
			Game.s.actors.add(new Turbo(body.getPosition().x, body.getPosition().y, false));
		
		super.notifyDestroyed();
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
				shotCooldown = 8;
				Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing(), 15.f, "vulcanshot", true, 1.f/4.5f));
				Game.s.playAudio("vulcanfire");
			}
		}
		robotUpdate();
	}
}
