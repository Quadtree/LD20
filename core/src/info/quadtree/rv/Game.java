package info.quadtree.rv;

import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import info.quadtree.rv.actor.Actor;
import info.quadtree.rv.actor.EvilShopkeeperRobot;
import info.quadtree.rv.actor.Player;
import info.quadtree.rv.item.Item;
import sgf.GameInterface;
import sgf.SGF;

public class Game implements GameInterface, KeyListener, MouseListener, ContactListener {

	public static Game s;

	public ArrayList<Actor> actors;

	AudioThread at;

	HashMap<String, AudioClip> audioFiles;

	public boolean dialogUp = false;

	public boolean freeItemGiven = false;

	public boolean helpScreenUp = true;

	boolean inventoryScreen = false;

	int itemSelected = -1;

	public Map map;

	public boolean objectiveScreen = false;

	public World physicsWorld;

	public Player player;
	public int plot = 0;

	public Random rand;

	int respawnClock = 0;

	public boolean showWinMessage = false;

	public boolean titleScreenUp = true;

	public Game() {
		s = this;
	}

	@Override
	public void add(ContactPoint point) {
		Object o1 = point.shape1.getBody().getUserData();
		Object o2 = point.shape2.getBody().getUserData();

		if (o1 != null && o1 instanceof ContactListener)
			((ContactListener) o1).add(point);
		if (o2 != null && o2 instanceof ContactListener)
			((ContactListener) o2).add(point);
	}

	float getInvItemX(int itemId) {
		return 200 + (itemId % 17 * 38);
	}

	float getInvItemY(int itemId) {
		return 250 + (itemId / 17 * 38);
	}

