package info.quadtree.rv.item;

import info.quadtree.rv.Game;

public class RepairCartridge extends Item {

	int charges;
	
	public RepairCartridge(float x, float y, boolean forSale, int charges) {
		super(x, y, forSale);
		this.charges = charges;
	}
	
	public void setCharges(int charges) {
		this.charges = charges;
	}

	@Override
	public String getGraphicName() {
		return "repaircart";
	}

	@Override
	public void pickupSequence() {
		Game.s.player.addRepairCartridge();
		charges--;
	}

	@Override
	public boolean keep() {
		return charges > 0;
	}

	@Override
	public String getName() {
		return "Repair Cartridge";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Heals you for 3","Press R to use"};
	}

	@Override
	public float getValue() {
		return 160;
	}
	
	
}
