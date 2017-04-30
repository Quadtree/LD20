package info.quadtree.rv.item;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.PhysicalActor;
import info.quadtree.rv.actor.Player;
import info.quadtree.rv.graphics.KeyEvent;
import info.quadtree.rv.graphics.KeyListener;
import info.quadtree.rv.graphics.SGF;

public abstract class Item extends PhysicalActor implements KeyListener, ContactListener {
	Set<Body> bodiesInContact = new HashSet<Body>();

	boolean displayInfo = false;

	boolean forSale;

	boolean onGround;

	public Item() {
	}

	public Item(float x, float y, boolean forSale) {
		BodyDef bd = new BodyDef();

		bd.position.x = x;
		bd.position.y = y;
		bd.fixedRotation = true;

		body = Game.s.physicsWorld.createBody(bd);

		CircleShape cd = new CircleShape();
		cd.setRadius(0.75f);

		FixtureDef fxd = new FixtureDef();
		fxd.isSensor = true;
		fxd.shape = cd;

		Filter fd = new Filter();
		fd.maskBits = 1 << 2;
		fd.categoryBits = 1 << 4;

		body.createFixture(fxd).setFilterData(fd);

		onGround = true;
		this.forSale = forSale;

		if (forSale)
			SGF.getInstance().addKeyListener(this);
	}

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getBody() != body)
			this.bodiesInContact.add(contact.getFixtureB().getBody());
		if (contact.getFixtureB().getBody() != body)
			this.bodiesInContact.add(contact.getFixtureA().getBody());
	}

	public void displayInfo() {
		SGF.getInstance().renderImage("inventoryback", 512, 100, 300, 200, 0, false);
		SGF.getInstance().renderText(getName(), 512 - 130, 30, 255, 255, 255, false, 18);
		SGF.getInstance().renderText("Value: " + (int) getValue(), 512 - 130, 54, 255, 255, 255, false, 18);
		int y = 54 + 22;
		for (String s : getDescription()) {
			SGF.getInstance().renderText(s, 512 - 130, y, 255, 255, 255, false, 12);
			y += 14;
		}
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA().getBody() != body)
			this.bodiesInContact.remove(contact.getFixtureB().getBody());
		if (contact.getFixtureB().getBody() != body)
			this.bodiesInContact.remove(contact.getFixtureA().getBody());
	}

	public String[] getDescription() {
		return new String[] { "" };
	}

	public abstract String getGraphicName();

	public String getName() {
		return "";
	}

	public float getShieldMod() {
		return 0;
	}

	public float getSpeedMod() {
		return 1;
	}

	public float getValue() {
		return 0;
	}

	@Override
	public boolean keep() {
		return onGround;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_P && displayInfo && Game.s.player.hasMoney(getValue())) {
			Game.s.player.modMoney(-getValue());
			pickupSequence();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void pickedUp() {
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
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void render() {
		if (body != null)
			SGF.getInstance().renderImage(getGraphicName(), body.getPosition().x, body.getPosition().y, 1, 1, 0, true);

		if (displayInfo) {
			displayInfo();

			SGF.getInstance().renderText("Press P to buy this", 512 - 130, 180, 255, 255, 0, false, 12);
		}

		super.render();
	}

	public void tryUse(Player user) {
	}

	@Override
	public void update() {
		if (Game.s.plot >= 6)
			forSale = false;

		displayInfo = false;
		if (body != null && bodiesInContact.contains(Game.s.player.getBody())) {
			if (!forSale) {
				pickupSequence();
			} else {
				displayInfo = true;
			}
		}
	}
}
