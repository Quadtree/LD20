package info.quadtree.rv.projectile;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import info.quadtree.rv.Game;
import info.quadtree.rv.actor.PhysicalActor;
import sgf.SGF;

public class BreakerShot extends PhysicalActor {
	public BreakerShot(float x, float y, float angle)
	{
		BodyDef bd = new BodyDef();
		
		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		bd.linearDamping = 0.4f;
		
		body = Game.s.physicsWorld.createBody(bd);
		
		CircleDef cd = new CircleDef();
		cd.radius = 0.25f;
		cd.density = 10;
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 5;
		fd.maskBits = (1 << 3) | (1) | (1 << 7);
		
		body.createShape(cd).setFilterData(fd);
		
		body.setMassFromShapes();
		
		body.setLinearVelocity(new Vec2((float)Math.cos(angle), (float)Math.sin(angle)).mul(24));
		
		body.setUserData(this);
	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("breakershot", body.getPosition().x, body.getPosition().y, 0.5f, 0.5f, body.getAngle(), true);
		super.render();
	}

	@Override
	public boolean keep() {
		return body.getLinearVelocity().length() > 2.5f;
	}

}
