package es.remara.notacommonsnake.base;

import org.andengine.entity.scene.Scene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;


public abstract class BaseScene extends Scene{
	
	public abstract SceneType getSceneType();

}
