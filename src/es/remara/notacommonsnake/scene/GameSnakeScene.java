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

	/*
	 * Parameters
	 */
	private int points;
	private int level;

	/*
	 * Others
	 */
	private SurfaceGestureDetector mSGD;
	private TimerHandler utimehandler;
	private Session session;
	private Snake_Level snake_level;

	/**
	 * Constructors
	 */ 
	public GameSnakeScene() {
		super();
		session = new Session();
		session.setLevel(0);
		snake_level = new Snake_Level();
		snake_level.setScore(0);
		session.AddSnake_Level(snake_level);
		this.level = this.session.getLevel();
	}

	public GameSnakeScene(Session session) {
		super();
		this.session = session;
		snake_level = new Snake_Level();
		snake_level.setScore(0);
		session.AddSnake_Level(snake_level);
		this.level = this.session.getLevel();
		
		createobjects();
	}

	public int getpoints() {
		return this.points;
	}

	public int getlevel() {
		return this.level;
	}

	@Override
	public void createScene() {
		// Session info
		session = new Session();

		// Controles
		createcontrols();

		// Puntos
		this.points = 0;
		createHUD();

		// Background
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));

		// Create all objects: Snake, Food and walls
		createobjects();

		utimehandler = new TimerHandler(snake.getSpeed(), true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						updateScreen();
						pTimerHandler.setTimerSeconds(snake.getSpeed());
					}
				});

		registerUpdateHandler(utimehandler);

		setOnSceneTouchListener(this);
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
				camera.getWidth() / 40, camera.getHeight() / 24,
				resourcesManager.snake_body_region,
				resourcesManager.snake_head_region,
				resourcesManager.snake_tail_region,
				resourcesManager.snake_corner_region, 0.3f, vbom);
		attachChild(snake);
		snake.setZIndex(1);

		// Walls
		walls = new Walls(getlevel(), resourcesManager.wall_region, this,
				activity, vbom);
		attachChild(walls);
		walls.setZIndex(2);

		// Comida
		foods = new Foods(walls, resourcesManager, vbom);
		attachChild(foods);
		foods.setZIndex(0);

		sortChildren();
	}

	/*
	 * Métodos
	 */
	// Handles the screen update
	protected void updateScreen() {
		if (foods.is_there_food(snake.getHead().getX(), snake.getHead().getY(),
				true)) {
			snake.eat(foods.get_eatenfood());
			points += (foods.get_eatenfood().getType() == FoodType.X2) ? 200
					: 100;
			addScore((foods.get_eatenfood().getType() == FoodType.X2) ? 200
					: 100);
		}
		if (snake.is_ghost_mode()
				|| (!snake.suicide() && !snake.hit_a_wall(walls)))
			snake.move();
		else {
			gameOver();
		}
		if (points > 2000) {
			nextlevel();
			resetScreen();
		}
	}

	private void gameOver() {
		final EditText ed = new EditText(activity);
		ed.setHint("Player Namer");

		unregisterUpdateHandler(utimehandler);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						activity);
				alert.setCancelable(false);
				alert.setTitle("GAME OVER");
				alert.setMessage("You have score: " + points + " points"
						+ "\nDo you want to save your score?");
				alert.setView(ed);
				alert.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if (ed.getText().toString() != "") {
							session.setPlayer_name(ed.getText().toString());

						} else
							session.setPlayer_name("player1");
						session.setLevel(points);
						session.save(dbmanager);
						playAgain();
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

	public void playAgain() {
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
		points = 0;

		createobjects();
	}

	// Choose the next level
	private void nextlevel() {
		switch (level) {
		case 0:
			level = 1;
			break;
		case 1:
			level = 2;
			break;
		case 2:
			level = 0;
			break;
		}
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