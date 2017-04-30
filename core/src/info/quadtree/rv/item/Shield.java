package rv.item;

public class Shield extends Item {

	public Shield(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public Shield() {
	}

	@Override
	public float getShieldMod() {
		return 3;
	}

	@Override
	public String getGraphicName() {
		return "shielditem";
	}

	@Override
	public String getName() {
		return "Shield Module";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Gives you a shield of strength","3 when equipped."};
	}

	@Override
	public float getValue() {
		return 500;
	}
}
