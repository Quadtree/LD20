package info.quadtree.rv;

import info.quadtree.rv.actor.EvilShopkeeperRobot;
import info.quadtree.rv.graphics.KeyEvent;

public class FinalCinematic extends Dialog {

	EvilShopkeeperRobot subject;

	public FinalCinematic(EvilShopkeeperRobot subject) {
		this.subject = subject;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (shown >= 10) {
			if (subject.stage == 0) {
				subject.stage = 1;
			} else if (subject.stage == 2) {
				super.keyPressed(e);
			}
		} else {
			super.keyPressed(e);
		}
	}

	@Override
	public void notifyDestroyed() {
		subject.stage = 3;
		super.notifyDestroyed();
	}
}
