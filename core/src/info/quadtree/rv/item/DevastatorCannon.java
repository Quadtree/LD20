package info.quadtree.rv.item;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.Player;
import info.quadtree.rv.projectile.Bolt;

public class DevastatorCannon extends Item {

	int fireCooldown = 0;
	
	public DevastatorCannon(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public DevastatorCannon() {
	}

	@Override
	public String getGraphicName() {
		return "devastator";
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
			fireCooldown = 90;
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing(), 10.f, "devastatorshot", false, 8.f));
			Game.s.playAudio("firexplosion");
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Devastator Cannon";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Slow firing gun that shoots", "a single damaging shell."};
	}

	@Override
	public float getValue() {
		return 900;
	}
	
	
}
