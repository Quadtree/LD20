package info.quadtree.rv.actor;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import info.quadtree.rv.Game;
import info.quadtree.rv.projectile.Shockbomb;
import info.quadtree.rv.graphics.SGF;

public class Robot extends PhysicalActor implements Harmable {
	Vec2 aimPoint;
	
	boolean forward, backward, left, right;
	
	float baseFacing;
	
	float moveSinceLastTread;
	
	String tread = "A";
	
	int muzzleBlastTimer = 0;
	
	int shockImmune = 0;
	
	float hp;

	public Robot(float x, float y)
	{
		BodyDef bd = new BodyDef();
		
		bd.position.x = x;
		bd.position.y = y;
		bd.fixedRotation = true;
		
		body = Game.s.physicsWorld.createBody(bd);
		
		CircleDef cd = new CircleDef();
		cd.radius = 0.4f;
		cd.density = 1;
		
		body.createShape(cd);
		
		body.setMassFromShapes();
		
		aimPoint = body.getPosition().add(new Vec2(100,0));
		
		body.setUserData(this);
	}
	
	public float getHp() {
		return hp;
	}
	
	public boolean testLOSTo(Vec2 trg)
	{
		Vec2 cur = new Vec2(body.getPosition());
		Vec2 delta = trg.sub(cur);
		delta = delta.mul(0.3f / delta.length());
		
		while(Math.abs(cur.x - trg.x) > 1 || Math.abs(cur.y - trg.y) > 1)
		{
			if(!Game.s.map.isPassable((int)(cur.x + 0.5f), (int)(cur.y + 0.5f))) return false;
			cur.x += delta.x;
			cur.y += delta.y;
		}
		
		return true;
	}

	public void setMuzzleBlastTimer(int muzzleBlastTimer) {
		this.muzzleBlastTimer = muzzleBlastTimer;
	}
	
	public float getAimFacing()
	{
		return (float)Math.atan2(aimPoint.y - body.getPosition().y, aimPoint.x - body.getPosition().x);
	}
	
	public float getSpeedMod()
	{
		return 1;
	}

	@Override
	public void update() {
		robotUpdate();
		
		super.update();
	}

	protected void robotUpdate() {
		body.setLinearVelocity(body.getLinearVelocity().mul(0.8f));
		
		// forward backward movement
		
		int fb = 0;
		
		if(forward && !backward) fb = 1;
		if(!forward && backward) fb = -1;
		
		Vec2 force = getMovePoint().sub(body.getPosition());
		force.normalize();
		
		body.applyImpulse(force.mul(fb).mul(getSpeedMod()), new Vec2());
		
		// strafing
		
		int lr = 0;
		
		if(left && !right) lr = -1;
		if(!left && right) lr = 1;
		
		float aimFacing = (float)Math.atan2(getMovePoint().y - body.getPosition().y, getMovePoint().x - body.getPosition().x);
		
		force = new Vec2((float)Math.cos(aimFacing + 1.6f), (float)Math.sin(aimFacing + 1.6f));
		
		body.applyImpulse(force.mul(lr).mul(getSpeedMod()), new Vec2());
		
		if(muzzleBlastTimer > 0) muzzleBlastTimer--;
		
		moveSinceLastTread += body.getLinearVelocity().length();
		
		if(moveSinceLastTread > 0.5f)
		{
			if(tread.equals("A"))
				tread = "B";
			else
				tread = "A";
			
			moveSinceLastTread = 0;
		}
		
		if(shockImmune > 0) shockImmune--;
	}
	
	public Vec2 getAimPoint() {
		return aimPoint;
	}

	public Vec2 getMovePoint()
	{
		return aimPoint;
	}
	
	public String getFacingImageNumber(float facing)
	{
		if(facing < 0) facing += (float)(Math.PI * 2);
		return "00" + (((int)(facing * (16.f / Math.PI)) % 32) + 32);
	}

	@Override
	public void render() {
		
		if(body.getLinearVelocity().length() > 0.1f)
			baseFacing = -(float)Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x);
		
		SGF.getInstance().renderImage("Base" + tread + getFacingImageNumber(baseFacing), body.getPosition().x, body.getPosition().y, getSize(), getSize(), 0, true);
		
		float aimFacing = -(float)Math.atan2(aimPoint.y - body.getPosition().y, aimPoint.x - body.getPosition().x);
		
		SGF.getInstance().renderImage(getTopPrefix() + getFacingImageNumber(aimFacing), body.getPosition().x, body.getPosition().y, getSize(), getSize(), 0, true);
		if(muzzleBlastTimer > 0)
			SGF.getInstance().renderImage("muzzleblast" + getFacingImageNumber(aimFacing), body.getPosition().x, body.getPosition().y, getSize(), getSize(), 0, true);
		
		super.render();
	}
	
	protected String getTopPrefix() {
		return "top";
	}

	protected float getSize()
	{
		return 1.45f;
	}
	
	protected void aim(Vec2 target)
	{
		aimPoint = target;
	}

	@Override
	public boolean keep() {
		return hp > 0;
	}

	@Override
	public void takeDamage(float amount) {
		if(amount == Shockbomb.DAMAGE){
			if(shockImmune > 0)
				return;
			else
				shockImmune = 120;
		}
		hp -= amount;
		//System.out.println(this + " took " + amount + " damage!");
	}

	@Override
	public void notifyDestroyed() {
		Game.s.playAudio("robotdie");
		super.notifyDestroyed();
	}
}
