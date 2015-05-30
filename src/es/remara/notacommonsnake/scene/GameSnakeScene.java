package es.remara.notacommonsnake.scene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;
import android.widget.EditText;

import es.remara.notacommonsnake.base.BaseGameScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.model.Session;
import es.remara.notacommonsnake.model.Snake_Level;
import es.remara.notacommonsnake.object.Foods;
import es.remara.notacommonsnake.object.Snake;
import es.remara.notacommonsnake.object.Food.FoodType;
import es.remara.notacommonsnake.object.Walls;
import es.remara.notacommonsnake.other.Direction;

public class GameSnakeScene extends BaseGameScene implements
		IOnSceneTouchListener {
	/*
	 * Objects
	 */
	private Foods foods;
	private Snake snake;
	private Walls walls;
	private Sprite door;

	/*
	 * Parameters
	 */

	/*
	 * Others
	 */
	private SurfaceGestureDetector mSGD;
	private TimerHandler utimehandler;
	private TimerHandler chg_level_timehandler;
	private Session session;
	private Snake_Level snake_level;
	

	/**
	 * Constructors
	 */ 
	//Contructor when you choose no level
	public GameSnakeScene() {
		super();
		session = new Session();
		session.setLevel(0);
		session.setScore(0);
		snake_level = new Snake_Level();
		snake_level.setScore(0);
		session.AddSnake_Level(snake_level);
		setLevelTitle(""+session.getLevel());
		
		// Create all objects: Snake, Food and walls
		createobjects();
		//Create the scene handlers
		createHandlers();
	}

	//Contructor when you come from another scene (for example and Arkanoid Scene)
	public GameSnakeScene(Session session) {
		super();
		this.session = session;
		snake_level = new Snake_Level();
		snake_level.setScore(0);
		session.AddSnake_Level(snake_level);
		session.nextlevel();
		addScore(session.getScore());
		setLevelTitle("" + session.getLevel());
		// Create all objects: Snake, Food and walls
		createobjects();
		//Create the scene handlers
		createHandlers();
	}

	//Contructor when you choose a specific level
	public GameSnakeScene(int level){
		super();
		session = new Session();
		session.setLevel(level);
		session.setScore(0);
		snake_level = new Snake_Level();
		snake_level.setScore(0);
		session.AddSnake_Level(snake_level);
		setLevelTitle("" + session.getLevel());
		
		// Create all objects: Snake, Food and walls
		createobjects();
		//Create the scene handlers
		createHandlers();
	}

	@Override
	public void createScene() {
		// Controles
		createcontrols();

		// Puntos
		createHUD();

		// Background
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
		
		//Doors
		door = new Sprite(camera.getWidth()/2 -10, camera.getHeight() - 30, resourcesManager.door_region, vbom);
		door.setVisible(false);
		door.setZIndex(5);
		attachChild(door);
	}
	
	/*
	 * Metodos de creación de elementos
	 */

	private void createcontrols() {
		if (Looper.myLooper() == null) {
			Looper.prepare();
		}
		mSGD = new SurfaceGestureDetector(resourcesManager.activity) {

			@Override
			protected boolean onSwipeUp() {
				if (snake.getDirec() != Direction.opposite(Direction.TOP))
					snake.setDirec(Direction.TOP);
				return false;
			}

			@Override
			protected boolean onSwipeRight() {
				if (snake.getDirec() != Direction.opposite(Direction.RIGHT))
					snake.setDirec(Direction.RIGHT);
				return false;
			}

			@Override
			protected boolean onSwipeLeft() {
				if (snake.getDirec() != Direction.opposite(Direction.LEFT))
					snake.setDirec(Direction.LEFT);
				return false;
			}

			@Override
			protected boolean onSwipeDown() {
				if (snake.getDirec() != Direction.opposite(Direction.DOWN))
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

	private void createobjects() {
		// Serpiente
		// snake = new Snake( camera.getWidth()/16, camera.getHeight()*25/48,
		// camera.getWidth()/40, camera.getHeight()/24, 0.3f, vbom);
		// attachChild(snake);
		// Snake Sprites
		snake = new Snake(camera.getWidth() / 16, camera.getHeight() * 25 / 48,
				camera.getWidth() / 40, camera.getHeight() / 24, 0.3f, vbom);
		attachChild(snake);
		snake.setZIndex(1);

		// Walls
		walls = new Walls(session.getLevel(), resourcesManager.wall_region, this,
				activity, vbom);
		attachChild(walls);
		walls.setZIndex(3);

		// Comida
		foods = new Foods(walls, resourcesManager, vbom);
		attachChild(foods);
		foods.setZIndex(0);

		sortChildren();
	}

	private void createHandlers(){
		utimehandler = new TimerHandler(snake.getSpeed(), true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						unregisterUpdateHandler(utimehandler);
						updateScreen();
						pTimerHandler.setTimerSeconds(snake.getSpeed());
					}
				});

		registerUpdateHandler(utimehandler);

		setOnSceneTouchListener(this);
	}
	
	private void create_levelChangeHandler(){
		chg_level_timehandler = new TimerHandler(snake.getSpeed(), true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						
						
						
						if(snake.go_through_portal()){
							unregisterUpdateHandler(chg_level_timehandler);
							if(snake.is_moving_through_worlds())
								SceneManager.getInstance().createArkanoidScene(session);
							else{
								session.nextlevel();
								resetScreen();
								door.setVisible(false);
								registerUpdateHandler(utimehandler);
							}
						}
						pTimerHandler.setTimerSeconds(snake.getSpeed()*2);
					}
				});

		registerUpdateHandler(chg_level_timehandler);
	}
	/*
	 * Métodos
	 */
	// Handles the screen update
	protected void updateScreen() {
		if (foods.is_there_food(snake.getHead().getX(), snake.getHead().getY(),
				true)) {
			snake.eat(foods.get_eatenfood());
			addScore((foods.get_eatenfood().getType() == FoodType.X2) ? 200
					: 100);
		}
		if (snake.is_moving_through_worlds()) {
			session.setScore(getScore());
			// it unregisters the snake update handler and register the changing
			// level animations
			create_levelChangeHandler();
		} else {
			if (door.isVisible()
					&& snake.getHead().getX() == (camera.getWidth() / 2 - 10)
					&& snake.getHead().getY() == (camera.getHeight() - 50)) {
				// it unregisters the snake update handler and register the
				// changing level animations
				create_levelChangeHandler();
			} else {
				if (snake.is_ghost_mode()
						|| (!snake.suicide() && !snake.hit_a_wall(walls))) {
					snake.move();
					registerUpdateHandler(utimehandler);
				} else {
					gameOver();
				}
				if (getScore() > 500 + session.getScore()) {
					door.setVisible(true);

				}
			}
		}
	}

	private void gameOver() {
		final EditText ed = new EditText(activity);
		ed.setHint("Player Namer");

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						activity);
				alert.setCancelable(false);
				alert.setTitle("GAME OVER");
				alert.setMessage("You have score: " + getScore() + " points"
						+ "\nDo you want to save your score?");
				alert.setView(ed);
				alert.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if (ed.getText().toString() != "") {
							session.setPlayer_name(ed.getText().toString());

						} else
							session.setPlayer_name("player1");
						session.setScore(getScore());
						session.save(dbmanager);
						playAgain();
					}
				});

				alert.setNegativeButton("NO", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						playAgain();
					}
				});

				alert.show();
				
			}
		});
		
	}

	public void playAgain() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder alert = new AlertDialog.Builder(activity);
				alert.setCancelable(false);
				alert.setMessage("Play Again?");
				alert.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						registerUpdateHandler(utimehandler);
						resetScreen();
					}
				});

				alert.setNegativeButton("NO", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						onBackKeyPressed();
					}
				});

				alert.show();
			}
		});
	}

	// Resets objects on screen
	protected void resetScreen() {
		detachChild(snake);
		snake = null;
		foods.disposeChilds();
		detachChild(foods);
		foods = null;
		walls.disposeChilds();
		walls.dispose();
		walls.detachSelf();
		session.setScore(getScore());
		setLevelTitle(""+session.getLevel());
		createobjects();
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
		camera.setHUD(null);
		snake.dispose();
		snake.detachSelf();
		foods.detachSelf();
		foods.dispose();
		this.detachSelf();
		this.dispose();

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		return mSGD.onManagedTouchEvent(pSceneTouchEvent);
	}
}