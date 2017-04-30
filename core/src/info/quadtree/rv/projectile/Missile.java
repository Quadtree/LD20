package info.quadtree.rv.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import info.quadtree.rv.Game;
import info.quadtree.rv.Spark;
import info.quadtree.rv.actor.Harmable;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.graphics.SGF;

public class Missile extends PhysicalActor implements ContactListener {

	boolean destroyed = false;

	public Missile(float x, float y, float angle) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		bd.type = BodyDef.BodyType.DynamicBody;

		body = Game.s.physicsWorld.createBody(bd);

		PolygonShape pd = new PolygonShape();
		pd.setAsBox(.5f, 0.2f);

		FixtureDef fxd = new FixtureDef();
		fxd.density = 1;

		Filter fd = new Filter();
		fd.categoryBits = 1 << 7;
		fd.maskBits = 1 << 2 | (1) | 1 << 5;

		body.createFixture(fxd).setFilterData(fd);

		body.setUserData(this);
	}

	@Override
	public void beginContact(Contact point) {
		destroyed = true;

		Object o1 = point.getFixtureA().getBody().getUserData();
		Object o2 = point.getFixtureB().getBody().getUserData();

		if (o1 instanceof Harmable)
			((Harmable) o1).takeDamage(2);
		if (o2 instanceof Harmable)
			((Harmable) o2).takeDamage(2);
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
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 1.f, 0.015f, 2.5f, "firespark"));
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
		SGF.getInstance().renderImage("missile", body.getPosition().x, body.getPosition().y, 1.f, 0.4f, body.getAngle(), true);
		super.render();
	}

	@Override
	public void update() {
		body.setLinearVelocity(new Vector2((float) Math.cos(body.getAngle()), (float) Math.sin(body.getAngle())).scl(7.5f));

		Vector2 pLeft = body.getPosition().add(new Vector2((float) Math.cos(body.getAngle() - 0.2f), (float) Math.sin(body.getAngle() - 0.2f)));
		Vector2 pCenter = body.getPosition().add(new Vector2((float) Math.cos(body.getAngle()), (float) Math.sin(body.getAngle())));
		Vector2 pRight = body.getPosition().add(new Vector2((float) Math.cos(body.getAngle() + 0.2f), (float) Math.sin(body.getAngle() + 0.2f)));

		float dLeft = pLeft.sub(Game.s.player.getPosition()).len2();
		float dCenter = pCenter.sub(Game.s.player.getPosition()).len2();
		float dRight = pRight.sub(Game.s.player.getPosition()).len2();

		if (dLeft < dCenter && dLeft < dRight)
			body.setAngularVelocity(-1);
		else if (dRight < dCenter && dRight < dLeft)
			body.setAngularVelocity(1);
		else
			body.setAngularVelocity(0);

		super.update();
	}

}
