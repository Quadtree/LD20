package info.quadtree.rv.actor;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;

import info.quadtree.rv.Game;

public class AlliedRobot extends Robot implements ContactListener {

	protected boolean lookAtPlayer = true;

	public AlliedRobot(float x, float y) {
		super(x, y);

		// we're going to make this allied bot static so the player can't push
		// them around
		body.destroyShape(body.getShapeList());

		CircleShape cd = new CircleShape();
		cd.setRadius(0.4f);

		body.createShape(cd);

		body.setMassFromShapes();

		Filter fd = new Filter();
		fd.categoryBits = 1 << 2;
		fd.maskBits = 0xffffffff;

		body.getShapeList().setFilterData(fd);
	}

	@Override
	public void add(ContactPoint point) {
		if (Game.s.dialogUp)
			return;
		Object o1 = point.shape1.getBody().getUserData();
		Object o2 = point.shape2.getBody().getUserData();

		if (o1 instanceof Player || o2 instanceof Player)
			startConversation();
	}

	@Override
	public void persist(ContactPoint point) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(ContactPoint point) {
		// TODO Auto-generated method stub

	}

	@Override
	public void result(ContactResult point) {
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
