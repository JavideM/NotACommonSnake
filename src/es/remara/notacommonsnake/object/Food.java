package es.remara.notacommonsnake.object;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import es.remara.notacommonsnake.manager.ResourcesManager;

public class Food extends Entity{
	
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
	private ResourcesManager resourcesManager;
	private VertexBufferObjectManager vbom;
	
	public Food(FoodType type, ResourcesManager resourcesmanager, VertexBufferObjectManager vbom){
		this.resourcesManager = resourcesmanager;
		this.vbom = vbom;
		this.type = type;
		
		Sprite foodtype = new Sprite(getX(), getY(), setRandomType(), this.vbom);
		attachChild(foodtype);
		setRandomPosition();
	}

	public FoodType getType() {
		return type;
	}

	private ITextureRegion setRandomType() {
		switch(this.type){
			case AUG_SPEED:
				return resourcesManager.food_AUG_SPEED_region;
			case CHG_GAME_MODE:
				return resourcesManager.food_CHG_GAME_MODE_region;
			case GHOST_MODE:
				return resourcesManager.food_GHOST_MODE_region;
			case INV_CONTROLS:
				return resourcesManager.food_INV_CONTROLS_region;
			case REDUC_SPEED:
				return resourcesManager.food_REDUC_SPEED_region;
			case SUPER_GROW:
				return resourcesManager.food_SUPER_GROW_region;
			case X2:
				return resourcesManager.food_X2_region;
			case NORMAL:
				//Añadir nueva imagen
				return resourcesManager.food_X2_region;
			default:
				//Añadir Normal
				return resourcesManager.food_X2_region;
		
		}
	}

	private void setRandomPosition() {
		setX(MathUtils.random(1, 38)*20 + 10);
		setY(MathUtils.random(1, 22)*20 + 10);
	}
		
}
