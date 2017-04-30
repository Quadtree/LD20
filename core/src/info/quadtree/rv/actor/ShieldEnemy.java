package info.quadtree.rv.actor;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import info.quadtree.rv.Game;
import info.quadtree.rv.graphics.SGF;
import info.quadtree.rv.item.Shield;

public class ShieldEnemy extends BasicEnemy {

	public ShieldEnemy(float x, float y) {
		super(x, y);

		CircleShape cd = new CircleShape();
		cd.setRadius(2.f);

		FixtureDef fxd = new FixtureDef();
		fxd.isSensor = true;
		fxd.shape = cd;

		Fixture shieldFixture = body.createFixture(fxd);

		Filter fd = new Filter();
		fd.categoryBits = 1 << 3;
		fd.maskBits = 0xffffffff;

		shieldFixture.setFilterData(fd);

		reset();
	}

	@Override
	public void notifyDestroyed() {
		if (Game.s.rand.nextInt(15) == 0)
			Game.s.actors.add(new Shield(body.getPosition().x, body.getPosition().y, false));

		Game.s.player.modMoney(45);

		super.notifyDestroyed();
	}

	@Override
	public void render() {
		super.render();

		SGF.getInstance().renderImage("bigshield", getPosition().x, getPosition().y, 4, 4, 0, true);
	}

	@Override
	public void reset() {
		hp = 15;
	}
}
