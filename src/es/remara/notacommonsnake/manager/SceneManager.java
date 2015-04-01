package es.remara.notacommonsnake.manager;
//Linea random
public class SceneManager 
{

	private static final SceneManager INSTANCE = new SceneManager();
	
	public enum SceneType
	{
		
	};
	
	public static SceneManager getInstance()
	{
		return INSTANCE;
	}
	
	public void setScene(SceneType sceneType)
	{
		switch(sceneType)
		{
			
		}
	}
}
