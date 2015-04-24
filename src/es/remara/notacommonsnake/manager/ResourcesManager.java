package es.remara.notacommonsnake.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import es.remara.notacommonsnake.GameActivity;

public class ResourcesManager 
{
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	
	
//---------------------------------------------
// TEXTURES & TEXTURE REGIONS
//---------------------------------------------
	
	// Texture Regions
	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion wip_region; 
	public ITextureRegion ark_ball_region;
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
	
	
	// Bipmat Textures
	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	private BitmapTextureAtlas wipTextureAtlas;
	private BitmapTextureAtlas ark_ballTextureAtlas;
	private BitmapTextureAtlas snakeTextureAtlas;

	
//---------------------------------------------
// CLASS LOGIC
//---------------------------------------------

public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    public void loadGameResources()
    {
        loadGameSnakeGraphics();
        loadGameSnakeFonts();
        loadGameSnakeAudio();
    }
    
    public void loadGameArkanoidResources() {
		loadGameArkanoidGraphics();
		loadGameSnakeFonts();
		loadGameArkanoidAudio();
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
    
    private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        // menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "icon_play.png");
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "icon_options.png");
       
    	try 
    	{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
    }
    
    private void loadMenuAudio()
    {
        
    }
    
    private void loadMenuFonts(){
    	
    }

    private void loadGameSnakeGraphics()
    {
        
    }
    
    private void loadGameSnakeFonts()
    {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		snakeTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		background_grass_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "backgraound/grass.png", 0, 0);
		food_X2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_aqua.png", 0, 0);
		food_AUG_SPEED_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_black.png", 0, 0);
		food_REDUC_SPEED_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_blue.png", 0, 0);
		food_CHG_GAME_MODE_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_green.png", 0, 0);
		food_GHOST_MODE_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_pink.png", 0, 0);
		food_SUPER_GROW_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_red.png", 0, 0);
		food_INV_CONTROLS_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_yellow.png", 0, 0);
		food_NORMAL_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/food_lsd.png", 0, 0);
		snake_body_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/snake_body.png", 0, 0);
		snake_head_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/snake_head.png", 0, 0);
		snake_tail_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/snake_tail.png", 0, 0);
		snake_corner_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/snake_corner.png", 0, 0);
		wall_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(snakeTextureAtlas, activity, "snake/wall.png", 0, 0);
		snakeTextureAtlas.load();
    }
    
    private void loadGameSnakeAudio()
    {
        
    }
    
    public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
        splashTextureAtlas.load();	
	}
	
	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}
	

	public void unloadMenuTextures()
	{
		menuTextureAtlas.unload();
	}
	
	
	public void loadWorkInProgressScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/workinprogress/");
		wipTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		wip_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(wipTextureAtlas, activity, "workinprogress.png", 0, 0);
		wipTextureAtlas.load();
	}
	
	public void unloadWorkInProgressScreen()
	{
		wipTextureAtlas.unload();
		wip_region = null;
	}
	
	/*
	 * Esta clase prepara todos los parametros de ResourcesManager 
	 * al cargar el juego para que después sean accesibles desde las distintas clases, escenas
	*/
	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
	
}
