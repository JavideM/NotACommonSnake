package es.remara.notacommonsnake.object;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.object.Food.FoodType;

public class Foods extends Entity {

	private ArrayList<NormalFood> normalfoods = new ArrayList<NormalFood>();
	private SpecialFood specialfood;
	private Food eatenfood;

	private ResourcesManager resourcesManager;
	private VertexBufferObjectManager vbom;
	private Walls walls;

	public Foods(Walls walls, ResourcesManager resourcesmanager,
			VertexBufferObjectManager vbom) {
		this.resourcesManager = resourcesmanager;
		this.vbom = vbom;
		this.walls = walls;

		specialfood = new SpecialFood(FoodType.getRandom(), walls,
				resourcesmanager, vbom);
		attachChild(specialfood);
		specialfood.setPosition(0, 0);

		for (int i = 0; i < 5; i++) {
			NormalFood newfood = new NormalFood(walls, resourcesmanager, vbom);
			setRandomPosition(walls, newfood);
			normalfoods.add(newfood);
			attachChild(newfood);
		}

		SpecialFood newspecialfood = new SpecialFood(FoodType.getRandom(),
				walls, resourcesmanager, vbom);
		setRandomPosition(walls, newspecialfood);
		detachChild(specialfood);
		specialfood = newspecialfood;
		attachChild(specialfood);

	}

	public Food get_eatenfood() {
		return eatenfood;
	}

	private void setRandomPosition(Walls walls, Food food) {
		do {
			food.setX(MathUtils.random(1, 38) * 20 + 10);
			food.setY(MathUtils.random(1, 22) * 20 + 10);
		} while (walls.is_there_a_wall(food) || is_there_food(food));
	}

	public boolean is_there_food(Entity here) {
		for (NormalFood food : normalfoods) {
			if (food.getX() == here.getX() && food.getY() == here.getY()) {
				if (here.getClass().equals(Snake.class))
					food_got_eaten(food);
				return true;
			}
		}
		if (specialfood.getX() == here.getX()
				&& specialfood.getY() == here.getY()) {
			if (here.getClass().equals(Snake.class))
				food_got_eaten(specialfood);
			return true;
		}
		return false;
	}

	public boolean is_there_food(float hereX, float hereY, boolean is_snake) {
		for (NormalFood food : normalfoods) {
			if (food.getX() == hereX && food.getY() == hereY) {
				if (is_snake)
					food_got_eaten(food);
				return true;
			}
		}
		if (specialfood.getX() == hereX && specialfood.getY() == hereY) {
			if (is_snake)
				food_got_eaten(specialfood);
			return true;
		}
		return false;
	}

	private void food_got_eaten(NormalFood food) {
		eatenfood = food;

		NormalFood newfood = new NormalFood(walls, resourcesManager, vbom);
		setRandomPosition(walls, newfood);
		normalfoods.add(newfood);
		attachChild(newfood);

		normalfoods.remove(food);
		detachChild(eatenfood);
	}

	private void food_got_eaten(SpecialFood food) {
		eatenfood = food;
		if(!(eatenfood.getType() == FoodType.CHG_GAME_MODE)){
			SpecialFood newspecialfood = new SpecialFood(FoodType.getRandom(),
					walls, resourcesManager, vbom);
			setRandomPosition(walls, newspecialfood);
			detachChild(food);
			specialfood = newspecialfood;
			attachChild(specialfood);
		}
	}

	public void disposeChilds() {
		for (Entity nfood : normalfoods)
			nfood.detachSelf();
		specialfood.detachSelf();

	}

}
