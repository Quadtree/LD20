package info.quadtree.rv;

import java.io.Serializable;
import java.util.ArrayList;

import info.quadtree.rv.actor.*;
import info.quadtree.rv.graphics.SGF;

public class NPCDesc implements Serializable {

	public enum NPCType {
		BasicEnemy, Beacon, BossEnemy, EliteEnemy, FastEnemy, Mayor, MissileEnemy, PlotBarrier, ShieldEnemy, Shop1, Shop2, Shop3, Tech, Villager
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -201947023848124036L;

	public NPCType type;

	public float x, y;

	public NPCDesc(NPCType type, float x, float y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public void create(ArrayList<Actor> actorList) {
		switch (type) {
		case Shop1:
			actorList.add(new EvilShopkeeperRobot(x, y));
			break;
		case Shop2:
			actorList.add(new Shopkeeper2(x, y));
			break;
		case Shop3:
			actorList.add(new Shopkeeper3(x, y));
			break;
		case Tech:
			actorList.add(new Tech(x, y));
			break;
		case Mayor:
			actorList.add(new Mayor(x, y));
			break;
		case Villager:
			actorList.add(new Villager(x, y));
			break;
		case BasicEnemy:
			actorList.add(new BasicEnemy(x, y));
			break;
		case BossEnemy:
			actorList.add(new BossEnemy(x, y));
			break;
		case Beacon:
			actorList.add(new Beacon(x, y));
			break;
		case FastEnemy:
			actorList.add(new FastEnemy(x, y));
			break;
		case EliteEnemy:
			actorList.add(new EliteEnemy(x, y));
			break;
		case MissileEnemy:
			actorList.add(new MissileEnemy(x, y));
			break;
		case ShieldEnemy:
			actorList.add(new ShieldEnemy(x, y));
			break;
		case PlotBarrier:
			actorList.add(new PlotBarrier(x, y));
			break;
		}

	}

	public void render() {
		SGF.getInstance().renderImage("BaseA0032", x, y, 1, 1, 0, true);
		SGF.getInstance().renderText(type.toString(), x, y, 255, 255, 255, true, 1);
	}
}
