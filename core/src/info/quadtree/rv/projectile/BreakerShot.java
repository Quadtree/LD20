package info.quadtree.rv.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.graphics.SGF;

public class BreakerShot extends PhysicalActor {
	public BreakerShot(float x, float y, float angle) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		bd.linearDamping = 0.4f;
		bd.type = BodyDef.BodyType.DynamicBody;

		body = Game.s.physicsWorld.createBody(bd);

		CircleShape cd = new CircleShape();
		cd.setRadius(0.25f);

		FixtureDef fxd = new FixtureDef();
		fxd.density = 10;
		fxd.shape = cd;

		Filter fd = new Filter();
		fd.categoryBits = 1 << 5;
		fd.maskBits = (1 << 3) | (1) | (1 << 7);

		body.createFixture(fxd).setFilterData(fd);

		body.setLinearVelocity(new Vector2((float) Math.cos(angle), (float) Math.sin(angle)).scl(24));

		body.setUserData(this);
	}

	@Override
	public boolean keep() {
		return body.getLinearVelocity().len() > 2.5f;
	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("breakershot", body.getPosition().x, body.getPosition().y, 0.5f, 0.5f, body.getAngle(), true);
		super.render();
	}

}
