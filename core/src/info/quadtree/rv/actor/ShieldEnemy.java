package info.quadtree.rv.actor;

import com.badlogic.gdx.physics.box2d.collision.FilterData;
import com.badlogic.gdx.physics.box2d.collision.shapes.CircleDef;
import info.quadtree.rv.Game;
import info.quadtree.rv.item.Shield;
import info.quadtree.rv.graphics.SGF;

public class ShieldEnemy extends BasicEnemy {

	public ShieldEnemy(float x, float y) {
		super(x, y);
		
		CircleDef cd = new CircleDef();
		cd.isSensor = true;
		cd.radius = 2.f;
		
		FilterData fd = new FilterData();
		fd.categoryBits = 1 << 3;
		fd.maskBits = 0xffffffff;
		
		body.createShape(cd).setFilterData(fd);
		
		reset();
	}
	
	@Override
	public void reset() {
		hp = 15;
	}

	@Override
	public void render() {
		super.render();
		
		SGF.getInstance().renderImage("bigshield", getPosition().x, getPosition().y, 4, 4, 0, true);
	}

	@Override
	public void notifyDestroyed() {
		if(Game.s.rand.nextInt(15) == 0) Game.s.actors.add(new Shield(body.getPosition().x, body.getPosition().y, false));
			
		Game.s.player.modMoney(45);
			
		super.notifyDestroyed();
	}
}
