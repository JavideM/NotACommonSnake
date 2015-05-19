package es.remara.notacommonsnake.manager;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.scene.MainMenuScene;
import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.scene.AchievementsRecordsStatsScene;
import es.remara.notacommonsnake.scene.GameArkanoidScene;
import es.remara.notacommonsnake.scene.GameSnakeScene;
import es.remara.notacommonsnake.scene.SplashScene;
import es.remara.notacommonsnake.scene.WorkInProgressScene;

/*
 * Esta clase maneja los cambios de escena
 */

public class SceneManager {

	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gamesnakeScene;
	private BaseScene workInProgressScene;
	private BaseScene arkanoidScene;
	private BaseScene arsScene;

	private BaseScene currentScene;
	private SceneType currentSceneType;

	private Engine engine = ResourcesManager.getInstance().engine;

	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_SNAKE, SCENE_IN_PROGRESS, SCENE_ARKANOID, SCENE_ARS
	};

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setScene(SceneType sceneType) {
		switch (sceneType) {
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_SNAKE:
			setScene(gamesnakeScene);
			break;
		case SCENE_IN_PROGRESS:
			setScene(workInProgressScene);
			break;
		case SCENE_ARKANOID:
			setScene(arkanoidScene);
			break;
		case SCENE_ARS:
			setScene(arsScene);
		}
	}

	// Metodos para crear el menu, la primera vez que se llama a la escena
	public void createMenuScene() {
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		SceneManager.getInstance().setScene(menuScene);

		disposeSplashScene();
	}

	// Metodo para cargar el Menu desde otras escenas
	public void loadMenuScene(final Engine mEngine, BaseScene prescene) {
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		SceneManager.getInstance().setScene(menuScene);
		switch (prescene.getSceneType()) {
		case SCENE_SNAKE:
			gamesnakeScene.disposeScene();
			gamesnakeScene = null;
			ResourcesManager.getInstance().unloadGameSnakeResources();
			break;
		case SCENE_ARKANOID:
			arkanoidScene.disposeScene();
			arkanoidScene = null;
			ResourcesManager.getInstance().unloadGameArkanoidResources();
			break;
		case SCENE_IN_PROGRESS:
			disposeWorkInProgress();
			break;
		case SCENE_ARS:
			disposeARSScene();
		default:
			break;
		}

	}

	// Metodo que crea una escena Splash
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	private void disposeSplashScene() {
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	// Metodo crea el juego
	public void createSnakeGameScene() {
		ResourcesManager.getInstance().loadGameSnakeResources();
		gamesnakeScene = new GameSnakeScene();
		SceneManager.getInstance().setScene(gamesnakeScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		// disposeSplashScene();

	}

	// Método que crea la escena Arkanoid
	public void createArkanoidScene() {
		ResourcesManager.getInstance().loadGameArkanoidResources();
		arkanoidScene = new GameArkanoidScene();
		currentScene = arkanoidScene;
		SceneManager.getInstance().setScene(arkanoidScene);
		ResourcesManager.getInstance().unloadMenuTextures();
	}

	@SuppressWarnings("unused")
	private void disposeArkanoidScene() {
		// Aun por implementar
	}

	// Metodo crea una escena WorkInProgress
	public void createWorkInProgress() {
		ResourcesManager.getInstance().loadWorkInProgressScreen();
		workInProgressScene = new WorkInProgressScene();
		SceneManager.getInstance().setScene(workInProgressScene);
	}

	private void disposeWorkInProgress() {
		ResourcesManager.getInstance().unloadWorkInProgressScreen();
		workInProgressScene.disposeScene();
		workInProgressScene = null;
	}

	/*
	 * ARSScene (Achievements, Records, Stats)
	 */
	public void createAchievementsRecordsStatsScene() {
		ResourcesManager.getInstance().loadARSResources();
		arsScene = new AchievementsRecordsStatsScene();
		SceneManager.getInstance().setScene(arsScene);
	}

	private void disposeARSScene() {
		ResourcesManager.getInstance().unloadARSResources();
		arsScene.disposeScene();
		arsScene = null;
	}

	/*
	 * Getters
	 */

	public static final SceneManager INSTANCE = new SceneManager();

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}
}
