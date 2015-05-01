package es.remara.notacommonsnake.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import es.remara.notacommonsnake.R;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{
	
//---------------------------------------------
// VARIABLES
//---------------------------------------------
		
	private MenuScene menuChildScene;
	
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;	
	private final int MENU_ACHIVEMENTS = 2;
	
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
//		
//		playMenuItem.detachSelf();
//		playMenuItem.dispose();
//		
//		optionsMenuItem.detachSelf();
//		optionsMenuItem.dispose();
		
		this.detachSelf();
		this.dispose();
	}
	
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		
		switch(pMenuItem.getID())
		{
			case MENU_PLAY:
				//Load Game Scene!
				SceneManager.getInstance().createSnakeGameScene();
				return true;
			case MENU_OPTIONS:
				SceneManager.getInstance().createArkanoidScene();
				return true;
			case MENU_ACHIVEMENTS:
				SceneManager.getInstance().createWorkInProgress();
			default:
				return false;
		}

	}
	
	
//---------------------------------------------
// CLASS LOGIC
//---------------------------------------------

	
	private void createBackground()	{
//		Background fondoMenu = new Background(Color.CYAN);
//		setBackground(fondoMenu);
		attachChild(new Sprite(camera.getWidth()/2, camera.getHeight()/2, resourcesManager.background_grass_region, vbom));
	}

	
	
	private void createMenuScene(){
		menuChildScene = new MenuScene(camera);		
		menuChildScene.buildAnimations();

		menuChildScene.setPosition(camera.getWidth()/2 , camera.getHeight()/2 );
		
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.1f, 1.2f);
		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.1f, 1);
		final IMenuItem achivementsMenuItem = new ScaleMenuItemDecorator (new SpriteMenuItem(MENU_ACHIVEMENTS, resourcesManager.achivements_region, vbom), 1.2f, 1 );
        final Text textPlay = new Text(0, 0, resourcesManager.font , activity.getString(R.string.play) , new TextOptions(HorizontalAlign.CENTER), this.vbom);
        final Text textOptions = new Text(0, 0, resourcesManager.font , activity.getString(R.string.options) , new TextOptions(HorizontalAlign.LEFT), this.vbom);
        final Text textAchivements = new Text(0, 0, resourcesManager.font , activity.getString(R.string.achivements) , new TextOptions(HorizontalAlign.RIGHT), this.vbom);

        menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.attachChild(textPlay);
		menuChildScene.addMenuItem(optionsMenuItem);		
		menuChildScene.attachChild(textOptions);
		menuChildScene.addMenuItem(achivementsMenuItem);
		menuChildScene.attachChild(textAchivements);
		menuChildScene.setBackgroundEnabled(false);		
		
		optionsMenuItem.setPosition( optionsMenuItem.getX() - (camera.getWidth()/3), optionsMenuItem.getY() );
		playMenuItem.setPosition( playMenuItem.getX(), playMenuItem.getY() );
		achivementsMenuItem.setPosition( achivementsMenuItem.getX() + (camera.getWidth()/3), achivementsMenuItem.getY() );
		textPlay.setPosition(playMenuItem.getX(), playMenuItem.getY() - playMenuItem.getHeight() / 2 - 40);
		textOptions.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - optionsMenuItem.getHeight() / 2 - 40);
		textAchivements.setPosition(achivementsMenuItem.getX(), achivementsMenuItem.getY() - achivementsMenuItem.getHeight() / 2 - 40);
		
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	}

}
