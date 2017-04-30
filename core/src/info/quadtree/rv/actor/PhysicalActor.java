package info.quadtree.rv.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import info.quadtree.rv.Game;

public class PhysicalActor extends Actor {
	protected Body body;

	public Body getBody() {
		return body;
	}

	public Vector2 getPosition() {
		return new Vector2(body.getPosition());
	}

	@Override
	public void notifyDestroyed() {
		if (body != null)
			Game.s.physicsWorld.destroyBody(body);
		super.notifyDestroyed();
	}
}
