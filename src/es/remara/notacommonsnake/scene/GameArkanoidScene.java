package es.remara.notacommonsnake.scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class GameArkanoidScene extends BaseScene {

	// Provisional. Se cambiara por sprites (supongo...)
	private Rectangle platform, ground, roof, left_wall, right_wall;

	PhysicsWorld physicsWorld;

	@Override
	public void createScene() {
		setBackground(new Background(0, 100, 200));
		physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		registerUpdateHandler(physicsWorld);
	}

	public void createWalls() {
		roof = new Rectangle(resourcesManager.camera.getWidth() / 2,
				resourcesManager.camera.getHeight() - 3,
				resourcesManager.camera.getWidth(), 6,
				engine.getVertexBufferObjectManager());
		roof.setColor(50, 30, 0);
		ground = new Rectangle(resourcesManager.camera.getWidth() / 2, 0,
				resourcesManager.camera.getWidth(), 12,
				engine.getVertexBufferObjectManager());
		ground.setColor(50, 30, 0);
		right_wall = new Rectangle(resourcesManager.camera.getWidth() - 3,
				resourcesManager.camera.getHeight() / 2, 6,
				resourcesManager.camera.getHeight(),
				engine.getVertexBufferObjectManager());
		right_wall.setColor(50, 30, 0);
		left_wall = new Rectangle(0, resourcesManager.camera.getHeight() / 2,
				12, resourcesManager.camera.getHeight(),
				engine.getVertexBufferObjectManager());
		left_wall.setColor(50, 30, 0);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}
}
