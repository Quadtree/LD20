package info.quadtree.rv.item;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.Player;
import info.quadtree.rv.projectile.Bolt;

public class CheatGun extends Item {

	int fireCooldown = 0;
	
	public CheatGun(float x, float y) {
		super(x, y, false);
	}

	@Override
	public String getGraphicName() {
		return "boltgun";
	}

	@Override
	public void update() {
		if(fireCooldown > 0) fireCooldown--;
		super.update();
	}

	@Override
	public void tryUse(Player user) {
		if(fireCooldown <= 0)
		{
			user.setMuzzleBlastTimer(3);
			fireCooldown = 4;
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing(), 20.f, "bolt1", false, 1000.f));
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Cheat Gun";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"CHEATER!!!"};
	}

	@Override
	public float getValue() {
		return 9001;
	}
	
	
}
