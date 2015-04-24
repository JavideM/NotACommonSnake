package es.remara.notacommonsnake.object;

import java.util.Random;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;

public class Food extends Sprite{
	


	public enum FoodType{
		X2,
		AUG_SPEED,
		REDUC_SPEED,
		CHG_GAME_MODE,
		GHOST_MODE,
		SUPER_GROW,
		INV_CONTROLS,
		NORMAL;
	    public static FoodType getRandom() {
			Random random = new Random();
	        return values()[random.nextInt(values().length)];
	    }
		
	}
	
	private FoodType type;
	
	public Food(ITextureRegion texture, VertexBufferObjectManager vbom){
		super(10,10,texture, vbom);
		setRandomPosition();
		setRandomType();
	}

	public FoodType getType() {
		return type;
	}

	private void setRandomType() {
		this.type = FoodType.getRandom();
		switch(this.type){
			case AUG_SPEED:
				break;
			case CHG_GAME_MODE:
				break;
			case GHOST_MODE:
				break;
			case INV_CONTROLS:
				break;
			case REDUC_SPEED:
				break;
			case SUPER_GROW:
				break;
			case X2:
				break;
			default:
				break; 
		
		}
	}

	private void setRandomPosition() {
		setX(MathUtils.random(1, 38)*20 + 10);
		setY(MathUtils.random(1, 22)*20 + 10);
	}
	
	public void setRandom(){
		setRandomPosition();
		setRandomType();
	}
	
}
