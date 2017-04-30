package info.quadtree.rv.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.graphics.SGF;

public class Shockbomb extends PhysicalActor implements ContactListener {

	public static final float DAMAGE = 1.51001f;

	boolean destroyed = false;

	Vector2 target;

	public Shockbomb(float x, float y, float angle, float speed, float tx, float ty) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		bd.type = BodyDef.BodyType.DynamicBody;

		body = Game.s.physicsWorld.createBody(bd);

		CircleShape cd = new CircleShape();
		cd.setRadius(0.4f);

		FixtureDef fxd = new FixtureDef();
		fxd.shape = cd;
		fxd.density = 1;

		Filter fd = new Filter();
		fd.categoryBits = 1 << 5;
		fd.maskBits = (1 << 3) | (1) | (1 << 7);

		body.createFixture(fxd).setFilterData(fd);

		body.setLinearVelocity(new Vector2((float) Math.cos(angle), (float) Math.sin(angle)).scl(8));

		body.setUserData(this);

		target = new Vector2(tx, ty);
	}

	@Override
	public void beginContact(Contact contact) {
		destroyed = true;
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

		for (float f = 0; f < 6.2f; f += 0.15f) {
			Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, f, 15.f, "shockwave", false, DAMAGE));
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
		SGF.getInstance().renderImage("shockbomblauncher", body.getPosition().x, body.getPosition().y, 0.8f, 0.8f, body.getAngle(), true);
		super.render();
	}

	@Override
	public void update() {
		if (body.getPosition().sub(target).len() < 1) {
			destroyed = true;
		}
		super.update();
	}
}
