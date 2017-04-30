package info.quadtree.rv.actor;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import info.quadtree.rv.Game;

public class PhysicalActor extends Actor {
	protected Body body;
	
	public Vec2 getPosition()
	{
		return body.getPosition();
	}
	
	public Body getBody()
	{
		return body;
	}

	@Override
	public void notifyDestroyed() {
		if(body != null) Game.s.physicsWorld.destroyBody(body);
		super.notifyDestroyed();
	}
}
