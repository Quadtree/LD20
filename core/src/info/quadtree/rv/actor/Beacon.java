package info.quadtree.rv.actor;

import info.quadtree.rv.Dialog;
import info.quadtree.rv.Game;
import info.quadtree.rv.graphics.SGF;

public class Beacon extends AlliedRobot {

	float animFrame;
	
	public Beacon(float x, float y) {
		super(x, y);
		hp = Float.MAX_VALUE;
	}

	@Override
	public void startConversation() {
		Dialog d = new Dialog();
		
		if(Game.s.plot == 4)
		{
			d.addMessage("Huh. This isn't a big boss. Its some kind of beacon.");
			d.addMessage("You carefully study the beacon. It seems to be rebroadcasting signals from somewhere else");
			d.addMessage("The command signals appear to be coming from the town!");
			d.addMessage("You'd better get back there quick and figure out what is going on");
			
			Game.s.plot = 5;
		}
		
		Game.s.actors.add(d);
	}

	@Override
	public void update() {
		animFrame = (animFrame + 0.4f) % 20;
		super.update();
	}

	@Override
	public void render() {
		String frameName = Integer.toString((int)animFrame + 1);
		if(frameName.length() == 1)
			frameName = "000" + frameName;
		else
			frameName = "00" + frameName;
		
		SGF.getInstance().renderImage("beacon" + frameName, body.getPosition().x, body.getPosition().y, 2, 2, 0, true);
	}
}
