package info.quadtree.rv.actor;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import info.quadtree.rv.Game;
import info.quadtree.rv.graphics.SGF;

public class PlotBarrier extends PhysicalActor {
	public PlotBarrier(float x, float y) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.fixedRotation = true;

		body = Game.s.physicsWorld.createBody(bd);

		CircleShape cd = new CircleShape();
		cd.setRadius(2f);

		body.createFixture(cd, 1);
	}

	@Override
	public boolean keep() {
		return Game.s.plot < 4;
	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("bigshield", body.getPosition().x, body.getPosition().y, 4, 4, 0, true);
		super.render();
	}

}
