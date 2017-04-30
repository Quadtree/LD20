package info.quadtree.rv.vfx;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.graphics.SGF;

public class Spark extends PhysicalActor {

	float decay;
	String graphic;

	float size;

	public Spark(float x, float y, float size, float decay, float power, String graphic) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;

		body = Game.s.physicsWorld.createBody(bd);

		CircleDef cd = new CircleDef();
		cd.radius = 0.1f;
		cd.density = 1;

		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 6;
		fd.maskBits = 1;

		body.createShape(cd).setFilterData(fd);

		body.setMassFromShapes();

		body.setUserData(this);

		power *= Game.s.rand.nextFloat();
		float ang = Game.s.rand.nextFloat() * 6.2f;

		body.setLinearVelocity(new Vec2((float) Math.cos(ang) * power, (float) Math.sin(ang) * power));

		this.size = size;
		this.decay = decay;
		this.graphic = graphic;
	}

	@Override
	public boolean keep() {
		return size > 0;
	}

	@Override
	public void render() {
		SGF.getInstance().renderImage(graphic, body.getPosition().x, body.getPosition().y, size, size, Game.s.rand.nextFloat() * 6.2f, true);
	}

	@Override
	public void update() {
		size -= decay;
		super.update();
	}
}
