package info.quadtree.rv.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;

import info.quadtree.rv.Game;
import info.quadtree.rv.Spark;
import info.quadtree.rv.graphics.KeyEvent;
import info.quadtree.rv.graphics.KeyListener;
import info.quadtree.rv.graphics.MouseEvent;
import info.quadtree.rv.graphics.MouseListener;
import info.quadtree.rv.graphics.MouseMotionListener;
import info.quadtree.rv.graphics.Point2D;
import info.quadtree.rv.graphics.SGF;
import info.quadtree.rv.item.Item;

public class Player extends Robot implements MouseMotionListener, MouseListener, KeyListener {

	public final static int MAX_HP = 10;
	public final static int MAX_INVENTORY = 68;

	boolean firingLeft = false;
	boolean firingRight = false;

	Item[] inventory;

	float money = 0;

	public int mScreenX;
	public int mScreenY;

	boolean plDown = false;

	boolean plLeft = false;

	boolean plRight = false;

	boolean plUp = false;
	int quickItemsSelected = 0;
	int repairCartridges = 1;
	float shield = 0;

	public Player(float x, float y) {
		super(x, y);

		SGF.getInstance().addKeyListener(this);
		SGF.getInstance().addMouseListener(this);
		SGF.getInstance().addMouseMotionListener(this);

		inventory = new Item[MAX_INVENTORY];

		Filter fd = new Filter();
		fd.categoryBits = 1 << 2;
		fd.maskBits = (1 << 2) | (1 << 4) | (1 << 5) | (1) | 1 << 7;

		this.setAllFilters(fd);

		hp = MAX_HP;
	}

	public void addRepairCartridge() {
		repairCartridges++;
		Game.s.playAudio("getitem");
	}

	public Item[] getInventory() {
		return inventory;
	}

	public float getMaxShield() {
		float maxShield = 0;

		if (inventory[quickItemsSelected * 2] != null)
			maxShield += inventory[quickItemsSelected * 2].getShieldMod();
		if (inventory[quickItemsSelected * 2 + 1] != null)
			maxShield += inventory[quickItemsSelected * 2 + 1].getShieldMod();

		return maxShield;
	}

	public float getMoney() {
		return money;
	}

	@Override
	public Vector2 getMovePoint() {
		if (plUp && !plDown && !plLeft && !plRight)
			return getPosition().add(new Vector2(0, -1));
		if (!plUp && plDown && !plLeft && !plRight)
			return getPosition().add(new Vector2(0, 1));
		if (!plUp && !plDown && plLeft && !plRight)
			return getPosition().add(new Vector2(-1, 0));
		if (!plUp && !plDown && !plLeft && plRight)
			return getPosition().add(new Vector2(1, 0));

		if (plUp && !plDown && plLeft && !plRight)
			return getPosition().add(new Vector2(-1, -1));
		if (plUp && !plDown && !plLeft && plRight)
			return getPosition().add(new Vector2(1, -1));
		if (!plUp && plDown && plLeft && !plRight)
			return getPosition().add(new Vector2(-1, 1));
		if (!plUp && plDown && !plLeft && plRight)
			return getPosition().add(new Vector2(1, 1));

		return getPosition().add(new Vector2(1, 1));
	}

	public String getName() {
		return "Player";
	}

	public int getQuickItemsSelected() {
		return quickItemsSelected;
	}

	public int getRepairCartridges() {
		return repairCartridges;
	}

	public float getShield() {
		return shield;
	}

	@Override
	public float getSpeedMod() {
		float moveMod = 1;

		if (inventory[quickItemsSelected * 2] != null)
			moveMod *= inventory[quickItemsSelected * 2].getSpeedMod();
		if (inventory[quickItemsSelected * 2 + 1] != null)
			moveMod *= inventory[quickItemsSelected * 2 + 1].getSpeedMod();

		return moveMod;
	}

