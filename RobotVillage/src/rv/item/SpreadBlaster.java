package rv.item;

import rv.Game;
import rv.actor.Player;
import rv.projectile.Bolt;

public class SpreadBlaster extends Item {

	int fireCooldown = 0;
	
	public SpreadBlaster(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	@Override
	public String getGraphicName() {
		return "spreadblaster";
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
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing(), 10.f, "bolt1", false, 0.7f));
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing() + 0.2f, 10.f, "bolt1", false, 0.7f));
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing() - 0.2f, 10.f, "bolt1", false, 0.7f));
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing() + 0.4f, 10.f, "bolt1", false, 0.7f));
			Game.s.actors.add(new Bolt(user.getPosition().x, user.getPosition().y, user.getAimFacing() - 0.4f, 10.f, "bolt1", false, 0.7f));
			Game.s.playAudio("boltfire");
		}
		
		super.tryUse(user);
	}

	@Override
	public String getName() {
		return "Spread Blaster";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Fires five bolts outward in a wide arc","Fires once per second"};
	}

	@Override
	public float getValue() {
		return 500;
	}
}
