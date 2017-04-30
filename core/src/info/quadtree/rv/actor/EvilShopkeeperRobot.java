package info.quadtree.rv.actor;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;

import info.quadtree.rv.Dialog;
import info.quadtree.rv.FinalCinematic;
import info.quadtree.rv.Game;
import info.quadtree.rv.item.BoltGun;
import info.quadtree.rv.item.DevastatorCannon;
import info.quadtree.rv.item.NeedleGun;
import info.quadtree.rv.item.VulcanCannon;
import info.quadtree.rv.projectile.Bolt;
import info.quadtree.rv.projectile.Missile;
import sv.vfx.Spark;

public class EvilShopkeeperRobot extends ShopkeeperRobot{

	// STAGES
	// 0 - Peaceful shopkeeper
	// 1 - Go out front
	// 2 - Have arrived at out front
	// 3 - Fighting player
	public int stage = 0;
	
	boolean dynamic = false;
	
	int volleyCooldown = 0;
	int missilesCooldown = 0;
	int cannonCooldown = 0;
	
	public final static float MAX_HP = 50;
	
	boolean spawnAdds = false;
	
	Vec2 startPos;
	
	public EvilShopkeeperRobot(float x, float y) {
		super(x,y);
		
		startPos = new Vec2(x,y);
		
		reset();
		
		Game.s.actors.add(new VulcanCannon(x - 2f, y + 2f, true));
		Game.s.actors.add(new NeedleGun(x, y + 2f, true));
		Game.s.actors.add(new DevastatorCannon(x + 2f, y + 2f, true));
	}
	
	@Override
	public void reset() {
		hp = MAX_HP;
	}
	
	protected void becomeDynamic()
	{
		body.destroyShape(body.getShapeList());
		
		CircleDef cd = new CircleDef();
		cd.radius = 0.4f;
		cd.density = 1;
		
		body.createShape(cd);
		
		body.setMassFromShapes();
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 3;
		fd.maskBits = 0xffffffff;
		
		body.getShapeList().setFilterData(fd);
		
		dynamic = true;
		lookAtPlayer = false;
		
		Game.s.player.getBody().applyImpulse(new Vec2(-40, 0), new Vec2());
	}

	@Override
	public void startConversation() {
		Dialog d = new Dialog();
		
		if(!Game.s.freeItemGiven)
		{
			d.addMessage("Shopkeeper Alpha: Its dangerous to go alone! Take this!");
			d.addMessage(Game.s.player.getName() + ": A bolt cannon for free? Whats the catch?");
			d.addMessage("Shopkeeper Alpha: No catch. Consider it a free sample.");
			d.addMessage(Game.s.player.getName() + ": Okay. Thanks.");
			
			Game.s.player.receiveItem(new BoltGun());
			Game.s.freeItemGiven = true;
		} else if(Game.s.plot == 6)
		{
			d.addMessage("Shopkeeper Alpha: AHAHAHAHAHAHAHAHA!");
		} else if(Game.s.plot == 5)
		{
			d = new FinalCinematic(this);
			
			d.addMessage(Game.s.player.getName() + ": The town is under attack! Give me all your wares so I can defend!");
			d.addMessage("Shopkeeper Alpha: So you've discovered me eh? About time!");
			d.addMessage("Shopkeeper Alpha: I'll just take over this town from that miserable mayor!");
			d.addMessage(Game.s.player.getName() + ": What?! You'll never get away with this!");
			d.addMessage("Shopkeeper Alpha: Oh really?");
			d.addMessage(Game.s.player.getName() + ": ...");
			d.addMessage("Shopkeeper Alpha: What? No snappy quip?");
			d.addMessage("Shopkeeper Alpha: Tongue tied? Must be that virus my bolt cannon installed.");
			d.addMessage("Shopkeeper Alpha: Nothing can stop me now!");
			d.addMessage("Shopkeeper Alpha: AHAHAHAHAHAHAHAHA!");
			d.addMessage("Tech: He's wrecking the town! DO SOMETHING!");
			d.addMessage("Tech: Hello? Can you hear me? Is my radio working?");
			d.addMessage("Tech: Oh. I think you have a virus, there.");
			d.addMessage("Tech: Give me a sec. I'll fix that right up for you.");
			d.addMessage("Tech: Good luck!");
			
			Game.s.plot = 6;
		} else {
			d.addMessage("Shopkeeper Alpha: Have a look. I've got all kinds of single target weapons!");
		}
		
		Game.s.actors.add(d);
	}

