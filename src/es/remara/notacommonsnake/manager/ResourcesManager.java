package es.remara.notacommonsnake.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import es.remara.notacommonsnake.GameActivity;

public class ResourcesManager 
{
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	private static Engine engine;
	private static GameActivity activity;
	private static BoundCamera camera;
	private static VertexBufferObjectManager vbom;
	
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
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
	
}
