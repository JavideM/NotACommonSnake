package es.remara.notacommonsnake.base;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import es.remara.notacommonsnake.manager.DBManager;
import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public abstract class BaseScene extends Scene {

	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;
	protected DBManager dbmanager;

	/*
	 * CONSTRUCTOR
	 */

	public BaseScene() {
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		this.dbmanager = resourcesManager.dbmanager;
		createScene();
	}

	/*
	 * METODOS ABSTRACTOS
	 */

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();

}