	@Override
	public void init() {

		at = new AudioThread();
		Thread t = new Thread(at);
		t.start();

		rand = new Random();
		audioFiles = new HashMap<String, AudioClip>();
		actors = new ArrayList<Actor>();
		physicsWorld = new World(new AABB(new Vec2(0, 0), new Vec2(1024, 1024)), new Vec2(), true);
		physicsWorld.setContactListener(this);
		map = new Map();
		actors.add(map);
		map.load();

		player = new Player(502, 520);

		actors.add(player);

		// actors.add(new CheatGun(510, 520));

		// actors.add(new RepairCartridge(530, 520, false, 1));

		SGF.getInstance().addKeyListener(this);
		SGF.getInstance().addMouseListener(this);

		Dialog startNarration = new Dialog();
		startNarration.addMessage("You arrive in a peaceful Robot Village.");
		startNarration.addMessage("Despite its peaceful looks, you have heard that they are having");
		startNarration.addMessage("a problem with some banditbots, and the mayor is offering");
		startNarration.addMessage("a reward for anyone who can rid them of this problem.");

		actors.add(startNarration);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (titleScreenUp) {
			titleScreenUp = false;
			return;
		}
		if (helpScreenUp) {
			helpScreenUp = false;
			return;
		}
		if (objectiveScreen) {
			objectiveScreen = false;
			return;
		}
		if (Game.s.dialogUp)
			return;
		if (e.getKeyCode() == KeyEvent.VK_I)
			inventoryScreen = !inventoryScreen;
		if (e.getKeyCode() == KeyEvent.VK_O)
			objectiveScreen = !objectiveScreen;
		if (e.getKeyCode() == KeyEvent.VK_F1)
			helpScreenUp = !helpScreenUp;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

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
	public void mousePressed(MouseEvent e) {
		if (inventoryScreen) {
			for (int i = 0; i < player.getInventory().length; i++) {
				if (Math.abs(getInvItemX(i) - e.getX()) < 32 && Math.abs(getInvItemY(i) - e.getY()) < 32) {
					if (itemSelected == -1) {
						itemSelected = i;
					} else {
						Item temp = player.getInventory()[i];
						player.getInventory()[i] = player.getInventory()[itemSelected];
						player.getInventory()[itemSelected] = temp;
						itemSelected = -1;
					}
					break;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void persist(ContactPoint point) {
		// TODO Auto-generated method stub

	}

	public void playAudio(String name) {
		// if(!audioFiles.containsKey(name))
		// {
		// audioFiles.put(name,
		// RobotVillage.s.getAudioClip(RobotVillage.s.getDocumentBase(),
		// "./media/" + name + ".wav"));
		// }
		// audioFiles.get(name).play();
		// RobotVillage.s.getAudioClip(RobotVillage.s.getDocumentBase(),
		// "./media/" + name + ".wav").play();

		at.audioNames.add(name);
	}

	@Override
	public void remove(ContactPoint point) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		SGF.getInstance().setCamera(player.getPosition().x, player.getPosition().y, 32);
		for (Actor a : actors)
			a.render();

		for (int i = 0; i < 2; i++) {
			SGF.getInstance().renderImage("inventorytile", 40 + 40 * i, 34, 32, 32, 0, false);
			if (player.getInventory()[player.getQuickItemsSelected() * 2 + i] != null) {
				SGF.getInstance().renderImage(player.getInventory()[player.getQuickItemsSelected() * 2 + i].getGraphicName(), 40 + 40 * i, 34, 32, 32, 0, false);
			}
		}

		if (titleScreenUp) {
			SGF.getInstance().renderImage("mayorbig", 512, 384, -1, -1, 0, false);
			SGF.getInstance().renderText("Made by Quadtree for Ludum Dare 20", 750, 760, 0, 255, 0, false, 12);
			return;
		}

		if (helpScreenUp) {
			SGF.getInstance().renderImage("help", 512, 464, -1, -1, 0, false);
		}

		if (objectiveScreen) {
			SGF.getInstance().renderImage("inventoryback", 512, 464, 700, 25, 0, false);

			String text = "";

			switch (plot) {
			case 0:
				text = "Talk to the mayor. His house is in the center bottom of town.";
				break;
			case 1:
				text = "Talk to the technician. His house is just west of the mayor's.";
				break;
			case 2:
				text = "Search the cave to the west of town for the leader.";
				break;
			case 3:
				text = "Return the core to the technician.";
				break;
			case 4:
				text = "Search the cave to the east of town for something transmitting.";
				break;
			case 5:
				text = "Return to town and find who was transmitting.";
				break;
			case 6:
				text = "Defeat the evil shopkeeper.";
				break;
			case 7:
				text = "Bask in your glory!";
				break;
			}

			SGF.getInstance().renderText(text, 200, 468, 255, 255, 255, false, 12);
		}

		if (inventoryScreen) {
			SGF.getInstance().renderImage("inventoryback", 512, 464, 700, 500, 0, false);

			for (int i = 0; i < player.getInventory().length; i++) {
				SGF.getInstance().renderImage("inventorytile", getInvItemX(i), getInvItemY(i), 32, 32, 0, false);

				if (player.getInventory()[i] != null)
					SGF.getInstance().renderImage(player.getInventory()[i].getGraphicName(), getInvItemX(i), getInvItemY(i), 32, 32, 0, false);
			}

			int q = player.getQuickItemsSelected() * 2;

			SGF.getInstance().renderImage("quickinv", (getInvItemX(q) + getInvItemX(q + 1)) / 2, getInvItemY(q), 86, 48, 0, false);

			for (int i = 0; i < player.getInventory().length; i++) {
				if (itemSelected == i)
					SGF.getInstance().renderImage("invselected", getInvItemX(i), getInvItemY(i), 48, 48, 0, false);
			}

			SGF.getInstance().renderText("The items surrounded by a yellow box are currently EQUIPPED.", 200, 400, 255, 255, 0, false, 12);
			SGF.getInstance().renderText("The mouse buttons activate these items, and if they have a passive effect", 200, 414, 255, 255, 0, false, 12);
			SGF.getInstance().renderText("like a shield or turbo module, it will be active while they are equipped.", 200, 428, 255, 255, 0, false, 12);
			SGF.getInstance().renderText("Use the 1-4 keys to change what items are equipped.", 200, 442, 255, 255, 0, false, 12);
			SGF.getInstance().renderText("Move items by clicking on them and clicking on the desired slot.", 200, 460, 0, 255, 0, false, 12);
		}

		for (int i = 0; i < (int) (player.getHp()) + (int) (player.getShield()); i++) {
			if (i < player.getHp())
				SGF.getInstance().renderImage("healthbar", 20 + i * 10, 60, 10, 10, 0, false);
			else
				SGF.getInstance().renderImage("shieldbar", 20 + i * 10, 60, 10, 10, 0, false);
		}

		SGF.getInstance().renderText("Money: " + (int) Game.s.player.getMoney(), 20, 80, 200, 255, 200, false, 16);
		for (int i = 0; i < player.getRepairCartridges(); i++) {
			SGF.getInstance().renderImage("repaircart", 20 + i * 18, 100, 24, 24, 0, false);
		}

		if (inventoryScreen) {
			for (int i = 0; i < player.getInventory().length; i++) {
				if (player.getInventory()[i] == null)
					continue;
				if (Math.abs(getInvItemX(i) - player.mScreenX) < 32 && Math.abs(getInvItemY(i) - player.mScreenY) < 32) {
					player.getInventory()[i].displayInfo();
					break;
				}
			}
		}

		if (showWinMessage) {
			SGF.getInstance().renderImage("winmessage", 512, 384, -1, -1, 0, false);
		}
	}

	@Override
	public void result(ContactResult point) {
		Object o1 = point.shape1.getBody().getUserData();
		Object o2 = point.shape2.getBody().getUserData();

		if (o1 != null && o1 instanceof ContactListener)
			((ContactListener) o1).result(point);
		if (o2 != null && o2 instanceof ContactListener)
			((ContactListener) o2).result(point);
	}

	@Override
	public void shutdown() {
		at.keepRun = false;
	}

	@Override
	public void update() {
		if (!inventoryScreen) {
			physicsWorld.step(1.f / 60.f, 10);

			boolean dialogTemp = false;

			for (int i = 0; i < actors.size(); i++) {
				if (actors.get(i) instanceof Dialog)
					dialogTemp = true;
				if (actors.get(i).keep())
					actors.get(i).update();
				else {
					actors.get(i).notifyDestroyed();
					actors.remove(i--);
				}
			}

			dialogUp = dialogTemp;
		}

		if (!freeItemGiven && player.getPosition().sub(new Vec2(512, 512)).length() > 30) {
			for (Actor a : actors) {
				if (a instanceof EvilShopkeeperRobot) {
					EvilShopkeeperRobot er = (EvilShopkeeperRobot) a;
					er.getBody().setXForm(player.getPosition(), 0);
				}
			}
		}

		if (player.getHp() <= 0) {
			respawnClock++;
			if (respawnClock > 180) {

				for (Actor a : actors)
					a.reset();

				Player pl = new Player(502, 545);
				pl.setInventory(player.getInventory());
				pl.setMoney(player.getMoney());
				player = pl;
				actors.add(player);

				respawnClock = 0;
			}
		}
	}

}
