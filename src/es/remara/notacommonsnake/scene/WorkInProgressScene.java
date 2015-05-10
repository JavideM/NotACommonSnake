package es.remara.notacommonsnake.scene;


import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import es.remara.notacommonsnake.base.BaseGameScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class WorkInProgressScene extends BaseGameScene{

	private Sprite wip_sprite;
	
	@Override
	public void createScene() {
		setBackground(new Background(Color.CYAN));
		createHUD();
		
		wip_sprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2, resourcesManager.wip_region, vbom);
		attachChild(wip_sprite);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine, this);	
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_IN_PROGRESS;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		wip_sprite.detachSelf();
		wip_sprite.dispose();
		this.detachSelf();
		this.dispose();	
	}

}
