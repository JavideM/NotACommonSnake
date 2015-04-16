package es.remara.notacommonsnake.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import es.remara.notacommonsnake.GameActivity;

public class ResourcesManager {

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;

	// SplashScene gfx
	private BitmapTextureAtlas splashTextureAtlas;
	public ITextureRegion splash_region;
	// Arkanoid gfx
	private BitmapTextureAtlas ark_ballTextureAtlas;
	public ITextureRegion ark_ball_region;
	// Plataforma gfx. Futuros recursos del sprite platform
	@SuppressWarnings("unused")
	private BitmapTextureAtlas platformTextureAtlas;
	public ITextureRegion platform_region;

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
	}

	public void loadGameSnakeResources() {
		loadGameSnakeGraphics();
		loadGameFonts();
		loadGameSnakeAudio();
	}

	public void loadGameArkanoidResources() {
		loadGameArkanoidGraphics();
		loadGameFonts();
		loadGameArkanoidAudio();
	}

	private void loadMenuGraphics() {

	}

	private void loadMenuAudio() {

	}

	private void loadGameSnakeGraphics() {

	}

	private void loadGameFonts() {

	}

	private void loadGameSnakeAudio() {

	}

	private void loadGameArkanoidGraphics() {
		// bola
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		ark_ballTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 16, 16);
		ark_ball_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ark_ballTextureAtlas, activity,
						"arkanoid/ball.png", 0, 0);
		ark_ballTextureAtlas.load();

		// plataforma textureAtlas y region pendiente
		// muros textureAtlas y region pendiente
	}

	private void loadGameArkanoidAudio() {
		// Audio
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
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
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

}
