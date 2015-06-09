package es.remara.notacommonsnake.scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;

import es.remara.notacommonsnake.R;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private MenuScene menuChildScene;

	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_ACHIVEMENTS = 2;
	private final int MENU_FACEBOOK = 3;
	private final int MENU_YOUTUBE = 4;

	// ---------------------------------------------
	// METHODS FROM PARENT
	// ---------------------------------------------
	@Override
	public void createScene() {
		createBackground();
		createMenuScene();
	}


	@Override
	public void onBackKeyPressed() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder alert = new AlertDialog.Builder(activity);
				alert.setCancelable(false);
				alert.setMessage(activity.getString(R.string.exitmessage));
				alert.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						resourcesManager.music.pause();
						System.exit(0);
					}
				});

				alert.setNegativeButton("NO", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});

				alert.show();
			}
		});
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		//
		// playMenuItem.detachSelf();
		// playMenuItem.dispose();
		//
		// optionsMenuItem.detachSelf();
		// optionsMenuItem.dispose();

		this.detachSelf();
		this.dispose();
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			// Load Game Scene!
			SceneManager.getInstance().createSnakeGameScene();
			return true;
		case MENU_OPTIONS:
			SceneManager.getInstance().createSettingsScene();
			return true;
		case MENU_ACHIVEMENTS:
			SceneManager.getInstance().createAchievementsRecordsStatsScene();
			return true;
		case MENU_FACEBOOK:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/Not-A-Common-Snake/110908249243655?fref=ts"));
			activity.startActivity(browserIntent);
			return true;
		case MENU_YOUTUBE:
			browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCR3TP7SU5g-0UzagJeIVsWg"));
			activity.startActivity(browserIntent);
		default:
			return false;
		}

	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createBackground() {
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
	}

	private void createMenuScene() {
		menuChildScene = new MenuScene(camera);
		menuChildScene.buildAnimations();

		menuChildScene.setPosition(camera.getWidth() / 2,
				camera.getHeight() / 2);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region,
						vbom), 1.1f, 1);
		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_OPTIONS,
						resourcesManager.options_region, vbom), 1.1f, 1);
		final IMenuItem achivementsMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_ACHIVEMENTS,
						resourcesManager.achivements_region, vbom), 1.2f, 1);
		final IMenuItem facebookMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_FACEBOOK,
						resourcesManager.facebook_region, vbom), 1.2f, 1);
		final IMenuItem youtubeMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_YOUTUBE,
						resourcesManager.youtube_region, vbom), 1.2f, 1);

		final Text textPlay = new Text(0, 0, resourcesManager.font,
				activity.getString(R.string.play), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		final Text textOptions = new Text(0, 0, resourcesManager.font,
				activity.getString(R.string.options), new TextOptions(
						HorizontalAlign.LEFT), this.vbom);
		final Text textAchivements = new Text(0, 0, resourcesManager.font,
				activity.getString(R.string.achivements), new TextOptions(
						HorizontalAlign.RIGHT), this.vbom);

		textPlay.setScale(2);
		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.attachChild(textPlay);
		menuChildScene.addMenuItem(optionsMenuItem);
		menuChildScene.attachChild(textOptions);
		menuChildScene.addMenuItem(achivementsMenuItem);
		menuChildScene.attachChild(textAchivements);
		menuChildScene.addMenuItem(facebookMenuItem);
		menuChildScene.addMenuItem(youtubeMenuItem);
		menuChildScene.setBackgroundEnabled(false);
		menuChildScene.attachChild(new Sprite(camera.getWidth()/2 - 400, camera.getHeight() / 8
				+ playMenuItem.getHeight() / 2, resourcesManager.title_region,
				vbom));
		
		optionsMenuItem.setPosition(optionsMenuItem.getX()
				- (camera.getWidth() / 3), optionsMenuItem.getY());
		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY());
		achivementsMenuItem.setPosition(
				achivementsMenuItem.getX() + (camera.getWidth() / 3),
				achivementsMenuItem.getY());
		facebookMenuItem.setPosition(-camera.getWidth()/2 + 33, -camera.getHeight()/2 + 33);
		youtubeMenuItem.setPosition(-camera.getWidth()/2 + 65, -camera.getHeight()/2 + 32);
		textPlay.setPosition(playMenuItem.getX(), playMenuItem.getY()
				- playMenuItem.getHeight() / 2 - 40);
		textOptions.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY()
				- optionsMenuItem.getHeight() / 2 - 40);
		textAchivements.setPosition(achivementsMenuItem.getX(),
				achivementsMenuItem.getY() - achivementsMenuItem.getHeight()
						/ 2 - 40);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

}
