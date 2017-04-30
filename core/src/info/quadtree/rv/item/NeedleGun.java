package info.quadtree.rv.item;

import info.quadtree.rv.Game;
import info.quadtree.rv.actor.Player;
import info.quadtree.rv.projectile.Needle;

public class NeedleGun extends Item {

	int fireCooldown = 0;
	
	public NeedleGun(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public NeedleGun() {
	}

	@Override
	public String getGraphicName() {
		return "needlegun";
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
			fireCooldown = 60;
			Game.s.actors.add(new Needle(user.getPosition().x, user.getPosition().y, user.getAimFacing()));
			Game.s.playAudio("needlefire");
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Needle Gun";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Fires a fast moving shot that passes", "through enemies, damaging them in a line."};
	}

	@Override
	public float getValue() {
		return 800;
	}
	
	
}
