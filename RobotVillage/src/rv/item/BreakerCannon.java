package rv.item;

import rv.Game;
import rv.actor.Player;
import rv.projectile.BreakerShot;

public class BreakerCannon extends Item {

	int fireCooldown;
	
	public BreakerCannon(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public BreakerCannon() {
	}

	@Override
	public String getGraphicName() {
		return "breakercannon";
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
			Game.s.actors.add(new BreakerShot(user.getPosition().x, user.getPosition().y, user.getAimFacing()));
			Game.s.actors.add(new BreakerShot(user.getPosition().x, user.getPosition().y, user.getAimFacing() + 0.2f));
			Game.s.actors.add(new BreakerShot(user.getPosition().x, user.getPosition().y, user.getAimFacing() - 0.2f));
			Game.s.actors.add(new BreakerShot(user.getPosition().x, user.getPosition().y, user.getAimFacing() + 0.4f));
			Game.s.actors.add(new BreakerShot(user.getPosition().x, user.getPosition().y, user.getAimFacing() - 0.4f));
			Game.s.playAudio("firexplosion");
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Breaker Cannon";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Fires a volley of cannonballs that ","knock enemies backward."};
	}

	@Override
	public float getValue() {
		return 500;
	}
}
