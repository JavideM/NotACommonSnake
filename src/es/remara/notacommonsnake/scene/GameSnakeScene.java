package es.remara.notacommonsnake.scene;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetector;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import android.R;
import android.app.Activity;
import android.os.Looper;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.object.Food;
import es.remara.notacommonsnake.object.Snake;
import es.remara.notacommonsnake.object.Food.FoodType;
import es.remara.notacommonsnake.object.Walls;
import es.remara.notacommonsnake.other.Direction;

public class GameSnakeScene extends BaseScene implements IOnSceneTouchListener{
	
	private Food food;
	private Snake snake;
	
 	private SurfaceGestureDetector mSGD;

	@Override
	public void createScene() {
		creacontroles();
		
		//Background
		attachChild(new Sprite(camera.getWidth()/2, camera.getHeight()/2,resourcesManager.background_grass_region,  vbom));
		
		
		//Comida
		food = new Food(FoodType.getRandom(), resourcesManager, vbom);
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
		
		//Walls
		Walls walls = new Walls(1, resourcesManager.wall_region, this, activity, vbom);
		attachChild(walls);
		
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
				if(snake.getDirec() != Direction.opposite(Direction.TOP))
					snake.setDirec(Direction.TOP);
				return false;
			}
			
			@Override
			protected boolean onSwipeRight() {
				if(snake.getDirec() != Direction.opposite(Direction.RIGHT))
					snake.setDirec(Direction.RIGHT);
				return false;
			}
			
			@Override
			protected boolean onSwipeLeft() {
				if(snake.getDirec() != Direction.opposite(Direction.LEFT))
					snake.setDirec(Direction.LEFT);
				return false;
			}
			
			@Override
			protected boolean onSwipeDown() {
				if(snake.getDirec() != Direction.opposite(Direction.DOWN))
					snake.setDirec(Direction.DOWN);
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
			snake.eat(food);
			detachChild(food);
			food = new Food(FoodType.getRandom(), resourcesManager, vbom);
			attachChild(food);
		}
		if(!snake.suicide())
			snake.move();
	}



	
}
