package es.remara.notacommonsnake.scene;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import es.remara.notacommonsnake.base.BaseGameScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.model.Session;
import es.remara.notacommonsnake.object.Brick;

public class GameArkanoidScene extends BaseGameScene implements
		IOnSceneTouchListener, ContactListener {

	// Provisional. Se cambiara por sprites (supongo...)
	private Rectangle[] walls;

	private Body[] wallBodies;

	private Rectangle gORegion;

	private ArrayList<Brick> bricks;

	private ArrayList<Brick> bricksRem;

	private int[][] grid;

	private FixtureDef wallFix, ballFix, platFix, gOFix;

	private Body platformBody, ballBody, gOBody;

	private PhysicsWorld arkanoidPhysicsWorld;

	private PhysicsConnector ballPC, platformPC;

	private final Vector2 SPEED = new Vector2(7.0f, 7.0f);

	private boolean notStarted = true;

	private boolean newGame;

	private int bricksAmount;

	private float modSpeed;

	private float initialX, initialY;

	private WeldJointDef ball_plat_JointDef;

	private Joint ball_plat_Joint;

	private Session session;

	float pmr = PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

	private Sprite platformSprite, ballSprite;

	private float a, b;

	private double y;

	private Vector2 test;
	
	protected float touchedY;

	private TimerHandler timerhandler;

	public GameArkanoidScene(Session session) {
		super();
		if (session != null) {
			this.session = session;
			addScore(session.getScore());
		}
	}

	@Override
	public void createScene() {
		newGame = true;
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
		arkanoidPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		// Creación HUD y conservación de datos.
		String sad = "Bonus level";
		this.createHUD();
		camera.getHUD().setRotation(-90.0f);
		camera.getHUD().setPosition(480, 0);
		setTitle(sad);
		this.titleText.setPosition(300, 435);
		// Creación sprites, físicas, conectores, etc.
		registerUpdateHandler(arkanoidPhysicsWorld);
		createFixtures();
		createWallSprites();
		createWallBodies();
		createPlatformSprite();
		createBallSprite();
		iLikeBricks();
		createGORegion();
		setBallPhysicsConnectors();
		setPlatformPhysicsConnectors();
		createJoint();
		attachChilds();
		bricksAmount = bricks.size();
		touchedY = camera.getHeight()/2;
		// Listeners
		this.setOnSceneTouchListener(this);
		arkanoidPhysicsWorld.setContactListener(this);
		this.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				removeBricks(bricksRem);
			}
		});
		
		timerhandler = new TimerHandler(0.01f, true,
				new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				if(touchedY -5  > platformSprite.getY() && platformSprite.getY() + platformSprite.getHeight()/2 < camera.getHeight()){
					platformBody.setLinearVelocity(new Vector2(0,10.0f));
				}else if(touchedY+5  < platformSprite.getY() && platformSprite.getY() - platformSprite.getHeight()/2 > 0)
					platformBody.setLinearVelocity(new Vector2(0,-10.0f));
				else
					platformBody.setLinearVelocity(new Vector2(0,0));
				}
			
		});
	}

	private void attachChilds() {
		this.attachChild(ballSprite);
		this.attachChild(gORegion);
		for (int i = 0; i < walls.length; i++) {
			this.attachChild(walls[i]);
		}
		for (Brick brk : bricks) {
			this.attachChild(brk);
		}
	}

	// Crea el "joint" que unirá la bola a la plataforma al comienzo.
	private void createJoint() {
		ball_plat_JointDef = new WeldJointDef();
		ball_plat_JointDef.initialize(platformBody, ballBody,
				platformBody.getPosition());
		ball_plat_Joint = arkanoidPhysicsWorld.createJoint(ball_plat_JointDef);
	}

	// Crea las "Fixtures" de las distintas entidades.
	private void createFixtures() {
		wallFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		platFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		ballFix = PhysicsFactory.createFixtureDef(10.0f, 1.0f, 0.0f);
		gOFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
	}

	/*
	 * Método que crea la plataforma. He implementado el "attachChild(Entity)"
	 * dentro del mismo para evitar posibles errores pues la bola se crea en
	 * función a la posición de la plataforma
	 */
	private void createPlatformSprite() {
		platformSprite = new Sprite(camera.getWidth() - 120,
				camera.getHeight() / 2, resourcesManager.ark_platform_region,
				vbom);
		platformBody = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld,
				platformSprite, BodyType.KinematicBody, platFix);
		platformBody.setUserData("Platform");
		this.attachChild(platformSprite);
	}

	/*
	 * Crea el Sprite de la bola así como su cuerpo. En caso de ser un nuevo //
	 * juego establece una posición especifica (posición "y" igual a la de la //
	 * plataforma inicialmente).
	 */
	private void createBallSprite() {
		float yPos = (newGame) ? (camera.getHeight() / 2) : (platformBody
				.getPosition().y * pmr);
		ballSprite = new Sprite((camera.getWidth() - 120) - 16, yPos,
				resourcesManager.ark_ball_region, vbom);
		ballBody = PhysicsFactory.createCircleBody(arkanoidPhysicsWorld,
				ballSprite, BodyType.DynamicBody, ballFix);
		ballBody.setUserData("Ball");
		newGame = false;
	}

	// Connector del cuerpo de la bola con su "Sprite".
	private void setBallPhysicsConnectors() {
		ballPC = new PhysicsConnector(ballSprite, ballBody, true, false);
		arkanoidPhysicsWorld.registerPhysicsConnector(ballPC);
	}

	// Connector del cuerpo de la plataforma con su "Sprite".
	private void setPlatformPhysicsConnectors() {
		platformPC = new PhysicsConnector(platformSprite, platformBody, true,
				false);
		arkanoidPhysicsWorld.registerPhysicsConnector(platformPC);
	}

	// "Sprites" de los muros.
	private void createWallSprites() {
		walls = new Rectangle[4];
		walls[0] = new Rectangle(camera.getWidth() / 2, camera.getHeight() + 3,
				camera.getWidth() + 6, 6, vbom);
		walls[1] = new Rectangle(camera.getWidth() / 2, -3,
				camera.getWidth() + 6, 6, vbom);
		walls[2] = new Rectangle(camera.getWidth() + 3, camera.getHeight() / 2,
				6, camera.getHeight(), vbom);
		walls[3] = new Rectangle(-3, camera.getHeight() / 2, 6,
				camera.getHeight(), vbom);
	}

	// Cuerpo de los muros.
	private void createWallBodies() {
		wallBodies = new Body[walls.length];
		for (int i = 0; i < walls.length; i++) {
			wallBodies[i] = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld,
					walls[i], BodyType.StaticBody, wallFix);
			wallBodies[i].setUserData("Wall" + i);
		}
	}

	/*
	 * Establece las instancias de "Brick" siguiendo un patrón establecido
	 * mediante variables ("booleans" en este caso).
	 */
	private void iLikeBricks() {
		initialX = 120;
		initialY = 16;
		grid = new int[2][15];
		int cont = 0;
		bricks = new ArrayList<Brick>(30);
		bricksRem = new ArrayList<Brick>(bricks.size());
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				bricks.add(new Brick(initialX, initialY,
						resourcesManager.ark_brick_region, vbom,
						arkanoidPhysicsWorld));
				initialY = initialY + 32;
				cont = cont + 1;
			}
			initialX = initialX + 16;
			initialY = 16;
		}
	}

	/*
	 * Crea la zona que será definida como región de "GAME OVER". A efectos
	 * prácticos se trata simplemente de otra entidad.
	 */
	private void createGORegion() {
		gORegion = new Rectangle(
				(camera.getWidth() - (120 - platformSprite.getWidth() / 2)),
				camera.getHeight() / 2, 2, camera.getHeight(), vbom);
		gOBody = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld, gORegion,
				BodyType.StaticBody, gOFix);
		gOBody.setUserData("GameOver");
		gORegion.setVisible(false);
	}

	/*
	 * Elimina el sprite de la bola y cambia el valor del booleano "notStarted".
	 */
	private void resetArkanoid() {
		destSprite(ballSprite, ballBody, ballPC);
		destSprite(platformSprite, platformBody, platformPC);
		createPlatformSprite();
		setPlatformPhysicsConnectors();
		createBallSprite();
		setBallPhysicsConnectors();
		SceneManager.getInstance().getCurrentScene().attachChild(ballSprite);
		createJoint();
		notStarted = true;
		touchedY = camera.getHeight()/2;
	}

	private void destSprite(Sprite sprite, Body body, PhysicsConnector pc) {
		arkanoidPhysicsWorld.unregisterPhysicsConnector(pc);
		pc = null;
		body.setLinearVelocity(0.0f, 0.0f);
		arkanoidPhysicsWorld.destroyBody(body);
		body = null;
		try{
			SceneManager.getInstance().getCurrentScene().detachChild(sprite);
			sprite.dispose();
		}
		catch(Exception ex){}
		sprite = null;
	}

	// Elimina los ladrillos del PhysicsWorld (cuerpos) y de la escena (sprites)
	private void removeBricks(ArrayList<Brick> bricksToRemove) {
		for (Brick brick : bricksToRemove) {
			arkanoidPhysicsWorld.destroyBody(brick.brickBody);
			this.detachChild(brick);
		}
		bricksToRemove.clear();
		bricksToRemove = null;
		bricksToRemove = new ArrayList<Brick>(bricks.size());
	}

	/*
	 * Obtiene la instancia de Brick cuyo "hashCode" del cuerpo definido como
	 * variable dentro de la clase coincide con el proporcionado como parámetro.
	 */
	private Brick getBrick(int hashCode) {
		for (Brick brk : bricks) {
			if (brk.brickBody.hashCode() == hashCode) {
				return brk;
			}
		}
		return null;
	}

	/*
	 * Genera un vector normalizado dentro de un intervalo específico. En este
	 * caso un ángulo de 90 grados sexagesimales cuya bisectriz sería una recta
	 * normal a la superficie de la plataforma (más o menos).
	 */
	private Vector2 normVecGen() {
		Vector2 iniSpeed = new Vector2();
		float y = -1;
		float x = 0;
		while (y < -.70 || y > .70) {
			x = new Random().nextFloat();
			y = (float) Math.sqrt(1 - (x * x));
		}
		int _switch = new Random().nextInt(2);
		if (_switch == 0) {
			iniSpeed.set(-x, y);
		} else {
			iniSpeed.set(-x, -y);
		}
		return iniSpeed;
	}

	/*
	 * Determina las acciones que se efectuarán al pulsar la tecla de retroceso
	 * del dispositivo.
	 */
	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine, this);
		camera.setHUD(null);
	}

	// Devuelve el tipo de escena.
	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_ARKANOID;
	}

	/*
	 * Elimina las diferentes entidades, físicas y conectores así como la escena
	 * actual.
	 */
	@Override
	public void disposeScene() {
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				try{
					arkanoidPhysicsWorld.clearForces();
					for (int i = 0; i < wallBodies.length; i++) {
						arkanoidPhysicsWorld.destroyBody(wallBodies[i]);
					}
					arkanoidPhysicsWorld.destroyBody(platformBody);
					arkanoidPhysicsWorld.destroyBody(ballBody);
	
					SceneManager.getInstance().getCurrentScene()
							.detachChild(platformSprite);
					SceneManager.getInstance().getCurrentScene()
							.detachChild(ballSprite);
					removeBricks(bricks);
					bricks.clear();
					for (int i = 0; i < walls.length; i++) {
						SceneManager.getInstance().getCurrentScene()
								.detachChild(walls[i]);
						walls[i].dispose();
					}
					platformSprite.dispose();
					ballSprite.dispose();
	
					SceneManager.getInstance().getCurrentScene().detachSelf();
					SceneManager.getInstance().getCurrentScene().dispose();
				}
				catch(Exception ex){}
			}
		});

	}

	// "Listener" de interacción con la pantalla
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		/*
		 * Evento de inicio: vector velocidad aleatorio y destrucción del
		 * "joint" que une bola y plataforma.
		 */
		if (pSceneTouchEvent.isActionUp() && notStarted) {
			modSpeed = SPEED.len();
			a = (platformSprite.getX() - bricks.get(22).getX() - 16) / pmr;
			b = (bricks.get(22).getHeight() / 2) / pmr;
			y = Math.sqrt(a * a + b * b);

			test = new Vector2(-(float) ((modSpeed * a) / y),
					(float) ((modSpeed * b) / y));
			notStarted = false;
			ballBody.setLinearVelocity(normVecGen().mul(modSpeed));
			// ballBody.setLinearVelocity(test);
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					arkanoidPhysicsWorld.destroyJoint(ball_plat_Joint);
					platformBody.setLinearVelocity(0, 0);
				}
			});
		}
		else if(pSceneTouchEvent.isActionUp() )
		{
			touchedY = pSceneTouchEvent.getY();
			registerUpdateHandler(timerhandler);
		}
		else{
			if(pSceneTouchEvent.isActionDown() && !notStarted)
				unregisterUpdateHandler(timerhandler);
			if(pSceneTouchEvent.getY() -5 > platformSprite.getY() && platformSprite.getY() + platformSprite.getHeight()/2 < camera.getHeight())
				platformBody.setLinearVelocity(new Vector2(0,10.0f));
			else if(pSceneTouchEvent.getY()+5 < platformSprite.getY() && platformSprite.getY() - platformSprite.getHeight()/2 > 0)
				platformBody.setLinearVelocity(new Vector2(0,-10.0f));
			else
				platformBody.setLinearVelocity(new Vector2(0,0));}
		return false;
	}

	@Override
	public void beginContact(Contact contact) {
		// Colisiones.
		if (contact.getFixtureA().getBody().getUserData().toString()
				.equals("Platform")) {
			// Efecto de colisión de la bola con la plataforma (cuanto más
			// cercano el punto de colisión más paralelo es el vector velocidad
			// a la superficie de la plataforma; el sentido de la componente "y"
			// es igual a el de la componente "y" del vector que define el punto
			// de colisión en la plataforma tomando como origen de coordenadas
			// el centro de la misma).
			Vector2[] contactPoints = contact.getWorldManifold().getPoints();
			float speedMagnitude1 = ballBody.getLinearVelocity().len();
			float yCom = (contactPoints[0].y - platformBody.getPosition().y)
					/ ((platformSprite.getHeight() / 2) / pmr);
			if (yCom < 1 && yCom > -1) {
				float xCom = (float) (Math.sqrt(1 - (yCom * yCom)));
				Vector2 normVector = new Vector2(xCom, yCom);
				ballBody.setLinearVelocity(normVector.mul(speedMagnitude1));
			}
		} else if (contact.getFixtureA().getBody().getUserData().toString()
				.substring(0, 4).equals("Wall")) {
			/*
			 * Establece un vector velocidad a la bola (si el ángulo de
			 * incidencia se aproximaba a 0 ó 180 grados el ángulo de reflexión
			 * se establecía en 0 grados; debido a la precisión de las variables
			 * la bola se quedaba atascada rebotando infinitamente).
			 */
			String key = contact.getFixtureA().getBody().getUserData()
					.toString();
			Vector2 normSpeed = ballBody.getLinearVelocity().nor();
			Vector2 newNormSpeed;
			if (key.equals("Wall3")) {
				newNormSpeed = new Vector2(-normSpeed.x, normSpeed.y);
				ballBody.setLinearVelocity(newNormSpeed.mul(modSpeed));
			} else {
				newNormSpeed = new Vector2(normSpeed.x, -normSpeed.y);
				ballBody.setLinearVelocity(newNormSpeed.mul(modSpeed));
			}
		} else if (contact.getFixtureA().getBody().getUserData().toString()
				.equals("GameOver")) {
			/*
			 * Al entrar en contacto con la región "GAME OVER" se reinicia la
			 * posición de la bola (se destruye y crea de nuevo así como sus
			 * físicas).
			 */
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					destSprite(ballSprite, ballBody, ballPC);
					notStarted = true;
					touchedY = camera.getHeight()/2;
					createBallSprite();
					setBallPhysicsConnectors();
					createJoint();
					SceneManager.getInstance().getCurrentScene()
							.attachChild(ballSprite);

				}
			});
		}

	}

	@Override
	public void endContact(Contact contact) {

		/*
		 * Al terminar el contacto con una objeto "Brick", este se destruye, se
		 * suma la puntuación y se resta a la cantidad total de objetos "Brick".
		 */
		if (contact.getFixtureA().getBody().getUserData().toString()
				.equals("Brick")) {

			bricksRem.add(getBrick(contact.getFixtureA().getBody().hashCode()));

			addScore(100);

			bricksAmount = bricksAmount - 1;
			// Cuando la cantidad llegué a cero se habrá completado el nivel.

			if (bricksAmount <= 25) {

				if (this.session != null) {
					this.session.setScore(getScore());
					SceneManager.getInstance().createSnakeGameScene(session);
				}

			}

		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}