	public boolean hasMoney(float amount) {
		return money >= amount;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (Game.s.dialogUp)
			return;
		/*
		 * if(e.getKeyCode() == KeyEvent.VK_W) forward = true; if(e.getKeyCode()
		 * == KeyEvent.VK_S) backward = true; if(e.getKeyCode() ==
		 * KeyEvent.VK_A) left = true; if(e.getKeyCode() == KeyEvent.VK_D) right
		 * = true;
		 */

		if (e.getKeyCode() == KeyEvent.VK_W)
			plUp = true;
		if (e.getKeyCode() == KeyEvent.VK_S)
			plDown = true;
		if (e.getKeyCode() == KeyEvent.VK_A)
			plLeft = true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			plRight = true;

		if (e.getKeyCode() == KeyEvent.VK_1)
			setQuickItems(0);
		if (e.getKeyCode() == KeyEvent.VK_2)
			setQuickItems(1);
		if (e.getKeyCode() == KeyEvent.VK_3)
			setQuickItems(2);
		if (e.getKeyCode() == KeyEvent.VK_4)
			setQuickItems(3);

		if (e.getKeyCode() == KeyEvent.VK_R && repairCartridges > 0 && hp < MAX_HP) {
			repairCartridges--;
			hp = Math.min(hp + 3, MAX_HP);
			Game.s.playAudio("usecart");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*
		 * if(e.getKeyCode() == KeyEvent.VK_W) forward = false;
		 * if(e.getKeyCode() == KeyEvent.VK_S) backward = false;
		 * if(e.getKeyCode() == KeyEvent.VK_A) left = false; if(e.getKeyCode()
		 * == KeyEvent.VK_D) right = false;
		 */

		if (e.getKeyCode() == KeyEvent.VK_W)
			plUp = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			plDown = false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			plLeft = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			plRight = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void modMoney(float amount) {
		money += amount;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (Game.s.dialogUp)
			return;
		mScreenX = e.getX();
		mScreenY = e.getY();
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
	public void mouseMoved(MouseEvent e) {
		if (Game.s.dialogUp)
			return;
		mScreenX = e.getX();
		mScreenY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (Game.s.dialogUp)
			return;
		if (e.getButton() == MouseEvent.BUTTON1)
			firingLeft = true;
		if (e.getButton() == MouseEvent.BUTTON3)
			firingRight = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			firingLeft = false;
		if (e.getButton() == MouseEvent.BUTTON3)
			firingRight = false;
	}

	@Override
	public void notifyDestroyed() {
		for (int i = 0; i < 24; i++) {
			Game.s.actors.add(new Spark(body.getPosition().x, body.getPosition().y, 0.9f, 0.015f, 25.f, "bolt1"));
		}
		super.notifyDestroyed();
	}

	public void receiveItem(Item itm) {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null) {
				inventory[i] = itm;
				Game.s.playAudio("getitem");
				return;
			}
		}
	}

	@Override
	public void render() {
		super.render();

		SGF.getInstance().log("" + getPosition());

		// SGF.getInstance().renderImage("wall", aimPoint.x, aimPoint.y,
		// getSize(), getSize(), 0, true);
	}

	@Override
	public void reset() {
		hp = MAX_HP;
		body.setTransform(new Vector2(502, 545), 0);
	}

	public void setInventory(Item[] inv) {
		inventory = inv;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	protected void setQuickItems(int ind) {
		// if(inventory[quickItemsSelected*2] != null)
		// inventory[quickItemsSelected*2].deEquiped();
		// if(inventory[quickItemsSelected*2+1] != null)
		// inventory[quickItemsSelected*2+1].deEquiped();
		// if(inventory[ind*2] != null) inventory[ind*2].equipped();
		// if(inventory[ind*2+1] != null) inventory[ind*2+1].equipped();

		quickItemsSelected = ind;
	}

	@Override
	public void takeDamage(float amount) {
		float shieldDamage = Math.min(amount, shield);
		amount -= shieldDamage;
		shield -= shieldDamage;
		if (amount > 0.01f)
			super.takeDamage(amount);
	}

	@Override
	public void update() {

		Point2D ap = SGF.getInstance().screenToReal(new Point2D(mScreenX, mScreenY));
		aim(new Vector2(ap.getX(), ap.getY()));

		if (firingLeft && inventory[quickItemsSelected * 2] != null)
			inventory[quickItemsSelected * 2].tryUse(this);
		if (firingRight && inventory[quickItemsSelected * 2 + 1] != null)
			inventory[quickItemsSelected * 2 + 1].tryUse(this);

		for (Item itm : inventory)
			if (itm != null)
				itm.update();

		forward = plUp || plDown || plLeft || plRight;

		// if(shield < getMaxShield()) shield += 0.01f;
		shield = Math.min(shield + 0.01f, getMaxShield());

		super.update();
	}
}
