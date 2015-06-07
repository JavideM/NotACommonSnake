package es.remara.notacommonsnake.scene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

import es.remara.notacommonsnake.base.BaseGameScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.model.Session;
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
	private TimerHandler utimehandler;
	private TimerHandler chg_level_timehandler;
	private Session session;
	
	//Position touched
	private float touched_x;
	private float touched_y;
	

	/**
	 * Constructors
	 */ 
	//Contructor when you choose no level
	public GameSnakeScene() {
		super();
		session = new Session();
		session.setLevel(0);
		session.setScore(0);
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
		setLevelTitle("" + session.getLevel());
		
		// Create all objects: Snake, Food and walls
		createobjects();
		//Create the scene handlers
		createHandlers();
	}

	@Override
	public void createScene() {
		// Controles
//		createcontrols();

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


	private void createobjects() {
		// Snake Sprites
		snake = new Snake(camera.getWidth() / 16, camera.getHeight() * 25 / 48,
				camera.getWidth() / 40, camera.getHeight() / 24, 0.25f, vbom);
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
		
		//Set mode text
		setModetext(FoodType.getTextMode(FoodType.NORMAL));
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
		chg_level_timehandler = new TimerHandler(0.2f, true,
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
						pTimerHandler.setTimerSeconds(0.2f);
					}
				});

		registerUpdateHandler(chg_level_timehandler);
	}
	/*
	 * Métodos
	 */
	// Handles the screen update
	protected void updateScreen() {
		//checks if a food has been eaten
		if (foods.is_there_food(snake.getHead().getX(), snake.getHead().getY(),
				true)) {
			snake.eat(foods.get_eatenfood());
			if(foods.get_eatenfood().getType() != FoodType.CHG_GAME_MODE)
				if(resourcesManager.with_sounds)
					resourcesManager.omonnomnom.play();
			addScore((foods.get_eatenfood().getType() == FoodType.X2) ? 200
					: 100);
			//sets the mode text
			setModetext(FoodType.getTextMode(foods.get_eatenfood().getType()));
		}
		//checks if the snake is changing game mode 
		if (snake.is_moving_through_worlds()) {
			session.setScore(getScore());
			// it unregisters the snake update handler and register the changing
			// level animations
			create_levelChangeHandler();
		} else {
			// Checks if the 'door' to change level is visible
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
					if (getScore() > 100 + session.getScore()) {
						door.setVisible(true);
					}
				} else {
					gameOver();
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
						session.save();
						go_to_menu();
					}
				});

				alert.setNegativeButton("NO", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						go_to_menu();
					}
				});

				alert.show();
				
			}
		});
		
	}

	// Resets objects on screen
	protected void resetScreen() {
		try{
			detachChild(snake);
			snake = null;
			foods.disposeChilds();
			detachChild(foods);
			foods = null;
			walls.disposeChilds();
			walls.dispose();
			walls.detachSelf();
		}catch(Exception ex){
			
		}
		session.setScore(getScore());
		setLevelTitle(""+session.getLevel());
		createobjects();
	}

	private void go_to_menu(){
		camera.setHUD(null);
		SceneManager.getInstance().loadMenuScene(engine, SceneManager.getInstance().getCurrentScene());
	}

	@Override
	public void onBackKeyPressed() {
		 	unregisterUpdateHandler(utimehandler);
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog.Builder alert = new AlertDialog.Builder(activity);
					alert.setCancelable(false);
					alert.setMessage("Are you sure you want to quit?");
					alert.setPositiveButton("OK", new OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							
							go_to_menu();
						}
					});

					alert.setNegativeButton("NO", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							registerUpdateHandler(utimehandler);
						}
					});

					alert.show();
				}
			});
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SNAKE;
	}

	@Override
	public void disposeScene() {
		try{
			walls.disposeChilds();
			walls.dispose();
			walls.detachSelf();
			snake.detachSelf();
			snake.dispose();
			foods.disposeChilds();
			foods.detachSelf();
			foods.dispose();
			this.detachSelf();
			this.dispose();
		}
		catch(Exception ex){
			
		}
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		float swipe_distance = 30.0f;
		//Swipe Conditions
		boolean swipe_left = Math.abs(touched_x - pSceneTouchEvent.getX())>Math.abs(touched_y - pSceneTouchEvent.getY())
				&& touched_x > pSceneTouchEvent.getX() + swipe_distance;
		boolean swipe_right = Math.abs(touched_x - pSceneTouchEvent.getX())>Math.abs(touched_y - pSceneTouchEvent.getY())
		&& touched_x + swipe_distance < pSceneTouchEvent.getX();
		boolean swipe_up = Math.abs(touched_x - pSceneTouchEvent.getX())<Math.abs(touched_y - pSceneTouchEvent.getY())
				&& touched_y + swipe_distance < pSceneTouchEvent.getY();
		boolean swipe_down = Math.abs(touched_x - pSceneTouchEvent.getX())<Math.abs(touched_y - pSceneTouchEvent.getY())
				&& touched_y > pSceneTouchEvent.getY() + swipe_distance ;
		// Swipe
		if(pSceneTouchEvent.isActionDown()){
			touched_x = pSceneTouchEvent.getX();
			touched_y = pSceneTouchEvent.getY();
		}else{
			if(pSceneTouchEvent.isActionUp()){
				if(swipe_right){
					if (snake.getDirec() != Direction.opposite(Direction.RIGHT))
						snake.setDirec(Direction.RIGHT);
				}else if(swipe_left){
					if (snake.getDirec() != Direction.opposite(Direction.LEFT))
						snake.setDirec(Direction.LEFT);
				}else if(swipe_up){
					if (snake.getDirec() != Direction.opposite(Direction.TOP))
						snake.setDirec(Direction.TOP);
				}else if(swipe_down){
					if (snake.getDirec() != Direction.opposite(Direction.DOWN))
						snake.setDirec(Direction.DOWN);
				}
			}
		}
		
		return false;
	}
}