package info.quadtree.rv.actor;


public class ShopkeeperRobot extends AlliedRobot{

	public ShopkeeperRobot(float x, float y) {
		super(x, y);
	}

	@Override
	protected String getTopPrefix() {
		return "topsk";
	}
}
