package info.quadtree.rv.actor;

import org.jbox2d.collision.FilterData;
import info.quadtree.rv.Game;
import info.quadtree.rv.item.RepairCartridge;
import info.quadtree.rv.projectile.Bolt;
import sv.vfx.Spark;

public class BasicEnemy extends Robot {

	int shotCooldown = 0;
	
	public BasicEnemy(float x, float y) {
		super(x, y);
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 3;
		fd.maskBits = 0xffffffff;
		
		body.getShapeList().setFilterData(fd);
		
		reset();
	}

	@Override
	public void reset() {
		hp = 3;
	}

	@Override
	public void update() {
		
		if(body.getPosition().sub(Game.s.player.getPosition()).lengthSquared() > 32*32) return;
		
		if(shotCooldown > 0) shotCooldown--;
		
		aim(Game.s.player.getPosition());
		
		if(testLOSTo(Game.s.player.getPosition()))
		{
			if(shotCooldown <= 0)
			{
				setMuzzleBlastTimer(3);
				shotCooldown = 40;
				Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing(), 3.5f, "evilshot", true, 1.f));
				Game.s.playAudio("boltfire");
			}
		}
		
		super.update();
	}
	
	@Override
	public void notifyDestroyed() {
		Game.s.player.modMoney(20);
		
		if(Game.s.rand.nextInt(10) == 0) Game.s.actors.add(new RepairCartridge(body.getPosition().x, body.getPosition().y, false, 1));
		
		for(int i=0;i<12;i++)
		{
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.9f, 0.015f, 7.f, "evilshot"));
		}
		
		super.notifyDestroyed();
	}
	
	@Override
	protected String getTopPrefix() {
		return "topbad";
	}
}
