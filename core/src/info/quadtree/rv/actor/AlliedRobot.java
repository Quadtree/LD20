package info.quadtree.rv.actor;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import info.quadtree.rv.Game;

public class AlliedRobot extends Robot implements ContactListener {

	protected boolean lookAtPlayer = true;

	public AlliedRobot(float x, float y) {
		super(x, y);

		// we're going to make this allied bot static so the player can't push
		// them around
		for (Fixture f : body.getFixtureList())
			body.destroyFixture(f);

		CircleShape cd = new CircleShape();
		cd.setRadius(0.4f);

		body.createFixture(cd, 1);

		Filter fd = new Filter();
		fd.categoryBits = 1 << 2;
		fd.maskBits = 0xffffffff;

		for (Fixture f : body.getFixtureList())
			f.setFilterData(fd);
	}

	@Override
	public void beginContact(Contact contact) {
		if (Game.s.dialogUp)
			return;
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Player || o2 instanceof Player)
			startConversation();
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	public void startConversation() {
	}

	@Override
	public void update() {

		if (lookAtPlayer && testLOSTo(Game.s.player.getPosition())) {
			aim(Game.s.player.getPosition());
		}

		super.update();
	}
}
