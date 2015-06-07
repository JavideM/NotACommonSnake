package es.remara.notacommonsnake.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

import es.remara.notacommonsnake.R;
import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class SettingsScene extends BaseScene {

	/*
	 * Parameters
	 */
	private Entity aboutPannel;
	private Entity soundsPannel;
	private Sprite btnabout;
	private Sprite btnsound;
	private Sprite btnback;


	@Override
	public void createScene() {
		createBackground();
		createControls();
		createAboutPannel();
		createSoundsPannel();

	}

	// Background
	private void createBackground() {
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
	}

	private void createControls() {
		btnabout = new Sprite(2*camera.getWidth() / 3, 35,
				resourcesManager.gamepad_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				soundsPannel.setVisible(false);
				aboutPannel.setVisible(true);
				return true;
			}
		};
		btnsound = new Sprite(camera.getWidth() / 3, 35,
				resourcesManager.music_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				soundsPannel.setVisible(true);
				aboutPannel.setVisible(false);
				return true;
			}
		};
		btnback = new Sprite(camera.getWidth() - 45, 35, resourcesManager.back_region, vbom){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				ResourcesManager.getInstance().unloadSettingsResources();
				disposeScene();
				SceneManager.getInstance().createArkanoidScene();
				return true;
			}
		};
		attachChild(btnabout);
		registerTouchArea(btnabout);
		attachChild(btnsound);
		registerTouchArea(btnsound);
		attachChild(btnback);
		registerTouchArea(btnback);
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
		soundsPannel.setVisible(true);
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
		//Main Content
		Text musictxt = new Text(camera.getWidth() / 4, camera.getHeight()/ 3, resourcesManager.fonttitle,
				activity.getString(R.string.music), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		soundsPannel.attachChild(musictxt);
		TiledSprite speaker = new TiledSprite(camera.getWidth()/4, 3*camera.getHeight()/5, resourcesManager.speaker_region, vbom){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(soundsPannel.isVisible() && pSceneTouchEvent.isActionDown()){
					if(this.getCurrentTileIndex() == 0){
						resourcesManager.music.pause();
						this.setCurrentTileIndex(1);
					}else{
						resourcesManager.music.play();
						resourcesManager.music.setLooping(true);
						this.setCurrentTileIndex(0);
					}
				}
				return true;
			}
		};
		registerTouchArea(speaker);
		soundsPannel.attachChild(speaker);
		if(!resourcesManager.music.isPlaying())
			speaker.setCurrentTileIndex(1);
		Text soundstxt = new Text(3*camera.getWidth() / 4, camera.getHeight()/ 3, resourcesManager.fonttitle,
				activity.getString(R.string.sounds), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		soundsPannel.attachChild(soundstxt);
		TiledSprite speaker_sounds = new TiledSprite(3*camera.getWidth()/4, 3*camera.getHeight()/5, resourcesManager.speaker_region, vbom){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(soundsPannel.isVisible() && pSceneTouchEvent.isActionDown()){
					if(this.getCurrentTileIndex() == 0){
						resourcesManager.with_sounds = false;
						this.setCurrentTileIndex(1);
					}else{
						resourcesManager.with_sounds = true;
						this.setCurrentTileIndex(0);
					}
				}
				return true;
			}
		};
		registerTouchArea(speaker_sounds);
		soundsPannel.attachChild(speaker_sounds);
		if(!resourcesManager.with_sounds)
			speaker_sounds.setCurrentTileIndex(1);
	}

	private void createAboutPannel() {
		aboutPannel = new Entity();
		/*
		 * Color Pannel
		 */
		Sprite pannel = new Sprite(camera.getWidth() / 2,
				camera.getHeight() / 2 + 30,
				resourcesManager.blue_pannel_region, vbom);
		aboutPannel.attachChild(pannel);
		attachChild(aboutPannel);
		aboutPannel.setVisible(false);

		/*
		 * Pannel Content
		 */
		// Title
		Text title = new Text(camera.getWidth() / 2, camera.getHeight()
				- camera.getHeight() / 12, resourcesManager.fonttitle,
				activity.getString(R.string.difficulty_title), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(title);
		//Main Content
		//CREATED BY
		Text textcreatedby = new Text(camera.getWidth() / 2, camera.getHeight()
				- 2*camera.getHeight() / 12, resourcesManager.fontsubtitle,
				activity.getString(R.string.createdby), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textcreatedby);
		Text textcreatedby_plp = new Text(camera.getWidth() / 2, camera.getHeight()
				- 3*camera.getHeight() / 12, resourcesManager.fontARS,
				activity.getString(R.string.ivan) + ", "
				+activity.getString(R.string.sebas) + " & " 
				+ activity.getString(R.string.javi), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textcreatedby_plp);
		//ARTWORK AUTHORS
		Text textartwork = new Text(camera.getWidth() / 2, camera.getHeight()
				- 4*camera.getHeight() / 12, resourcesManager.fontsubtitle,
				activity.getString(R.string.artwork), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textartwork);
		Text textartwork_plp = new Text(camera.getWidth() / 2, camera.getHeight()
				- 5*camera.getHeight() / 12, resourcesManager.fontARS,
				activity.getString(R.string.ivan) + ", "
				+activity.getString(R.string.sebas) + " & " 
				+ activity.getString(R.string.javi), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textartwork_plp);
		//MUSIC AUTHORS
		Text textmusicby = new Text(camera.getWidth() / 2, camera.getHeight()
				- 6*camera.getHeight() / 12, resourcesManager.fontsubtitle,
				activity.getString(R.string.musicby), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textmusicby);
		Text textmusic_plp = new Text(camera.getWidth() / 2, camera.getHeight()
				- 7*camera.getHeight() / 12, resourcesManager.fontARS,
				activity.getString(R.string.musicauthor), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textmusic_plp);
		//SOUNDS AUTHORS
		Text textsoundsby = new Text(camera.getWidth() / 2, camera.getHeight()
				- 8*camera.getHeight() / 12, resourcesManager.fontsubtitle,
				activity.getString(R.string.soundsby), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textsoundsby);
		Text textsoundsby_plp = new Text(camera.getWidth() / 2, camera.getHeight()
				-9*camera.getHeight() / 12, resourcesManager.fontARS,
				activity.getString(R.string.ivan) + ", "
						+activity.getString(R.string.sebas) + " & " 
						+ activity.getString(R.string.javi), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		aboutPannel.attachChild(textsoundsby_plp);
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