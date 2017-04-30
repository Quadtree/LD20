package info.quadtree.rv.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import info.quadtree.rv.Game;
import info.quadtree.rv.graphics.SGF;
import info.quadtree.rv.projectile.Shockbomb;

public class Robot extends PhysicalActor implements Harmable {
	Vector2 aimPoint;

	float baseFacing;

	boolean forward, backward, left, right;

	float hp;

	float moveSinceLastTread;

	int muzzleBlastTimer = 0;

	int shockImmune = 0;

	String tread = "A";

	public Robot(float x, float y) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.fixedRotation = true;

		body = Game.s.physicsWorld.createBody(bd);

		CircleShape cd = new CircleShape();
		cd.setRadius(0.4f);

		body.createFixture(cd, 1);

		aimPoint = body.getPosition().add(new Vector2(100, 0));

		body.setUserData(this);
	}

	protected void aim(Vector2 target) {
		aimPoint = target;
	}

	void destroyAllShapes() {
		Array<Fixture> fxs = body.getFixtureList();

		for (Fixture f : fxs)
			body.destroyFixture(f);
	}

	public float getAimFacing() {
		return (float) Math.atan2(aimPoint.y - body.getPosition().y, aimPoint.x - body.getPosition().x);
	}

	public Vector2 getAimPoint() {
		return aimPoint;
	}

	public String getFacingImageNumber(float facing) {
		if (facing < 0)
			facing += (float) (Math.PI * 2);
		return "00" + (((int) (facing * (16.f / Math.PI)) % 32) + 32);
	}

	public float getHp() {
		return hp;
	}

	public Vector2 getMovePoint() {
		return aimPoint;
	}

	protected float getSize() {
		return 1.45f;
	}

	public float getSpeedMod() {
		return 1;
	}

	protected String getTopPrefix() {
		return "top";
	}

	@Override
	public boolean keep() {
		return hp > 0;
	}

	@Override
	public void notifyDestroyed() {
		Game.s.playAudio("robotdie");
		super.notifyDestroyed();
	}

	@Override
	public void render() {

		if (body.getLinearVelocity().len() > 0.1f)
			baseFacing = -(float) Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x);

		SGF.getInstance().renderImage("Base" + tread + getFacingImageNumber(baseFacing), body.getPosition().x, body.getPosition().y, getSize(), getSize(), 0, true);

		float aimFacing = -(float) Math.atan2(aimPoint.y - body.getPosition().y, aimPoint.x - body.getPosition().x);

		SGF.getInstance().renderImage(getTopPrefix() + getFacingImageNumber(aimFacing), body.getPosition().x, body.getPosition().y, getSize(), getSize(), 0, true);
		if (muzzleBlastTimer > 0)
			SGF.getInstance().renderImage("muzzleblast" + getFacingImageNumber(aimFacing), body.getPosition().x, body.getPosition().y, getSize(), getSize(), 0, true);

		super.render();
	}

	protected void robotUpdate() {
		body.setLinearVelocity(body.getLinearVelocity().scl(0.8f));

		// forward backward movement

		int fb = 0;

		if (forward && !backward)
			fb = 1;
		if (!forward && backward)
			fb = -1;

		Vector2 force = getMovePoint().sub(body.getPosition());
		force.nor();

		body.applyLinearImpulse(force.scl(fb).scl(getSpeedMod()), new Vector2(), true);

		// strafing

		int lr = 0;

		if (left && !right)
			lr = -1;
		if (!left && right)
			lr = 1;

		float aimFacing = (float) Math.atan2(getMovePoint().y - body.getPosition().y, getMovePoint().x - body.getPosition().x);

		force = new Vector2((float) Math.cos(aimFacing + 1.6f), (float) Math.sin(aimFacing + 1.6f));

		body.applyLinearImpulse(force.scl(lr).scl(getSpeedMod()), new Vector2(), true);

		if (muzzleBlastTimer > 0)
			muzzleBlastTimer--;

		moveSinceLastTread += body.getLinearVelocity().len();

		if (moveSinceLastTread > 0.5f) {
			if (tread.equals("A"))
				tread = "B";
			else
				tread = "A";

			moveSinceLastTread = 0;
		}

		if (shockImmune > 0)
			shockImmune--;
	}

	void setAllFilters(Filter filter) {
		for (Fixture f : body.getFixtureList())
			f.setFilterData(filter);
	}

	public void setMuzzleBlastTimer(int muzzleBlastTimer) {
		this.muzzleBlastTimer = muzzleBlastTimer;
	}

	@Override
	public void takeDamage(float amount) {
		if (amount == Shockbomb.DAMAGE) {
			if (shockImmune > 0)
				return;
			else
				shockImmune = 120;
		}
		hp -= amount;
		// System.out.println(this + " took " + amount + " damage!");
	}

	public boolean testLOSTo(Vector2 trg) {
		Vector2 cur = new Vector2(body.getPosition());
		Vector2 delta = trg.sub(cur);
		delta = delta.scl(0.3f / delta.len());

		while (Math.abs(cur.x - trg.x) > 1 || Math.abs(cur.y - trg.y) > 1) {
			if (!Game.s.map.isPassable((int) (cur.x + 0.5f), (int) (cur.y + 0.5f)))
				return false;
			cur.x += delta.x;
			cur.y += delta.y;
		}

		return true;
	}

	@Override
	public void update() {
		robotUpdate();

		super.update();
	}
}
