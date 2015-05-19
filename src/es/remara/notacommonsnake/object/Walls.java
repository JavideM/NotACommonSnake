package es.remara.notacommonsnake.object;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import es.remara.notacommonsnake.scene.GameSnakeScene;

import android.app.Activity;

public class Walls extends Entity {

	private ArrayList<Entity> walls = new ArrayList<Entity>();

	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_NUMBER = "number";
	private static final String TAG_ENTITY_ATTRIBUTE_DIRECTION_X = "directionx";
	private static final String TAG_ENTITY_ATTRIBUTE_DIRECTION_Y = "directiony";

	public Walls(int level_id, final ITextureRegion txtWall,
			final GameSnakeScene scene, Activity activity,
			final VertexBufferObjectManager vbom) {

		SimpleLevelLoader levelloader = new SimpleLevelLoader(vbom);
		levelloader
				.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(
						LevelConstants.TAG_LEVEL) {
					public IEntity onLoadEntity(
							final String pEntityName,
							final IEntity pParent,
							final Attributes pAttributes,
							final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData)
							throws IOException {

						return scene;
					}
				});
		levelloader
				.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(
						TAG_ENTITY) {
					public IEntity onLoadEntity(
							final String pEntityName,
							final IEntity pParent,
							final Attributes pAttributes,
							final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData)
							throws IOException {
						final int x = SAXUtils.getIntAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_X);
						final int y = SAXUtils.getIntAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
						// final String type =
						// SAXUtils.getAttributeOrThrow(pAttributes,
						// TAG_ENTITY_ATTRIBUTE_TYPE);
						final int number = SAXUtils.getIntAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_NUMBER);
						final int direction_x = SAXUtils
								.getIntAttributeOrThrow(pAttributes,
										TAG_ENTITY_ATTRIBUTE_DIRECTION_X);
						final int direction_y = SAXUtils
								.getIntAttributeOrThrow(pAttributes,
										TAG_ENTITY_ATTRIBUTE_DIRECTION_Y);

						final Entity levelObject = new Entity();

						for (int i = 0; i < number; i++) {
							Sprite wall_section = new Sprite(x, y, txtWall,
									vbom);
							wall_section.setPosition(x + i * direction_x * 20,
									y + i * direction_y * 20);
							levelObject.attachChild(wall_section);
							walls.add(wall_section);
						}

						// Entity levelObject = new Sprite(x,y, txtWall, vbom);

						return levelObject;
					}
				});
		levelloader.loadLevelFromAsset(activity.getAssets(), "level/"
				+ level_id + ".lvl");

	}

	public boolean is_there_a_wall(Entity object) {
		for (Entity entity : this.walls) {
			if (entity.getX() == object.getX()
					&& entity.getY() == object.getY())
				return true;
		}
		return false;
	}

	public boolean is_there_a_wall(float x, float y) {
		for (Entity entity : this.walls) {
			return entity.getX() == x && entity.getY() == y;
		}
		return false;
	}

	public void disposeChilds() {
		for (Entity entity : this.walls)
			entity.detachSelf();

	}

}
