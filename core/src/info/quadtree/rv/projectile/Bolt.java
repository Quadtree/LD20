package info.quadtree.rv.projectile;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
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

public class Bolt extends PhysicalActor implements ContactListener {
	
	String graphic;
	
	boolean destroyed = false;
	
	float damage;
	
	public Bolt(float x, float y, float angle, float speed, String graphic, boolean hostile, float damage)
	{
		BodyDef bd = new BodyDef();
		
		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		
		body = Game.s.physicsWorld.createBody(bd);
		
		CircleDef cd = new CircleDef();
		cd.radius = 0.2f;
		cd.density = 1;
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 5;
		fd.maskBits = 1 << (hostile ? 2 : 3) | (1) | (!hostile ? (1 << 7) : 0);
		
		body.createShape(cd).setFilterData(fd);
		
		body.setMassFromShapes();
		
		body.setLinearVelocity(new Vec2((float)Math.cos(angle), (float)Math.sin(angle)).mul(speed));
		
		body.setUserData(this);
		
		this.graphic = graphic;
		this.damage = damage;
	}

	@Override
	public void render() {
		SGF.getInstance().renderImage(graphic, body.getPosition().x, body.getPosition().y, -1/32.f, -1/32.f, body.getAngle(), true);
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
		
		if(o1 instanceof Harmable) ((Harmable) o1).takeDamage(damage);
		if(o2 instanceof Harmable) ((Harmable) o2).takeDamage(damage);
	}

	@Override
	public void notifyDestroyed() {
		
		if(!graphic.equals("shockwave"))
			for(int i=0;i<Math.min(4 * damage, 16);i++)
			{
				Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.35f, 0.015f, 7.f, graphic));
			}
		
		Game.s.playAudio("bolthit");
		
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
