package rv.item;

import rv.Game;
import rv.actor.Player;
import rv.projectile.Bolt;

public class BoltGun extends Item {

	int fireCooldown = 0;
	
	public BoltGun(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public BoltGun() {
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
			fireCooldown = 20;
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing(), 10.f, "bolt1", false, 1.f));
			Game.s.playAudio("boltfire");
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Bolt Cannon";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Fires 3 small shots per second."};
	}

	@Override
	public float getValue() {
		return 180;
	}
	
	
}
