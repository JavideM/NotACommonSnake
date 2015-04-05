package es.remara.notacommonsnake;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.manager.SceneManager;

public class GameActivity extends BaseGameActivity
{
	
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	private BoundCamera camera;
	private EngineOptions engineoptions;
	private ResourcesManager resourcesmanager;
	
	@Override
	public EngineOptions onCreateEngineOptions() 
	{
		this.camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT); 
		this.engineoptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		return this.engineoptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception 
	{
		ResourcesManager.prepareManager(mEngine, this, this.camera, getVertexBufferObjectManager());
		resourcesmanager = ResourcesManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception 
	{
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);		
	}
	
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception 
	{
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                //Cargar aqui la escena del men�
            }
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

}
