package es.remara.notacommonsnake.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class GameArkanoidScene extends BaseScene{

	PhysicsWorld physicsWorld;
	
	@Override
	public void createScene() {
		setBackground(new Background(0, 100, 200));
		physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		registerUpdateHandler(physicsWorld);
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
