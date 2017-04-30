package info.quadtree.rv.projectile;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.Harmable;
import info.quadtree.rv.actor.PhysicalActor;
import sgf.SGF;
import sv.vfx.Spark;

public class Missile extends PhysicalActor implements ContactListener{
	
	boolean destroyed = false;

	public Missile(float x, float y, float angle) {
		BodyDef bd = new BodyDef();
		
		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		
		body = Game.s.physicsWorld.createBody(bd);
		
		PolygonDef pd = new PolygonDef();
		pd.setAsBox(.5f, 0.2f);
		pd.density = 1;
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 7;
		fd.maskBits = 1 << 2 | (1) | 1 << 5;
		
		body.createShape(pd).setFilterData(fd);
		
		body.setMassFromShapes();
		
		
		
		body.setUserData(this);
	}

	@Override
	public void update() {
		body.setLinearVelocity(new Vec2((float)Math.cos(body.getAngle()), (float)Math.sin(body.getAngle())).mul(7.5f));
		
		Vec2 pLeft = body.getPosition().add(new Vec2((float)Math.cos(body.getAngle() - 0.2f), (float)Math.sin(body.getAngle() - 0.2f)));
		Vec2 pCenter = body.getPosition().add(new Vec2((float)Math.cos(body.getAngle()), (float)Math.sin(body.getAngle())));
		Vec2 pRight = body.getPosition().add(new Vec2((float)Math.cos(body.getAngle() + 0.2f), (float)Math.sin(body.getAngle() + 0.2f)));
		
		float dLeft = pLeft.sub(Game.s.player.getPosition()).lengthSquared();
		float dCenter = pCenter.sub(Game.s.player.getPosition()).lengthSquared();
		float dRight = pRight.sub(Game.s.player.getPosition()).lengthSquared();
		
		if(dLeft < dCenter && dLeft < dRight)
			body.setAngularVelocity(-1);
		else if(dRight < dCenter && dRight < dLeft)
			body.setAngularVelocity(1);
		else
			body.setAngularVelocity(0);
		
		super.update();
	}
	
	
	
	@Override
	public void render() {
		SGF.getInstance().renderImage("missile", body.getPosition().x, body.getPosition().y, 1.f, 0.4f, body.getAngle(), true);
		super.render();
	}

	@Override
	public boolean keep() {
		return !destroyed;
	}

	@Override
	public void add(ContactPoint point) {
		destroyed = true;
		
		Object o1 = point.shape1.getBody().getUserData();
		Object o2 = point.shape2.getBody().getUserData();
		
		if(o1 instanceof Harmable) ((Harmable) o1).takeDamage(2);
		if(o2 instanceof Harmable) ((Harmable) o2).takeDamage(2);
	}

	@Override
	public void notifyDestroyed() {
		
		for(int i=0;i<8;i++)
		{
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 1.f, 0.015f, 2.5f, "firespark"));
		}
		
		super.notifyDestroyed();
	}

	@Override
	public void persist(ContactPoint point) {
	}

	@Override
	public void remove(ContactPoint point) {
	}

	@Override
	public void result(ContactResult point) {
		
	}

}
