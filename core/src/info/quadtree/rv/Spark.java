package info.quadtree.rv;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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

		CircleShape cd = new CircleShape();
		cd.setRadius(0.1f);

		FixtureDef fxd = new FixtureDef();
		fxd.shape = cd;
		fxd.density = 1;

		Filter fd = new Filter();
		fd.categoryBits = 1 << 6;
		fd.maskBits = 1;

		body.createFixture(fxd).setFilterData(fd);

		body.setUserData(this);

		power *= Game.s.rand.nextFloat();
		float ang = Game.s.rand.nextFloat() * 6.2f;

		body.setLinearVelocity(new Vector2((float) Math.cos(ang) * power, (float) Math.sin(ang) * power));

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
