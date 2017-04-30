package rv.item;

import rv.Game;

public class Core extends Item {

	public Core(float x, float y) {
		super(x, y, false);
	}

	@Override
	public String getGraphicName() {
		return "core";
	}

	@Override
	public void pickedUp() {
		if(Game.s.plot < 3) Game.s.plot = 3;
		super.pickedUp();
	}
}
