package es.remara.notacommonsnake.scene;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class GameSnakeScene extends BaseScene {
	
	
	
	@Override
	public void createScene() {
		
		
	}

	@Override
	public void onBackKeyPressed() {
		return;		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SNAKE;

	}

	@Override
	public void disposeScene() {
		//TODO detach y depose sprites
		this.detachSelf();
		this.dispose();
		
	}

}
