package info.quadtree.rv.actor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import org.jbox2d.collision.FilterData;
import org.jbox2d.common.Vec2;

import info.quadtree.rv.Game;
import info.quadtree.rv.item.Item;
import sgf.SGF;
import sv.vfx.Spark;

public class Player extends Robot implements MouseMotionListener, MouseListener, KeyListener {

	public final static int MAX_INVENTORY = 68;
	public final static int MAX_HP = 10;
	
	public int mScreenX;
	public int mScreenY;
	
	Item[] inventory;
	
	int quickItemsSelected = 0;
	
	boolean firingLeft = false;
	boolean firingRight = false;
	
	float money = 0;
	
	int repairCartridges = 1;
	
	float shield = 0;
	
	boolean plUp = false;
	boolean plDown = false;
	boolean plLeft = false;
	boolean plRight = false;
	
	public String getName()
	{
		return "Player";
	}
	
	public Player(float x, float y) {
		super(x, y);
		
		SGF.getInstance().addKeyListener(this);
		SGF.getInstance().addMouseListener(this);
		SGF.getInstance().addMouseMotionListener(this);
		
		inventory = new Item[MAX_INVENTORY];
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 2;
		fd.maskBits = (1 << 2) | (1 << 4) | (1 << 5) | (1) | 1 << 7;
		
		body.getShapeList().setFilterData(fd);
		
		hp = MAX_HP;
	}
	
	public void setMoney(float money) {
		this.money = money;
	}

	@Override
	public void reset() {
		hp = MAX_HP;
		body.setXForm(new Vec2(502, 545), 0.f);
	}
	
	public float getShield()
	{
		return shield;
	}
	
	public float getMaxShield()
	{
		float maxShield = 0;
		
		if(inventory[quickItemsSelected * 2] != null) maxShield += inventory[quickItemsSelected * 2].getShieldMod();
		if(inventory[quickItemsSelected * 2 + 1] != null) maxShield += inventory[quickItemsSelected * 2 + 1].getShieldMod();
		
		return maxShield;
	}
	
	@Override
	public float getSpeedMod()
	{
		float moveMod = 1;
		
		if(inventory[quickItemsSelected * 2] != null) moveMod *= inventory[quickItemsSelected * 2].getSpeedMod();
		if(inventory[quickItemsSelected * 2 + 1] != null) moveMod *= inventory[quickItemsSelected * 2 + 1].getSpeedMod();
		
		return moveMod;
	}
	
	public void addRepairCartridge()
	{
		repairCartridges++;
		Game.s.playAudio("getitem");
	}
	
	public int getRepairCartridges()
	{
		return repairCartridges;
	}
	
	public void modMoney(float amount)
	{
		money += amount;
	}
	
	public boolean hasMoney(float amount)
	{
		return money >= amount;
	}
	
	public Item[] getInventory() {
		return inventory;
	}
	
	public void setInventory(Item[] inv)
	{
		inventory = inv;
	}

	public void receiveItem(Item itm)
	{
		for(int i=0;i<inventory.length;i++){
			if(inventory[i] == null){
				inventory[i] = itm;
				Game.s.playAudio("getitem");
				return;
			}
		}
	}
	
	public int getQuickItemsSelected() {
		return quickItemsSelected;
	}

	@Override
	public void update() {
		
		Point2D ap = SGF.getInstance().screenToReal(new Point2D.Float(mScreenX, mScreenY));
		aim(new Vec2((float)ap.getX(), (float)ap.getY()));
		
		if(firingLeft && inventory[quickItemsSelected * 2] != null) inventory[quickItemsSelected * 2].tryUse(this);
		if(firingRight && inventory[quickItemsSelected * 2 + 1] != null) inventory[quickItemsSelected * 2 + 1].tryUse(this);
		
		for(Item itm : inventory)
			if(itm != null) itm.update();
		
		forward = plUp || plDown || plLeft || plRight;
		
		//if(shield < getMaxShield()) shield += 0.01f;
		shield = Math.min(shield + 0.01f, getMaxShield());
		
		super.update();
	}

