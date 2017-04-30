package info.quadtree.rv.actor;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

import info.quadtree.rv.Game;
import info.quadtree.rv.Spark;
import info.quadtree.rv.item.RepairCartridge;
import info.quadtree.rv.projectile.Bolt;

public class BasicEnemy extends Robot {

	int shotCooldown = 0;

	public BasicEnemy(float x, float y) {
		super(x, y);

		Filter fd = new Filter();
		fd.categoryBits = 1 << 3;
		fd.maskBits = 0xffffffff;

		for (Fixture f : body.getFixtureList())
			f.setFilterData(fd);

		reset();
	}

	@Override
	protected String getTopPrefix() {
		return "topbad";
	}

	@Override
	public void notifyDestroyed() {
		Game.s.player.modMoney(20);

		if (Game.s.rand.nextInt(10) == 0)
			Game.s.actors.add(new RepairCartridge(body.getPosition().x, body.getPosition().y, false, 1));

		for (int i = 0; i < 12; i++) {
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.9f, 0.015f, 7.f, "evilshot"));
		}

		super.notifyDestroyed();
	}

	@Override
	public void reset() {
		hp = 3;
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
				setMuzzleBlastTimer(3);
				shotCooldown = 40;
				Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing(), 3.5f, "evilshot", true, 1.f));
				Game.s.playAudio("boltfire");
			}
		}

		super.update();
	}
}
