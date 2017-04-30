package info.quadtree.rv;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import info.quadtree.rv.actor.Actor;
import sgf.SGF;

public class Dialog extends Actor implements KeyListener {
	ArrayList<String> messages;
	protected int shown = 1;

	public Dialog() {
		messages = new ArrayList<String>();

		SGF.getInstance().addKeyListener(this);
		Game.s.dialogUp = true;
	}

	public void addMessage(String s) {
		messages.add(s);
	}

	@Override
	public boolean keep() {
		return shown <= messages.size();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (Game.s.helpScreenUp || Game.s.titleScreenUp)
			return;
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			shown++;
			if (keep())
				Game.s.playAudio("dialog");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("inventoryback", 512, 600, 1024, 336, 0, false);

		for (int i = 0; i < Math.min(shown, messages.size()); i++) {
			SGF.getInstance().renderText(messages.get(i), 20, 620 - 168 + i * 22, 255, 255, 255, false, 18);
		}

		SGF.getInstance().renderText("Press ENTER for more", 20, 748, 255, 255, 0, false, 12);

		super.render();
	}

	@Override
	public void update() {

	}
}
