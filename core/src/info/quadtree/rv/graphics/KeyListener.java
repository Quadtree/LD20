package info.quadtree.rv.graphics;

public interface KeyListener {

	/**
	 * Invoked when a key has been pressed. See the class description for
	 * {@link KeyEvent} for a definition of a key pressed event.
	 */
	public void keyPressed(KeyEvent e);

	/**
	 * Invoked when a key has been released. See the class description for
	 * {@link KeyEvent} for a definition of a key released event.
	 */
	public void keyReleased(KeyEvent e);

	/**
	 * Invoked when a key has been typed. See the class description for
	 * {@link KeyEvent} for a definition of a key typed event.
	 */
	public void keyTyped(KeyEvent e);
}
