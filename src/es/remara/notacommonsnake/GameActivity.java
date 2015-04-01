package es.remara.notacommonsnake;

//Javi acepta

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class GameActivity extends BaseGameActivity
{
	
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	private Camera camera;
	private EngineOptions engineoptions;
	
	@Override
	public EngineOptions onCreateEngineOptions() 
	{
		this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT); 
		this.engineoptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		return this.engineoptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception 
	{
		
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

}