	@Override
	public Vec2 getMovePoint() {
		if(plUp && !plDown && !plLeft && !plRight) return getPosition().add(new Vec2(0, -1));
		if(!plUp && plDown && !plLeft && !plRight) return getPosition().add(new Vec2(0, 1));
		if(!plUp && !plDown && plLeft && !plRight) return getPosition().add(new Vec2(-1, 0));
		if(!plUp && !plDown && !plLeft && plRight) return getPosition().add(new Vec2(1, 0));
		
		if(plUp && !plDown && plLeft && !plRight) return getPosition().add(new Vec2(-1, -1));
		if(plUp && !plDown && !plLeft && plRight) return getPosition().add(new Vec2(1, -1));
		if(!plUp && plDown && plLeft && !plRight) return getPosition().add(new Vec2(-1, 1));
		if(!plUp && plDown && !plLeft && plRight) return getPosition().add(new Vec2(1, 1));
		
		return getPosition().add(new Vec2(1, 1));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	protected void setQuickItems(int ind)
	{
		//if(inventory[quickItemsSelected*2] != null) inventory[quickItemsSelected*2].deEquiped();
		//if(inventory[quickItemsSelected*2+1] != null) inventory[quickItemsSelected*2+1].deEquiped();
		//if(inventory[ind*2] != null) inventory[ind*2].equipped();
		//if(inventory[ind*2+1] != null) inventory[ind*2+1].equipped();
		
		quickItemsSelected = ind;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(Game.s.dialogUp) return;
		/*if(e.getKeyCode() == KeyEvent.VK_W) forward = true;
		if(e.getKeyCode() == KeyEvent.VK_S) backward = true;
		if(e.getKeyCode() == KeyEvent.VK_A) left = true;
		if(e.getKeyCode() == KeyEvent.VK_D) right = true;*/
		
		if(e.getKeyCode() == KeyEvent.VK_W) plUp = true;
		if(e.getKeyCode() == KeyEvent.VK_S) plDown = true;
		if(e.getKeyCode() == KeyEvent.VK_A) plLeft = true;
		if(e.getKeyCode() == KeyEvent.VK_D) plRight = true;
		
		if(e.getKeyCode() == KeyEvent.VK_1) setQuickItems(0);
		if(e.getKeyCode() == KeyEvent.VK_2) setQuickItems(1);
		if(e.getKeyCode() == KeyEvent.VK_3) setQuickItems(2);
		if(e.getKeyCode() == KeyEvent.VK_4) setQuickItems(3);
		
		if(e.getKeyCode() == KeyEvent.VK_R && repairCartridges > 0 && hp < MAX_HP)
		{
			repairCartridges--;
			hp = Math.min(hp + 3, MAX_HP);
			Game.s.playAudio("usecart");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*if(e.getKeyCode() == KeyEvent.VK_W) forward = false;
		if(e.getKeyCode() == KeyEvent.VK_S) backward = false;
		if(e.getKeyCode() == KeyEvent.VK_A) left = false;
		if(e.getKeyCode() == KeyEvent.VK_D) right = false;*/
		
		if(e.getKeyCode() == KeyEvent.VK_W) plUp = false;
		if(e.getKeyCode() == KeyEvent.VK_S) plDown = false;
		if(e.getKeyCode() == KeyEvent.VK_A) plLeft = false;
		if(e.getKeyCode() == KeyEvent.VK_D) plRight = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(Game.s.dialogUp) return;
		if(e.getButton() == MouseEvent.BUTTON1) firingLeft = true;
		if(e.getButton() == MouseEvent.BUTTON3) firingRight = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) firingLeft = false;
		if(e.getButton() == MouseEvent.BUTTON3) firingRight = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(Game.s.dialogUp) return;
		mScreenX = e.getX();
		mScreenY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(Game.s.dialogUp) return;
		mScreenX = e.getX();
		mScreenY = e.getY();
	}

	public float getMoney() {
		return money;
	}

	@Override
	public void takeDamage(float amount) {
		float shieldDamage = Math.min(amount, shield);
		amount -= shieldDamage;
		shield -= shieldDamage;
		if(amount > 0.01f)
			super.takeDamage(amount);
	}

	@Override
	public void notifyDestroyed() {
		for(int i=0;i<24;i++)
		{
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.9f, 0.015f, 25.f, "bolt1"));
		}
		super.notifyDestroyed();
	}
}
