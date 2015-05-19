package es.remara.notacommonsnake.scene;

import java.util.Random;

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
import es.remara.notacommonsnake.object.Brick;

public class GameArkanoidScene extends BaseGameScene implements
		IOnSceneTouchListener, ContactListener {

	// Provisional. Se cambiara por sprites (supongo...)
	private Rectangle[] walls;

	private Body[] wallBodies;

	private Rectangle gORegion;

	private Brick[] bricks;

	private int[][] grid;

	private FixtureDef wallFix, ballFix, platFix, gOFix;

	private Body platformBody, ballBody, gOBody;

	private PhysicsWorld arkanoidPhysicsWorld;

	private PhysicsConnector ballPC, platformPC;

	private final Vector2 SPEED = new Vector2(7.0f, 7.0f);

	private boolean brickTime = false;

	private boolean notStarted = true;

	private boolean newGame;

	private int bricksAmount;

	private float modSpeed;

	private float initialX, initialY;

	private WeldJointDef ball_plat_JointDef;

	private Joint ball_plat_Joint;

	float pmr = PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

	private Sprite platformSprite, ballSprite;

	@Override
	public void createScene() {
		newGame = true;
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
		arkanoidPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		// Creación HUD y conservación de datos.
		createHUD();
		camera.getHUD().setRotation(-90.0f);
		camera.getHUD().setPosition(480, 0);
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
		// Listeners
		this.setOnSceneTouchListener(this);
		arkanoidPhysicsWorld.setContactListener(this);
	}

	private void attachChilds() {
		this.attachChild(ballSprite);
		this.attachChild(gORegion);
		for (int i = 0; i < walls.length; i++) {
			this.attachChild(walls[i]);
		}
		for (int i = 0; i < bricks.length; i++) {
			this.attachChild(bricks[i]);
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
		ballSprite = new Sprite((camera.getWidth() - 120) - 14, yPos,
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
		initialY = (camera.getHeight() / 2) - (6 * 22);
		grid = new int[3][7];
		bricks = new Brick[10];
		bricksAmount = bricks.length;
		int cont = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (brickTime) {
					bricks[cont] = new Brick(initialX, initialY,
							resourcesManager.ark_brick_region, vbom,
							arkanoidPhysicsWorld);
					brickTime = false;
					cont += 1;
				} else {
					brickTime = true;
				}
				initialY = initialY + 44;
			}
			initialY = (camera.getHeight() / 2) - (6 * 22);
			initialX = initialX + 22;
		}
		brickTime = false;
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
		arkanoidPhysicsWorld.unregisterPhysicsConnector(ballPC);
		ballPC = null;
		ballBody.setLinearVelocity(0.0f, 0.0f);
		arkanoidPhysicsWorld.destroyBody(ballBody);
		ballBody = null;
		SceneManager.getInstance().getCurrentScene().detachChild(ballSprite);
		ballSprite.dispose();
		ballSprite = null;
		notStarted = true;
	}

	/*
	 * Obtiene la instancia de Brick cuyo "hashCode" del cuerpo definido como
	 * variable dentro de la clase coincide con el proporcionado como parámetro.
	 */
	private Brick getBrick(int hashCode) {
		for (int i = 0; i < bricks.length; i++) {
			if (bricks[i].brickBody.hashCode() == hashCode) {
				return bricks[i];
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
		arkanoidPhysicsWorld.clearForces();
		for (int i = 0; i < wallBodies.length; i++) {
			arkanoidPhysicsWorld.destroyBody(wallBodies[i]);
		}
		arkanoidPhysicsWorld.destroyBody(platformBody);
		arkanoidPhysicsWorld.destroyBody(ballBody);

		this.detachChild(platformSprite);
		this.detachChild(ballSprite);
		for (int i = 0; i < bricks.length; i++) {
			SceneManager.getInstance().getCurrentScene().detachChild(bricks[i]);
			bricks[i].dispose();
		}
		for (int i = 0; i < walls.length; i++) {
			this.detachChild(walls[i]);
			walls[i].dispose();
		}
		platformSprite.dispose();
		ballSprite.dispose();

		this.detachSelf();
		this.dispose();
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
			notStarted = false;
			ballBody.setLinearVelocity(normVecGen().mul(modSpeed));
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					arkanoidPhysicsWorld.destroyJoint(ball_plat_Joint);
				}
			});
		}
		// Movimiento de la plataforma.
		if (pSceneTouchEvent.getY() > (platformSprite.getHeight() / 2)
				&& pSceneTouchEvent.getY() < camera.getHeight()
						- (platformSprite.getHeight() / 2)) {
			if (pSceneTouchEvent.isActionUp()) {

			} else {
				platformBody.setTransform(platformSprite.getX() / pmr,
						pSceneTouchEvent.getY() / pmr, 0.0f);
			}
		} else {
			if (pSceneTouchEvent.getY() < (platformSprite.getHeight() / 2)) {
				platformBody.setTransform(platformSprite.getX() / pmr,
						(platformSprite.getHeight() / 2) / pmr, 0.0f);
			} else {
				platformBody.setTransform(platformSprite.getX() / pmr,
						(camera.getHeight() - (platformSprite.getHeight() / 2))
								/ pmr, 0.0f);
			}

		}
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
					resetArkanoid();
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
			final Contact contact_aux = contact;
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					SceneManager
							.getInstance()
							.getCurrentScene()
							.detachChild(
									getBrick(contact_aux.getFixtureA()
											.getBody().hashCode()));
					arkanoidPhysicsWorld.destroyBody(contact_aux.getFixtureA()
							.getBody());
					addScore(100);
					bricksAmount = bricksAmount - 1;
				}
			});
		}
		// Cuando la cantidad llegué a cero se habrá completado el nivel.
		if (bricksAmount == 0) {

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