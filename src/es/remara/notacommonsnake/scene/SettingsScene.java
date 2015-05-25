package es.remara.notacommonsnake.scene;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

import es.remara.notacommonsnake.R;
import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.model.Session;

public class SettingsScene extends BaseScene {

	/*
	 * Parameters
	 */
	private Entity profilesPannel;
	private Entity difficultiesPannel;
	private Entity soundsPannel;
	private Sprite btndifficulties;
	private Sprite btnprofile;
	private Sprite btnsound;
	private Sprite btnback;

	private List<Text> textlist;

	private final float top_ini_position = camera.getHeight()
			- (camera.getHeight() / 12 + camera.getHeight() / 12);
	private final float bottom_ini_position = camera.getHeight()
			- (camera.getHeight() / 12 + camera.getHeight() / 12 * 8);
	

	@Override
	public void createScene() {
		createBackground();
		createControls();
		createProfilesPannel();
		createDifficultiesPannel();
		createSoundsPannel();

	}

	// Background
	private void createBackground() {
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
	}

	private void createControls() {
		btnprofile = new Sprite(camera.getWidth() / 3, 35,
				resourcesManager.profile_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				soundsPannel.setVisible(false);
				difficultiesPannel.setVisible(false);
				profilesPannel.setVisible(true);
				return true;
			}
		};
		btndifficulties = new Sprite(camera.getWidth() / 2, 35,
				resourcesManager.gamepad_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				soundsPannel.setVisible(false);
				difficultiesPannel.setVisible(true);
				profilesPannel.setVisible(false);
				return true;
			}
		};
		btnsound = new Sprite(2 * camera.getWidth() / 3, 35,
				resourcesManager.music_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				soundsPannel.setVisible(true);
				difficultiesPannel.setVisible(false);
				profilesPannel.setVisible(false);
				return true;
			}
		};
		btnback = new Sprite(45, 35, resourcesManager.back_region, vbom){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				onBackKeyPressed();
				return true;
			}
		};
		attachChild(btnprofile);
		registerTouchArea(btnprofile);
		attachChild(btndifficulties);
		registerTouchArea(btndifficulties);
		attachChild(btnsound);
		registerTouchArea(btnsound);
		attachChild(btnback);
		registerTouchArea(btnback);
	}

	private void createProfilesPannel() {
		profilesPannel = new Entity();
		/*
		 * Pannel
		 */
		Sprite pannel = new Sprite(camera.getWidth() / 2,
				camera.getHeight() / 2 + 30,
				resourcesManager.orange_pannel_region, vbom);
		profilesPannel.attachChild(pannel);
		attachChild(profilesPannel);
		profilesPannel.setVisible(true);

		/*
		 * Pannel Content
		 */
		// Title
		Text text = new Text(camera.getWidth() / 2, camera.getHeight()
				- camera.getHeight() / 12, resourcesManager.fonttitle,
				activity.getString(R.string.profile_title),
				new TextOptions(HorizontalAlign.CENTER), this.vbom);
		profilesPannel.attachChild(text);
	}

	private void createSoundsPannel() {
		soundsPannel = new Entity();
		/*
		 * Color Pannel
		 */
		Sprite pannel = new Sprite(camera.getWidth() / 2,
				camera.getHeight() / 2 + 30,
				resourcesManager.salmon_pannel_region, vbom);
		soundsPannel.attachChild(pannel);
		soundsPannel.setVisible(false);
		/*
		 * Pannel Content
		 */
		// Title
		Text text = new Text(camera.getWidth() / 2, camera.getHeight()
				- camera.getHeight() / 12, resourcesManager.fonttitle,
				activity.getString(R.string.sound_title), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		soundsPannel.attachChild(text);
		attachChild(soundsPannel);
	}

	private void createDifficultiesPannel() {
		difficultiesPannel = new Entity();
		/*
		 * Color Pannel
		 */
		Sprite pannel = new Sprite(camera.getWidth() / 2,
				camera.getHeight() / 2 + 30,
				resourcesManager.blue_pannel_region, vbom);
		difficultiesPannel.attachChild(pannel);
		attachChild(difficultiesPannel);
		difficultiesPannel.setVisible(false);

		/*
		 * Pannel Content
		 */
		// Title
		Text text = new Text(camera.getWidth() / 2, camera.getHeight()
				- camera.getHeight() / 12, resourcesManager.fonttitle,
				activity.getString(R.string.difficulty_title), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		difficultiesPannel.attachChild(text);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine, this);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SETTINGS;
	}

	@Override
	public void disposeScene() {
		detachChildren();
		this.detachSelf();
		this.dispose();
	}

}