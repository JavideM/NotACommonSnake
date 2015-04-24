package es.remara.notacommonsnake.object;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;

public class Food extends Rectangle{
	


	public enum FoodType{
		X2,
		AUG_SPEED,
		REDUC_SPEED,
		CHG_GAME_MODE,
		GHOST_MODE,
		SUPER_GROW,
		INV_CONTROLS;
		
	    public static FoodType getRandom() {
	        return values()[(int) (Math.random() * values().length)];
	    }
		
	}
	
	private FoodType type;
	
	public Food(VertexBufferObjectManager vbom){
		super(10,10,16,16, vbom);
		setColor(Color.BLACK);
		
		setRandomPosition();
		setRandomType();
	}

	public FoodType getType() {
		return type;
	}

	public void setRandomType() {
		this.type = FoodType.getRandom();
	}

	public void setRandomPosition() {
		setY(MathUtils.random(1, 38)*20 + 10);
		setX(MathUtils.random(1, 22)*20 + 10);

	}
}
