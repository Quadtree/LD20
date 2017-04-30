package info.quadtree.rv.item;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.Player;
import info.quadtree.rv.projectile.Bolt;

public class VulcanCannon extends Item {

	int fireCooldown = 0;
	
	public VulcanCannon(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public VulcanCannon() {
	}

	@Override
	public String getGraphicName() {
		return "vulcancannon";
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
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing(), 15.f, "vulcanshot", false, 1.f/3));
			Game.s.playAudio("vulcanfire");
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Vulcan Cannon";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Deadly rapid fire weapon."};
	}

	@Override
	public float getValue() {
		return 780;
	}
	
	
}
