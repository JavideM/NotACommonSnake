package es.remara.notacommonsnake.scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.adt.color.Color;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
		
	private MenuScene menuChildScene;
		
	//---------------------------------------------
	// METHODS FROM PARENT
	//---------------------------------------------
	@Override
	public void createScene() {
		createBackground();
		createMenuScene();
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------
	
	
	private void createBackground()	{
		Background fondoMenu = new Background(0.09804f, 0.6274f, 0.8784f);
		setBackground(fondoMenu);

	}

	
	
	private void createMenuScene(){
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(camera.getHeight() / 2, camera.getWidth()/2);
		
		Rectangle playButton = new Rectangle(-camera.getWidth()/3, 0, 80, 80, vbom);
		Rectangle achivementsButton = new Rectangle(0, 0, 80, 80, vbom);
		
		
		menuChildScene.attachChild(playButton);
		menuChildScene.attachChild(achivementsButton);
				
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	}

}
