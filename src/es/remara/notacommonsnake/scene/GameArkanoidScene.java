package es.remara.notacommonsnake.scene;

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
import com.badlogic.gdx.physics.box2d.Manifold;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class GameArkanoidScene extends BaseScene implements
		IOnSceneTouchListener, ContactListener {

	// Provisional. Se cambiara por sprites (supongo...)
	private Rectangle[] walls;

	private Body wall_body;

	private Rectangle platform, gORegion;

	private Rectangle[] lottaBricks;

	private int[][] grid;

	private Body[] bricksBodies;

	private FixtureDef wallFix, ballFix, platFix, brickFix, gOFix;

	private Body platformBody, ballBody, gOBody;

	private PhysicsWorld arkanoidPhysicsWorld;

	private PhysicsConnector ballPC, platformPC;

	private PhysicsConnector[] bricksPC;

	private boolean wannaBrick = false;

	private Boolean notStarted = true;

	private float initialX, initialY;

	float pmr = PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

	@SuppressWarnings("unused")
	private Sprite platformSprite, ballSprite;

	@Override
	public void createScene() {
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
		arkanoidPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		registerUpdateHandler(arkanoidPhysicsWorld);
		createFixtures();
		createWallSprites();
		createWallBodies();
		createBallSprite();
		createPlatformSprite();
		iLikeBricks();
		createGORegion();
		setSBPhysicsConnectors();
		setBallPhysicsConnectors();
		setPlatformPhysicsConnectors();
		attachChilds();
		this.setOnSceneTouchListener(this);
		arkanoidPhysicsWorld.setContactListener(this);
	}

	private void attachChilds() {
		this.attachChild(ballSprite);
		this.attachChild(platform);
		this.attachChild(gORegion);
		for (int i = 0; i < walls.length; i++) {
			this.attachChild(walls[i]);
		}
		for (int i = 0; i < lottaBricks.length; i++) {
			this.attachChild(lottaBricks[i]);
		}
	}

	private void createFixtures() {
		wallFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		platFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		ballFix = PhysicsFactory.createFixtureDef(10.0f, 1.0f, 0.0f);
		brickFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		gOFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
	}

	private void createBallSprite() {
		ballSprite = new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.ark_ball_region,
				engine.getVertexBufferObjectManager());
		ballBody = PhysicsFactory.createCircleBody(arkanoidPhysicsWorld,
				ballSprite, BodyType.DynamicBody, ballFix);
		ballBody.setUserData("Ball");
	}

	// Se cambiara por sprite (plataforma
	private void createPlatformSprite() {
		platform = new Rectangle(camera.getWidth() - 120,
				camera.getHeight() / 2, 12, 64,
				engine.getVertexBufferObjectManager());
		platformBody = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld,
				platform, BodyType.KinematicBody, platFix);
		platformBody.setUserData("Platform");
	}

	private void setSBPhysicsConnectors() {
		arkanoidPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				gORegion, gOBody, true, false));
	}

	private void setBallPhysicsConnectors() {
		ballPC = new PhysicsConnector(ballSprite, ballBody, true, false);
		arkanoidPhysicsWorld.registerPhysicsConnector(ballPC);
	}

	private void setPlatformPhysicsConnectors() {
		platformPC = new PhysicsConnector(platform, platformBody, true, false);
		arkanoidPhysicsWorld.registerPhysicsConnector(platformPC);
	}

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

	private void createWallBodies() {
		// Muros
		for (int i = 0; i < walls.length; i++) {
			wall_body = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld,
					walls[i], BodyType.StaticBody, wallFix);
			wall_body.setUserData("Wall");
		}
	}

	// Sir Bricks-A-Lot method
	private void iLikeBricks() {
		initialX = 120;
		initialY = (camera.getHeight() / 2) - (6 * 22);
		grid = new int[3][7];
		lottaBricks = new Rectangle[10];
		bricksBodies = new Body[lottaBricks.length];
		bricksPC = new PhysicsConnector[lottaBricks.length];
		int cont = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (wannaBrick) {
					lottaBricks[cont] = new Rectangle(initialX, initialY, 11,
							22, vbom);
					bricksBodies[cont] = PhysicsFactory.createBoxBody(
							arkanoidPhysicsWorld, lottaBricks[cont],
							BodyType.KinematicBody, brickFix);
					bricksBodies[cont].setUserData("Brick");
					bricksPC[cont] = new PhysicsConnector(lottaBricks[cont],
							bricksBodies[cont], true, false);
					arkanoidPhysicsWorld
							.registerPhysicsConnector(bricksPC[cont]);
					wannaBrick = false;
					cont += 1;
				} else {
					wannaBrick = true;
				}
				initialY = initialY + 44;
			}
			initialY = (camera.getHeight() / 2) - (6 * 22);
			initialX = initialX + 22;
		}
		wannaBrick = false;
	}

	private void createGORegion() {
		gORegion = new Rectangle(
				(camera.getWidth() - (120 - platform.getWidth() / 2)),
				camera.getHeight() / 2, 2, camera.getHeight(), vbom);
		gOBody = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld, gORegion,
				BodyType.StaticBody, gOFix);
		gOBody.setUserData("GameOver");
		gORegion.setVisible(false);
	}

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

	private void deleteBrick() {

	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine, this);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_ARKANOID;
	}

	@Override
	public void disposeScene() {
		arkanoidPhysicsWorld.clearForces();
		arkanoidPhysicsWorld.destroyBody(wall_body);
		arkanoidPhysicsWorld.destroyBody(platformBody);
		arkanoidPhysicsWorld.destroyBody(ballBody);

		this.detachChild(platform);
		this.detachChild(ballSprite);
		for (int i = 0; i < lottaBricks.length; i++) {
			arkanoidPhysicsWorld.destroyBody(bricksBodies[i]);
			this.detachChild(lottaBricks[i]);
			lottaBricks[i].dispose();
		}
		for (int i = 0; i < walls.length; i++) {
			this.detachChild(walls[i]);
			walls[i].dispose();
		}
		platform.dispose();
		ballSprite.dispose();

		this.detachSelf();
		this.dispose();
	}

	@Override
	// Movimiento plataforma
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionUp() && notStarted) {
			notStarted = false;
			ballBody.setLinearVelocity(7.0f, 7.0f);
		}
		if (pSceneTouchEvent.getY() > (platform.getHeight() / 2)
				&& pSceneTouchEvent.getY() < camera.getHeight()
						- (platform.getHeight() / 2)) {
			if (pSceneTouchEvent.isActionUp()) {

			} else {
				platformBody.setTransform(platform.getX() / pmr,
						pSceneTouchEvent.getY() / pmr, 0.0f);
			}
		} else {
			if (pSceneTouchEvent.getY() < (platform.getHeight() / 2)) {
				platformBody.setTransform(platform.getX() / pmr,
						(platform.getHeight() / 2) / pmr, 0.0f);
			} else {
				platformBody
						.setTransform(
								platform.getX() / pmr,
								(camera.getHeight() - (platform.getHeight() / 2))
										/ pmr, 0.0f);
			}

		}
		return false;
	}

	@Override
	public void beginContact(Contact contact) {
		Vector2[] contactPoints = contact.getWorldManifold().getPoints();
		if (contact.getFixtureA().getBody().getUserData().toString()
				.equals("Platform")) {
			float speedMagnitude1 = ballBody.getLinearVelocity().len();
			float yCom = (contactPoints[0].y - platformBody.getPosition().y)
					/ ((platform.getHeight() / 2) / pmr);
			if (yCom < 1 && yCom > -1) {
				float xCom = (float) (Math.sqrt(1 - (yCom * yCom)));
				Vector2 normVector = new Vector2(xCom, yCom);
				ballBody.setLinearVelocity(normVector.mul(speedMagnitude1));
			}
		} else if (contact.getFixtureA().getBody().getUserData().toString()
				.equals("GameOver")) {
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					resetArkanoid();
					createBallSprite();
					setBallPhysicsConnectors();
					SceneManager.getInstance().getCurrentScene()
							.attachChild(ballSprite);
				}
			});
		}

	}

	// Instante posterior a la colisión
	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA().getBody().getUserData().toString()
				.equals("Brick")) {
			final Contact contact_aux = contact;
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					arkanoidPhysicsWorld.destroyBody(contact_aux.getFixtureA()
							.getBody());

					// SceneManager.getInstance().getCurrentScene().detachChild(platform);
				}
			});
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