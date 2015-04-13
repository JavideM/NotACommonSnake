package es.remara.notacommonsnake.scene;

import java.util.LinkedList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetector;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;

import android.os.Looper;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class GameSnakeScene extends BaseScene implements IOnSceneTouchListener{
	
	private Rectangle snakehead;
	private Rectangle food;
	private LinkedList<Rectangle> snake;
	
 	private SurfaceGestureDetector mSGD;

 	private int direccion;
 	
 	private final int DERECHA = 10;
 	private final int IZQUIERDA = -10;
 	private final int ARRIBA = 1;
 	private final int ABAJO = -1;
	
	@Override
	public void createScene() {
		creacontroles();
		
		direccion = DERECHA;
		
		setBackground(new Background(Color.GREEN));
		
		//Comida
		food = new Rectangle(10, 10, 16, 16, vbom);
		food.setColor(Color.BLACK);
		attachChild(food);
		comidaAleatoria();
		
		/*Serpiente*/
		//--cabeza
		snakehead = new Rectangle(resourcesManager.camera.getWidth()/16, resourcesManager.camera.getHeight()*25/48, 
				resourcesManager.camera.getWidth()/40, resourcesManager.camera.getHeight()/24, vbom);
		snakehead.setColor(Color.RED);
		attachChild(snakehead);
		//--cuerpo
		snake = new LinkedList<Rectangle>();
		Rectangle cuerpo = new Rectangle(resourcesManager.camera.getWidth()/16 - 20, resourcesManager.camera.getHeight()*25/48, 
				resourcesManager.camera.getWidth()/40, resourcesManager.camera.getHeight()/24, vbom);
		cuerpo.setColor(Color.YELLOW);
		attachChild(cuerpo);
		snake.addFirst(cuerpo);
	
		this.registerUpdateHandler(new TimerHandler(0.3f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
					move();
					
				}

			
			}
		));
		
		setOnSceneTouchListener(this);
		
		
	}

	private void comidaAleatoria() {
		food.setPosition(MathUtils.random(1, 38)*20 + 10, MathUtils.random(1, 22)*20 + 10);
		
	}

	protected void move() {
		if(snakehead.getX() == food.getX() && snakehead.getY() == food.getY()){
			comidaAleatoria();
			snakecrece();
		}
		Rectangle cola = snake.removeLast();
		cola.setPosition(snakehead);
		snake.addFirst(cola);
		switch(direccion){
			case(ARRIBA):
				if(snakehead.getY() < resourcesManager.camera.getHeight() - 10)
					snakehead.setPosition(snakehead.getX(), snakehead.getY() + 20);
				else
					snakehead.setPosition(snakehead.getX(), 10);
				break;
			case(ABAJO):
				if(snakehead.getY() > 10)
					snakehead.setPosition(snakehead.getX(), snakehead.getY() - 20);
				else
					snakehead.setPosition(snakehead.getX(), resourcesManager.camera.getHeight() - 10);
				break;
			case(DERECHA):
				if(snakehead.getX() < resourcesManager.camera.getWidth() - 10)
					snakehead.setPosition(snakehead.getX() + 20, snakehead.getY());
				else
					snakehead.setPosition(10, snakehead.getY());
				break;
			case(IZQUIERDA):
				if(snakehead.getX() > 10)
					snakehead.setPosition(snakehead.getX() - 20, snakehead.getY() );
				else
					snakehead.setPosition(resourcesManager.camera.getWidth() - 10, snakehead.getY() );
				break;
		}
		
	}

	private void snakecrece() {
		Rectangle colanuevaRectangle = new Rectangle(snake.getFirst().getX(), snake.getFirst().getY(), 
				resourcesManager.camera.getWidth()/40, resourcesManager.camera.getHeight()/24, vbom);
		colanuevaRectangle.setColor(Color.YELLOW);
		attachChild(colanuevaRectangle);
		snake.addFirst(colanuevaRectangle);
	}

	private void creacontroles() {
		Looper.prepare();
		mSGD =  new SurfaceGestureDetector(resourcesManager.activity) {
			
			@Override
			protected boolean onSwipeUp() {
				direccion = ARRIBA;
				return false;
			}
			
			@Override
			protected boolean onSwipeRight() {
				direccion = DERECHA;
				return false;
			}
			
			@Override
			protected boolean onSwipeLeft() {
				direccion = IZQUIERDA;
				return false;
			}
			
			@Override
			protected boolean onSwipeDown() {
				direccion = ABAJO;
				return false;
			}
			
			@Override
			protected boolean onSingleTap() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			protected boolean onDoubleTap() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		mSGD.setEnabled(true);
		
	}

	@Override
	public void onBackKeyPressed() {
		return;		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SNAKE;
		
	}

	@Override
	public void disposeScene() {
		snakehead.detachSelf();
		snakehead.dispose();
		food.detachSelf();
		food.dispose();
		for (Rectangle cuerpo: snake) {
			cuerpo.detachSelf();
			cuerpo.dispose();
		}
		this.detachSelf();
		this.dispose();
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		return mSGD.onManagedTouchEvent(pSceneTouchEvent);
	}	
}