	@Override
	public void update() {
		if(stage == 1 && !dynamic) becomeDynamic();
		if(stage == 1)
		{
			aimPoint.x = 510;
			aimPoint.y = 520;
			forward = true;
			
			if(body.getPosition().sub(aimPoint).length() < 2)
			{
				stage = 2;
				forward = false;
			}
		}
		if(stage >= 2)
		{
			if(stage == 3 && testLOSTo(Game.s.player.getPosition()))
			{
				aimPoint = Game.s.player.getPosition();
				if(getPosition().sub(aimPoint).length() < 12)
					backward = true;
				else
					backward = false;
			} else {
				aimPoint = getPosition().add(new Vec2((float)Math.cos(getAimFacing() + 0.01f), (float)Math.sin(getAimFacing() + 0.01f)));
			}
			
			fireVolley();
			fireCannon();
			
			if(stage == 3)
				fireMissiles();
		}
		
		if(volleyCooldown > 0) volleyCooldown--;
		if(missilesCooldown > 0) missilesCooldown--;
		if(cannonCooldown > 0) cannonCooldown--;
		
		if(!Game.s.dialogUp && stage == 0) body.setXForm(startPos, 0);
		
		if(spawnAdds)
		{
			Game.s.actors.add(new FastEnemy(getPosition().x, getPosition().y));
			Game.s.actors.add(new FastEnemy(getPosition().x, getPosition().y));
			spawnAdds = false;
		}
		
		super.update();
	}
	
	public void fireVolley()
	{
		if(volleyCooldown > 0) return;
		Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing(), 6.f, "evilshot", true, 0.5f));
		Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing() + 0.2f, 6.f, "evilshot", true, 0.5f));
		Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing() - 0.2f, 6.f, "evilshot", true, 0.5f));
		Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing() + 0.4f, 6.f, "evilshot", true, 0.5f));
		Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing() - 0.4f, 6.f, "evilshot", true, 0.5f));
		Game.s.playAudio("boltfire");
		
		volleyCooldown = 100;
	}
	
	public void fireMissiles()
	{
		if(missilesCooldown > 0) return;
		
		Game.s.actors.add(new Missile(getPosition().x, getPosition().y, 1.6f));
		Game.s.actors.add(new Missile(getPosition().x, getPosition().y, 3.2f));
		Game.s.actors.add(new Missile(getPosition().x, getPosition().y, 4.8f));
		Game.s.actors.add(new Missile(getPosition().x, getPosition().y, 6.4f));
		
		missilesCooldown = 240;
	}
	
	public void fireCannon()
	{
		if(cannonCooldown > 0) return;
		
		Game.s.actors.add(new Bolt(getPosition().x, getPosition().y, getAimFacing(), 8.f, "devastatorshot", true, 9.5f));
		Game.s.playAudio("firexplosion");
		
		cannonCooldown = 150;
	}

	@Override
	public void notifyDestroyed() {
		Game.s.plot = 7;
		Game.s.player.modMoney(4000);
		
		for(int i=0;i<24;i++)
		{
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.9f, 0.015f, 25.f, "evilshot"));
		}
		
		Game.s.showWinMessage = true;
		
		super.notifyDestroyed();
	}
	
	@Override
	protected String getTopPrefix() {
		return "topalpha";
	}

	@Override
	public void takeDamage(float amount) {
		int hpLevel = (int)(hp / MAX_HP * 3);
		super.takeDamage(amount);
		if((int)(hp / MAX_HP * 3) != hpLevel)
		{
			spawnAdds = true;
		}
	}
}























