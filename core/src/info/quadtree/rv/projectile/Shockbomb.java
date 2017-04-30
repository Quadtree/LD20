package info.quadtree.rv.projectile;

import com.badlogic.gdx.physics.box2d.collision.FilterData;
import com.badlogic.gdx.physics.box2d.collision.shapes.CircleDef;
import com.badlogic.gdx.physics.box2d.common.Vector2;
import com.badlogic.gdx.physics.box2d.dynamics.BodyDef;
import com.badlogic.gdx.physics.box2d.dynamics.ContactListener;
import com.badlogic.gdx.physics.box2d.dynamics.contacts.ContactPoint;
import com.badlogic.gdx.physics.box2d.dynamics.contacts.ContactResult;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.graphics.SGF;

public class Shockbomb extends PhysicalActor implements ContactListener {
	
	public static final float DAMAGE = 1.51001f;
	
	boolean destroyed = false;
	
	Vector2 target;
	
	public Shockbomb(float x, float y, float angle, float speed, float tx, float ty)
	{
		BodyDef bd = new BodyDef();
		
		bd.position.x = x;
		bd.position.y = y;
		bd.angle = angle;
		
		body = Game.s.physicsWorld.createBody(bd);
		
		CircleDef cd = new CircleDef();
		cd.radius = 0.4f;
		cd.density = 1;
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 5;
		fd.maskBits = (1 << 3) | (1) | (1 << 7);
		
		body.createShape(cd).setFilterData(fd);
		
		body.setMassFromShapes();
		
		body.setLinearVelocity(new Vector2((float)Math.cos(angle), (float)Math.sin(angle)).mul(8));
		
		body.setUserData(this);
		
		target = new Vector2(tx,ty);
	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("shockbomblauncher", body.getPosition().x, body.getPosition().y, 0.8f, 0.8f, body.getAngle(), true);
		super.render();
	}

	@Override
	public boolean keep() {
		return !destroyed;
	}

	@Override
	public void update() {
		if(body.getPosition().sub(target).length() < 1)
		{
			destroyed = true;
		}
		super.update();
	}

	@Override
	public void add(ContactPoint point) {
		destroyed = true;
	}

	@Override
	public void notifyDestroyed() {
		
		for(float f=0;f<6.2f;f += 0.15f)
		{
			Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, f, 15.f, "shockwave", false, DAMAGE));
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
