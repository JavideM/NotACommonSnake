package es.remara.notacommonsnake.scene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetector;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;

import android.os.Looper;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.object.Food;
import es.remara.notacommonsnake.object.Snake;
import es.remara.notacommonsnake.other.Direccion;

public class GameSnakeScene extends BaseScene implements IOnSceneTouchListener{
	
	private Food food;
	private Snake snake;
	
 	private SurfaceGestureDetector mSGD;

	
	@Override
	public void createScene() {
		creacontroles();
		
		//Background
		attachChild(new Sprite(camera.getWidth()/2, camera.getHeight()/2,resourcesManager.background_grass_region, vbom));
		
		//Comida
		food = new Food(resourcesManager.food_AUG_SPEED_region, vbom);
		attachChild(food);

		//Serpiente
//		snake = new Snake( camera.getWidth()/16, camera.getHeight()*25/48, 
//				camera.getWidth()/40, camera.getHeight()/24, 0.3f, vbom);
//		attachChild(snake);
		
		//Snake Sprites
		snake = new Snake(camera.getWidth()/16, camera.getHeight()*25/48, camera.getWidth()/40, camera.getHeight()/24, 
				resourcesManager.snake_body_region, 
				resourcesManager.snake_head_region, 
				resourcesManager.snake_tail_region, 
				0.3f, vbom);
		attachChild(snake);
		
		registerUpdateHandler(new TimerHandler(snake.getSpeed(), true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
					actualizaPantalla();
				}
			}
		));
		
		setOnSceneTouchListener(this);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine, this);		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SNAKE;
	}

	@Override
	public void disposeScene() {
		snake.dispose();
		snake.detachSelf();
		food.detachSelf();
		food.dispose();
		this.detachSelf();
		this.dispose();
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		return mSGD.onManagedTouchEvent(pSceneTouchEvent);
	}	
	
	/*
	 * Metodos de creación de elementos
	 * */
	
	private void creacontroles() {
		Looper.prepare();
		mSGD =  new SurfaceGestureDetector(resourcesManager.activity) {
			
			@Override
			protected boolean onSwipeUp() {
				if(snake.getDirec() != Direccion.opuesta(Direccion.ARRIBA))
					snake.setDirec(Direccion.ARRIBA);
				return false;
			}
			
			@Override
			protected boolean onSwipeRight() {
				if(snake.getDirec() != Direccion.opuesta(Direccion.DERECHA))
					snake.setDirec(Direccion.DERECHA);
				return false;
			}
			
			@Override
			protected boolean onSwipeLeft() {
				if(snake.getDirec() != Direccion.opuesta(Direccion.IZQUIERDA))
					snake.setDirec(Direccion.IZQUIERDA);
				return false;
			}
			
			@Override
			protected boolean onSwipeDown() {
				if(snake.getDirec() != Direccion.opuesta(Direccion.ABAJO))
					snake.setDirec(Direccion.ABAJO);
				return false;
			}
			
			@Override
			protected boolean onSingleTap() {
				return false;
			}
			
			@Override
			protected boolean onDoubleTap() {
				return false;
			}
		};
		mSGD.setEnabled(true);
	}
	
	/*
	 *  Métodos 
	 */

	protected void actualizaPantalla() {
		if(snake.getHead().getX() == food.getX() && snake.getHead().getY() == food.getY()){
			snake.come(food);
			food.setRandom();
		}
		if(!snake.suicidado())
			snake.muevete();
	}



	
}
