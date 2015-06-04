package es.remara.notacommonsnake.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.graphics.Typeface;
import es.remara.notacommonsnake.GameActivity;

public class ResourcesManager {

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public DBManager dbmanager;

	public Font font;
	public Font fonttitle;
	public Font fontARS;
	public Font fontsubtitle;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	// Texture Regions
	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion achivements_region;
	public ITextureRegion title_region;
	public ITextureRegion facebook_region;
	public ITextureRegion youtube_region;

	public ITextureRegion wip_region;
	public ITextureRegion ark_ball_region;
	public ITextureRegion ark_platform_region;
	public ITextureRegion ark_brick_region;
	public ITextureRegion background_grass_region;
	public ITextureRegion food_X2_region;
	public ITextureRegion food_AUG_SPEED_region;
	public ITextureRegion food_REDUC_SPEED_region;
	public ITextureRegion food_CHG_GAME_MODE_region;
	public ITextureRegion food_GHOST_MODE_region;
	public ITextureRegion food_SUPER_GROW_region;
	public ITextureRegion food_INV_CONTROLS_region;
	public ITextureRegion food_NORMAL_region;
	public ITextureRegion snake_body_region;
	public ITextureRegion snake_head_region;
	public ITextureRegion snake_tail_region;
	public ITextureRegion snake_corner_region;
	public ITextureRegion wall_region;

	public ITextureRegion orange_pannel_region;
	public ITextureRegion blue_pannel_region;
	public ITextureRegion salmon_pannel_region;
	public ITextureRegion trophy_region;
	public ITextureRegion stats_region;
	public ITextureRegion medal_region;
	public ITextureRegion back_region;
	public ITextureRegion door_region;
	public ITextureRegion profile_region;
	public ITextureRegion gamepad_region;
	public ITextureRegion music_region;
	public ITiledTextureRegion speaker_region;

	private BitmapTextureAtlas mFontArsTexture;
	private BitmapTextureAtlas mFontTitleTexture;
	private BitmapTextureAtlas mFontsubTitleTexture;

	// Bipmat Textures
	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	private BuildableBitmapTextureAtlas wipTextureAtlas;
	private BuildableBitmapTextureAtlas arkanoidBGAtlas;
	private BuildableBitmapTextureAtlas snakeTextureAtlas;
	private BitmapTextureAtlas mFontTexture;

	private BuildableBitmapTextureAtlas arsTextureAtlas;
	private BuildableBitmapTextureAtlas settingsTextureAtlas;

	
	//Music
	public Music music;

	

	
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameSnakeResources() {
		loadGameSnakeGraphics();
		loadGameSnakeFonts();
		loadGameSnakeAudio();
	}

	public void unloadGameSnakeResources() {
		unloadGameSnakeGraphics();
	}

	public void loadGameArkanoidResources() {
		loadGameArkanoidGraphics();
		loadGameSnakeFonts();
		loadGameArkanoidAudio();
	}

	public void unloadGameArkanoidResources() {
		unloadGameArkanoidGraphics();

	}

