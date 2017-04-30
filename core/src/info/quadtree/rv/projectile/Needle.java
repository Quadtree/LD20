package info.quadtree.rv.projectile;

import java.util.HashSet;
import java.util.Set;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.ShapeType;
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

public class Needle extends PhysicalActor implements ContactListener {
	
	boolean destroyed = false;
	
	Set<Object> alreadyHit;
	
	boolean causeExplosion = false;
	
	public Needle(float x, float y, float angle)
	{
		BodyDef bd = new BodyDef();
		
		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		
		body = Game.s.physicsWorld.createBody(bd);
		
		CircleDef cd = new CircleDef();
		cd.radius = 0.2f;
		cd.density = 1;
		cd.isSensor = true;
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 5;
		fd.maskBits = (1 << 3) | (1) | (1 << 7);
		
		body.createShape(cd).setFilterData(fd);
		
		body.setMassFromShapes();
		
		body.setLinearVelocity(new Vec2((float)Math.cos(angle), (float)Math.sin(angle)).mul(18));
		
		body.setUserData(this);
		
		alreadyHit = new HashSet<Object>();
	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("needle", body.getPosition().x, body.getPosition().y, -1/32.f, -1/32.f, body.getAngle(), true);
		super.render();
	}

	@Override
	public boolean keep() {
		return !destroyed;
	}

	@Override
	public void add(ContactPoint point) {
		if(point.shape1.m_density < 0.01f && point.shape1.getType() == ShapeType.POLYGON_SHAPE)
			destroyed = true;
		
		Object o1 = point.shape1.getBody().getUserData();
		Object o2 = point.shape2.getBody().getUserData();
		
		if(o1 instanceof Harmable && !alreadyHit.contains(o1)){
			((Harmable) o1).takeDamage(3);
			causeExplosion = true;
		}
		if(o2 instanceof Harmable && !alreadyHit.contains(o2)) {
			((Harmable) o2).takeDamage(3);
			causeExplosion = true;
		}
		
		alreadyHit.add(o1);
		alreadyHit.add(o2);
	}
	
	

	@Override
	public void update() {
		if(causeExplosion)
		{
			for(int i=0;i<8;i++)
			{
				Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.35f, 0.015f, 7.f, "needle"));
			}
		}
		super.update();
	}

	@Override
	public void notifyDestroyed() {
		
		for(int i=0;i<8;i++)
		{
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.35f, 0.015f, 7.f, "needle"));
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
