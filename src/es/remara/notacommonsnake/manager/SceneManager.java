package es.remara.notacommonsnake.manager;

import org.andengine.ui.IGameInterface.OnCreateSceneCallback;


import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.scene.SplashScene;

/*
 * Esta clase maneja los cambios de escena
 */

public class SceneManager 
{

	
	
	private BaseScene splashScene;
	
	private SceneManager engine;
	private BaseScene currentScene;
	private SceneType currentSceneType;
	
	public enum SceneType
	{
		SCENE_SPLASH
	};
	
	public static SceneManager getInstance()
	{
		return INSTANCE;
	}
	
	public void setScene(BaseScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType)
	{
		switch(sceneType)
		{
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		}
	}

	//Metodo que crea una escena Splash
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	private void disposeSplashScene()
	{
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}
	
	/*
	 * Getters
	 */
	
	public static final SceneManager INSTANCE = new SceneManager();
	
	public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
}
