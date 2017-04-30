package info.quadtree.rv.item;

public class Turbo extends Item {

	public Turbo(float x, float y, boolean forSale) {
		super(x, y, forSale);
	}

	public Turbo() {
	}

	@Override
	public float getSpeedMod() {
		return 1.35f;
	}

	@Override
	public String getGraphicName() {
		return "turboitem";
	}

	@Override
	public String getName() {
		return "Turbo Module";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Increases move speed by 35% when","equipped."};
	}

	@Override
	public float getValue() {
		return 300;
	}
}
