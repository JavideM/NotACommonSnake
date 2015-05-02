package es.remara.notacommonsnake.object;


import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import es.remara.notacommonsnake.manager.ResourcesManager;

public class SpecialFood extends Food{
	
	public SpecialFood(FoodType type, Walls walls, ResourcesManager resourcesmanager, VertexBufferObjectManager vbom){
		super(type, walls, resourcesmanager, vbom);
		
		Sprite foodtype = new Sprite(getX(), getY(), setRandomType(), this.vbom);
		attachChild(foodtype);
		setRandomPosition(walls);
	}

	private void setRandomPosition(Walls walls) {
		do{
			setX(MathUtils.random(1, 38)*20 + 10);
			setY(MathUtils.random(1, 22)*20 + 10);
		}while(walls.is_there_a_wall(this));
	}
		
}
