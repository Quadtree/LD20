package rv.item;

import rv.Game;
import rv.actor.Player;
import rv.projectile.Shockbomb;

public class ShockbombLauncher extends Item {

	int fireCooldown = 0;
	
	public ShockbombLauncher(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public ShockbombLauncher() {
	}

	@Override
	public String getGraphicName() {
		return "shockbomblauncher";
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
			fireCooldown = 180;
			Game.s.actors.add(new Shockbomb(user.getPosition().x, user.getPosition().y, user.getAimFacing(), 10.f, user.getPosition().x, user.getPosition().y));
			Game.s.playAudio("firexplosion");
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Shockwave Launcher";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Fires a shockwave outward that", "deals area of effect damage.", "Low fire rate"};
	}

	@Override
	public float getValue() {
		return 1000;
	}
	
	
}
