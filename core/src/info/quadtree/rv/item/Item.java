package info.quadtree.rv.item;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.dynamics.BodyDef;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.actor.Player;
import sgf.SGF;

public abstract class Item extends PhysicalActor implements KeyListener{
	boolean onGround;
	
	boolean forSale;
	
	boolean displayInfo = false;
	
	public Item(float x, float y, boolean forSale)
	{
		BodyDef bd = new BodyDef();
		
		bd.position.x = x;
		bd.position.y = y;
		bd.fixedRotation = true;
		
		body = Game.s.physicsWorld.createBody(bd);
		
		CircleDef cd = new CircleDef();
		cd.radius = 0.75f;
		cd.isSensor = true;
		
		FilterData fd = new FilterData();
		fd.maskBits = 1 << 2;
		fd.categoryBits = 1 << 4;
		
		body.createShape(cd).setFilterData(fd);
		
		onGround = true;
		this.forSale = forSale;
		
		if(forSale) SGF.getInstance().addKeyListener(this);
	}
	
	public Item()
	{
	}
	
	public void pickedUp()
	{}
	
	public float getShieldMod()
	{
		return 0;
	}
	
	public float getSpeedMod()
	{
		return 1;
	}
	
	@Override
	public void update()
	{
		if(Game.s.plot >= 6) forSale = false;
		
		displayInfo = false;
		if(body != null && body.getBodiesInContact().contains(Game.s.player.getBody()))
		{
			if(!forSale)
			{
				pickupSequence();
			} else {
				displayInfo = true;
			}
		}
	}

	public void pickupSequence() {
		SGF.getInstance().removeKeyListener(this);
		onGround = false;
		Game.s.physicsWorld.destroyBody(body);
		Game.s.player.receiveItem(this);
		body = null;
		pickedUp();
	}
	
	@Override
	public boolean keep()
	{
		return onGround;
	}
	
	public abstract String getGraphicName();
	
	public String getName(){ return ""; }
	public String[] getDescription(){ return new String[]{""}; }
	public float getValue(){ return 0; }

	@Override
	public void render() {
		if(body != null)
			SGF.getInstance().renderImage(getGraphicName(), body.getPosition().x, body.getPosition().y, 1, 1, 0, true);
		
		if(displayInfo)
		{
			displayInfo();
			
			SGF.getInstance().renderText("Press P to buy this", 512 - 130, 180, 255, 255, 0, false, 12);
		}
		
		super.render();
	}

	public void displayInfo() {
		SGF.getInstance().renderImage("inventoryback", 512, 100, 300, 200, 0, false);
		SGF.getInstance().renderText(getName(), 512 - 130, 30, 255, 255, 255, false, 18);
		SGF.getInstance().renderText("Value: " + (int)getValue(), 512 - 130, 54, 255, 255, 255, false, 18);
		int y = 54+22;
		for(String s : getDescription())
		{
			SGF.getInstance().renderText(s, 512 - 130, y, 255, 255, 255, false, 12);
			y += 14;
		}
	}
	
	public void tryUse(Player user)
	{
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_P && displayInfo && Game.s.player.hasMoney(getValue()))
		{
			Game.s.player.modMoney(-getValue());
			pickupSequence();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