	private void loadGameArkanoidGraphics() {
		// bola
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		arkanoidBGAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR);
		background_grass_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arkanoidBGAtlas, activity,
						"background/dirt.png");
		ark_ball_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arkanoidBGAtlas, activity, "arkanoid/ball.png");
		ark_platform_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arkanoidBGAtlas, activity,
						"arkanoid/platform.png");
		ark_brick_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arkanoidBGAtlas, activity,
						"arkanoid/brick.png");

		try {
			arkanoidBGAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			arkanoidBGAtlas.load();
			// ark_ballTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}

		// plataforma textureAtlas y region pendiente
		// muros textureAtlas y region pendiente
	}

	private void unloadGameArkanoidGraphics() {
		arkanoidBGAtlas.unload();
		background_grass_region = null;
		ark_platform_region = null;
		ark_ball_region = null;
	}

	private void loadGameArkanoidAudio() {
		// Audio
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2048, 512,
				TextureOptions.BILINEAR);

		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "icon_play.png");

		options_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity, "icon_options.png");
		achivements_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity,
						"icon_achivements.png");

		title_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "title.png");

		facebook_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "facebook_icon.png");
		
		youtube_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "youtube_icon.png");

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		background_grass_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity,
						"background/grass.png");
		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadMenuAudio() {
		
	}

	private void loadMenuFonts() {
		this.mFontTexture = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.font = new Font(this.engine.getFontManager(), this.mFontTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true,
				Color.WHITE);

		this.engine.getTextureManager().loadTexture(this.mFontTexture);
		this.engine.getFontManager().loadFont(this.font);

	}

	private void loadGameSnakeGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		snakeTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR);
		background_grass_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"background/grass.png");
		food_X2_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_aqua.png");
		food_AUG_SPEED_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_black.png");
		food_REDUC_SPEED_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_blue.png");
		food_CHG_GAME_MODE_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_green.png");
		food_GHOST_MODE_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_pink.png");
		food_SUPER_GROW_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_red.png");
		food_INV_CONTROLS_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_yellow.png");
		food_NORMAL_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/food_lsd.png");
		snake_body_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/snake_body.png");
		snake_head_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/snake_head.png");
		snake_tail_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/snake_tail.png");
		snake_corner_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(snakeTextureAtlas, activity,
						"snake/snake_corner.png");
		wall_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				snakeTextureAtlas, activity, "snake/wall.png");
		door_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				snakeTextureAtlas, activity, "snake/door.png");
		try {
			this.snakeTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.snakeTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void unloadGameSnakeGraphics() {
		snakeTextureAtlas.unload();
		background_grass_region = null;
		food_X2_region = null;
		food_AUG_SPEED_region = null;
		food_REDUC_SPEED_region = null;
		food_CHG_GAME_MODE_region = null;
		food_GHOST_MODE_region = null;
		food_SUPER_GROW_region = null;
		food_INV_CONTROLS_region = null;
		food_NORMAL_region = null;
		snake_body_region = null;
		snake_head_region = null;
		snake_tail_region = null;
		snake_corner_region = null;
		wall_region = null;
	}

	private void loadGameSnakeFonts() {

	}

	private void loadGameSnakeAudio() {

	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
		try
		{
			MusicFactory.setAssetBasePath("mfx/");
		    music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity.getApplicationContext(), "soundtrack.ogg");
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}

	public void loadWorkInProgressScreen() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/workinprogress/");
		// wipTextureAtlas = new
		// BitmapTextureAtlas(activity.getTextureManager(),
		// 512, 256, TextureOptions.BILINEAR);
		wipTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		wip_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				wipTextureAtlas, activity, "workinprogress.png");
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/background/");
		background_grass_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(wipTextureAtlas, activity, "grass.png");
		try {
			this.wipTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.wipTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void unloadWorkInProgressScreen() {
		wipTextureAtlas.unload();
		wip_region = null;
		background_grass_region = null;
	}

	/*
	 * Achievements, Records, Stats Scene Resources
	 */

	public void loadARSResources() {
		loadARSGraphics();
		loadARSFonts();
	}

	public void loadARSGraphics() {
		arsTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/background/");
		background_grass_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arsTextureAtlas, activity, "grass.png");
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ars/");
		orange_pannel_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arsTextureAtlas, activity, "orangepannel.png");
		blue_pannel_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arsTextureAtlas, activity, "bluepannel.png");
		salmon_pannel_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arsTextureAtlas, activity, "salmonpannel.png");
		trophy_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				arsTextureAtlas, activity, "trophy.png");
		stats_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				arsTextureAtlas, activity, "stats.png");
		medal_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				arsTextureAtlas, activity, "medal.png");
		back_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				arsTextureAtlas, activity, "back.png");
		//Work in progress
		BitmapTextureAtlasTextureRegionFactory
		.setAssetBasePath("gfx/workinprogress/");
		wip_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				arsTextureAtlas, activity, "workinprogress.png");
		try {
			this.arsTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.arsTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadARSFonts() {
		mFontArsTexture = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		mFontTitleTexture = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		fonttitle = new Font(this.engine.getFontManager(), mFontTitleTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true,
				Color.WHITE);

		fontARS = new Font(this.engine.getFontManager(), mFontTexture,
				Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL), 20,
				true, Color.WHITE);

		engine.getTextureManager().loadTexture(mFontArsTexture);
		engine.getTextureManager().loadTexture(mFontTitleTexture);
		engine.getFontManager().loadFont(fontARS);
		engine.getFontManager().loadFont(fonttitle);
	}

	public void unloadARSResources() {
		arsTextureAtlas.unload();
		background_grass_region = null;
		orange_pannel_region = null;
		salmon_pannel_region = null;
		blue_pannel_region = null;
		trophy_region = null;
		medal_region = null;
		stats_region = null;
	}

	/*
	 * Settings Scene
	 */
	
	public void loadSettingsResources() {
		loadSettingsGraphics();
		loadSettingsFonts();
	}

	public void loadSettingsGraphics() {
		settingsTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/");
		background_grass_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(settingsTextureAtlas, activity, "background/grass.png");
		orange_pannel_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(settingsTextureAtlas, activity, "ars/orangepannel.png");
		blue_pannel_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(settingsTextureAtlas, activity, "ars/bluepannel.png");
		salmon_pannel_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(settingsTextureAtlas, activity, "ars/salmonpannel.png");
		profile_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				settingsTextureAtlas, activity, "settings/profiles.png");
		gamepad_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				settingsTextureAtlas, activity, "settings/gamepad.png");
		music_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				settingsTextureAtlas, activity, "settings/music.png");
		back_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				settingsTextureAtlas, activity, "ars/back.png");
		speaker_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				settingsTextureAtlas, activity, "settings/speaker.png", 2, 1);
		//Work in progress
		BitmapTextureAtlasTextureRegionFactory
		.setAssetBasePath("gfx/workinprogress/");
		wip_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				settingsTextureAtlas, activity, "workinprogress.png");
		try {
			this.settingsTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.settingsTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadSettingsFonts() {
		mFontArsTexture = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		mFontTitleTexture = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		mFontsubTitleTexture = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		fonttitle = new Font(this.engine.getFontManager(), mFontTitleTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true,
				Color.WHITE);
		
		fontsubtitle = new Font(this.engine.getFontManager(), mFontsubTitleTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20, true,
				Color.WHITE);

		fontARS = new Font(this.engine.getFontManager(), mFontTexture,
				Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL), 20,
				true, Color.WHITE);

		engine.getTextureManager().loadTexture(mFontArsTexture);
		engine.getTextureManager().loadTexture(mFontTitleTexture);
		engine.getTextureManager().loadTexture(mFontsubTitleTexture);
		engine.getFontManager().loadFont(fontsubtitle);
		engine.getFontManager().loadFont(fontARS);
		engine.getFontManager().loadFont(fonttitle);
	}

	public void unloadSettingsResources() {
		settingsTextureAtlas.unload();
		background_grass_region = null;
		orange_pannel_region = null;
		salmon_pannel_region = null;
		blue_pannel_region = null;
		profile_region = null;
		gamepad_region = null;
		music_region = null;
	}
	
	/*
	 * Esta clase prepara todos los parametros de ResourcesManager al cargar el
	 * juego para que después sean accesibles desde las distintas clases,
	 * escenas
	 */
	public static void prepareManager(Engine engine, GameActivity activity,
			BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().dbmanager = new DBManager(activity);
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

}
