package es.remara.notacommonsnake.scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class GameArkanoidScene extends BaseScene implements
		IOnSceneTouchListener {

	// Provisional. Se cambiara por sprites (supongo...)
	private Rectangle[] rectangles;

	private Body wall_body;

	private Rectangle platform;

	private FixtureDef wallPlatFix, ballFix;

	private Body platformBody, ballBody;

	private PhysicsWorld arkanoidPhysicsWorld;

	@SuppressWarnings("unused")
	private Sprite platformSprite, ballSprite;

	@Override
	public void createScene() {
		setBackground(new Background(0, 100, 200));
		arkanoidPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		registerUpdateHandler(arkanoidPhysicsWorld);
		createFixtures();
		createWallSprites();
		createWallBodies();
		createBallSprite();
		createPlatformSprite();
		setPhysicsConnectors();

	}

	private void createFixtures() {
		wallPlatFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		ballFix = PhysicsFactory.createFixtureDef(10.0f, 1.0f, 0.0f);
	}

	private void createBallSprite() {
		ballSprite = new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.ark_ball_region,
				engine.getVertexBufferObjectManager());
		ballBody = PhysicsFactory.createCircleBody(arkanoidPhysicsWorld,
				ballSprite, BodyType.DynamicBody, ballFix);
	}

	// Se cambiara por sprite (plataforma)
	private void createPlatformSprite() {
		platform = new Rectangle(camera.getHeight() / 2, 60, 64, 12,
				engine.getVertexBufferObjectManager());
		platformBody = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld,
				platform, BodyType.KinematicBody, wallPlatFix);
	}

	private void setPhysicsConnectors() {
		arkanoidPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				ballSprite, ballBody, true, false));
		arkanoidPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				platform, platformBody, true, false));
	}

	private void createWallSprites() {
		rectangles[0] = new Rectangle(camera.getWidth() / 2,
				camera.getHeight() - 3, camera.getWidth(), 6,
				vbom);
		rectangles[1] = new Rectangle(camera.getWidth() / 2, 0,
				camera.getWidth(), 12, vbom);
		rectangles[2] = new Rectangle(camera.getWidth() - 3,
				camera.getHeight() / 2, 6, camera.getHeight(),
				vbom);
		rectangles[3] = new Rectangle(0, camera.getHeight() / 2, 12,
				camera.getHeight(), vbom);
	}

	private void createWallBodies() {
		// Muros
		for (int i = 0; i < rectangles.length; i++) {
			wall_body = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld,
					rectangles[i], BodyType.StaticBody, wallPlatFix);
		}
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		arkanoidPhysicsWorld.clearForces();
		arkanoidPhysicsWorld.destroyBody(wall_body);

		platform.detachSelf();
		platform.dispose();
		this.detachSelf();
	}

	@Override
	// Movimiento plataforma
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getX() > (10 + platform.getWidth() / 2)
				&& pSceneTouchEvent.getX() < camera.getWidth()
						- ((platform.getWidth() / 2) + 10)) {
			if (pSceneTouchEvent.isActionUp()) {

			} else {
				platformBody
						.setTransform(
								pSceneTouchEvent.getX()
										/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
								platform.getY()
										/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
								0.0f);
			}
		} else {
			if (pSceneTouchEvent.getX() < (10 + platform.getWidth() / 2)) {
				platformBody
						.setTransform(
								(10 + platform.getWidth() / 2)
										/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
								platform.getY()
										/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
								0.0f);
			} else {
				platformBody
						.setTransform(
								(camera.getWidth() - (10 + platform.getWidth() / 2))
										/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
								platform.getY()
										/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
								0.0f);
			}

		}
		return false;
	}
}
