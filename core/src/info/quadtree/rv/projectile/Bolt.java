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
import info.quadtree.rv.Spark;
import info.quadtree.rv.actor.Harmable;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.graphics.SGF;

public class Bolt extends PhysicalActor implements ContactListener {

	float damage;

	boolean destroyed = false;

	String graphic;

	public Bolt(float x, float y, float angle, float speed, String graphic, boolean hostile, float damage) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;

		body = Game.s.physicsWorld.createBody(bd);

		CircleShape cd = new CircleShape();
		cd.setRadius(0.2f);

		FixtureDef fxd = new FixtureDef();
		fxd.shape = cd;
		fxd.density = 1;

		Filter fd = new Filter();
		fd.categoryBits = 1 << 5;
		fd.maskBits = (short) (1 << (hostile ? 2 : 3) | (1) | (!hostile ? (1 << 7) : 0));

		body.createFixture(fxd).setFilterData(fd);

		body.setLinearVelocity(new Vector2((float) Math.cos(angle), (float) Math.sin(angle)).scl(speed));

		body.setUserData(this);

		this.graphic = graphic;
		this.damage = damage;
	}

	@Override
	public void beginContact(Contact contact) {
		destroyed = true;

		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Harmable)
			((Harmable) o1).takeDamage(damage);
		if (o2 instanceof Harmable)
			((Harmable) o2).takeDamage(damage);
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

		if (!graphic.equals("shockwave"))
			for (int i = 0; i < Math.min(4 * damage, 16); i++) {
				Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.35f, 0.015f, 7.f, graphic));
			}

		Game.s.playAudio("bolthit");

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
		SGF.getInstance().renderImage(graphic, body.getPosition().x, body.getPosition().y, -1 / 32.f, -1 / 32.f, body.getAngle(), true);
		super.render();
	}
}
