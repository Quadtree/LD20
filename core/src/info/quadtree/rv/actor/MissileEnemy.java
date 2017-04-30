package info.quadtree.rv.actor;

import info.quadtree.rv.Game;
import info.quadtree.rv.item.DevastatorCannon;
import info.quadtree.rv.projectile.Missile;

public class MissileEnemy extends BasicEnemy {

	public MissileEnemy(float x, float y) {
		super(x, y);
	}

	@Override
	protected String getTopPrefix() {
		return "topmissile";
	}

	@Override
	public void notifyDestroyed() {
		if (Game.s.rand.nextInt(25) == 0)
			Game.s.actors.add(new DevastatorCannon(body.getPosition().x, body.getPosition().y, false));

		Game.s.player.modMoney(30);

		super.notifyDestroyed();
	}

	@Override
	public void update() {

		if (body.getPosition().sub(Game.s.player.getPosition()).len2() > 32 * 32)
			return;

		if (shotCooldown > 0)
			shotCooldown--;

		aim(Game.s.player.getPosition());

		if (testLOSTo(Game.s.player.getPosition())) {
			if (shotCooldown <= 0) {
				shotCooldown = 120;
				Game.s.actors.add(new Missile(getPosition().x, getPosition().y, getAimFacing()));
			}
		}
		robotUpdate();
	}
}
