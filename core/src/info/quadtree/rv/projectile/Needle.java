package info.quadtree.rv.projectile;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape.Type;

import info.quadtree.rv.Game;
import info.quadtree.rv.Spark;
import info.quadtree.rv.actor.Harmable;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.graphics.SGF;

public class Needle extends PhysicalActor implements ContactListener {

	Set<Object> alreadyHit;

	boolean causeExplosion = false;

	boolean destroyed = false;

	public Needle(float x, float y, float angle) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.angle = -angle;
		bd.type = BodyDef.BodyType.DynamicBody;

		body = Game.s.physicsWorld.createBody(bd);

		CircleShape cd = new CircleShape();
		cd.setRadius(0.2f);

		FixtureDef fxd = new FixtureDef();
		fxd.shape = cd;
		fxd.density = 1;
		fxd.isSensor = true;

		Filter fd = new Filter();
		fd.categoryBits = 1 << 5;
		fd.maskBits = (1 << 3) | (1) | (1 << 7);

		body.createFixture(fxd).setFilterData(fd);

		body.setLinearVelocity(new Vector2((float) Math.cos(angle), (float) Math.sin(angle)).scl(18));

		body.setUserData(this);

		alreadyHit = new HashSet<Object>();
	}

	@Override
	public void beginContact(Contact point) {
		if (point.getFixtureA().getDensity() < 0.01f && point.getFixtureA().getType() == Type.Polygon)
			destroyed = true;

		Object o1 = point.getFixtureA().getBody().getUserData();
		Object o2 = point.getFixtureB().getBody().getUserData();

		if (o1 instanceof Harmable && !alreadyHit.contains(o1)) {
			((Harmable) o1).takeDamage(3);
			causeExplosion = true;
		}
		if (o2 instanceof Harmable && !alreadyHit.contains(o2)) {
			((Harmable) o2).takeDamage(3);
			causeExplosion = true;
		}

		alreadyHit.add(o1);
		alreadyHit.add(o2);
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keep() {
		return !destroyed;
	}

	@Override
	public void notifyDestroyed() {

		for (int i = 0; i < 8; i++) {
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.35f, 0.015f, 7.f, "needle"));
		}

		super.notifyDestroyed();
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("needle", body.getPosition().x, body.getPosition().y, -1 / 32.f, -1 / 32.f, body.getAngle(), true);
		super.render();
	}

	@Override
	public void update() {
		if (causeExplosion) {
			for (int i = 0; i < 8; i++) {
				Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.35f, 0.015f, 7.f, "needle"));
			}
		}
		super.update();
	}
}
