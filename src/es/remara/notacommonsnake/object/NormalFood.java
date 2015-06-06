package es.remara.notacommonsnake.object;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import es.remara.notacommonsnake.manager.ResourcesManager;

public class NormalFood extends Food {

	public NormalFood(Walls walls, ResourcesManager resourcesmanager,
			VertexBufferObjectManager vbom) {
		super(FoodType.NORMAL, walls, resourcesmanager, vbom);

		Sprite foodtype = new Sprite(getX(), getY(),
				resourcesmanager.food_SUPER_GROW_region, this.vbom);
		attachChild(foodtype);
	}

}
