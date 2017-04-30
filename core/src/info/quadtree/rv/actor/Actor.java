package rv.actor;

public abstract class Actor {
	public void update(){}
	public void render(){}
	public boolean keep(){ return true; }
	public void notifyDestroyed(){}
	public void reset(){}
}
