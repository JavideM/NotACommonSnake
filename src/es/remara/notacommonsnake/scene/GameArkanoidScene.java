package es.remara.notacommonsnake.scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;

public class GameArkanoidScene extends BaseScene {

	// Provisional. Se cambiara por sprites (supongo...)
	private Rectangle platform, ground, roof, left_wall, right_wall;

	private FixtureDef wallPlatFix, ballFix;

	private Body platformBody, ballBody;

	private PhysicsWorld arkanoidPhysicsWorld;

	private Sprite platformSprite, ballSprite;

	@Override
	public void createScene() {
		setBackground(new Background(0, 100, 200));
		arkanoidPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		SceneManager.getInstance().getCurrentScene()
				.registerUpdateHandler(arkanoidPhysicsWorld);
	}

	private void createWalls() {
		roof = new Rectangle(camera.getWidth() / 2, camera.getHeight() - 3,
				camera.getWidth(), 6, engine.getVertexBufferObjectManager());
		ground = new Rectangle(camera.getWidth() / 2, 0, camera.getWidth(), 12,
				engine.getVertexBufferObjectManager());
		right_wall = new Rectangle(camera.getWidth() - 3,
				camera.getHeight() / 2, 6, camera.getHeight(),
				engine.getVertexBufferObjectManager());
		left_wall = new Rectangle(0, camera.getHeight() / 2, 12,
				camera.getHeight(), engine.getVertexBufferObjectManager());
	}

	private void createFixtures() {
		wallPlatFix = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		ballFix = PhysicsFactory.createFixtureDef(10.0f, 1.0f, 0.0f);
	}

	// Provisional
	private void createPlatform() {
		platform = new Rectangle(camera.getHeight() / 2, 60, 64, 12,
				engine.getVertexBufferObjectManager());
	}

	private void createBallSprite() {
		ballSprite = new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.ark_ball_region,
				engine.getVertexBufferObjectManager());
		ballBody = PhysicsFactory.createCircleBody(arkanoidPhysicsWorld,
				ballSprite, BodyType.DynamicBody, ballFix);
		arkanoidPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				ballSprite, ballBody, true, false));
	}

	private void createPlatformSprite() {
		platformBody = PhysicsFactory.createBoxBody(arkanoidPhysicsWorld,
				platform, BodyType.KinematicBody, wallPlatFix);
		arkanoidPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				platformSprite, platformBody, true, false));
	}

	private void createWallSprites() {
		// Muros
		PhysicsFactory.createBoxBody(arkanoidPhysicsWorld, ground,
				BodyType.StaticBody, wallPlatFix);
		PhysicsFactory.createBoxBody(arkanoidPhysicsWorld, roof,
				BodyType.StaticBody, wallPlatFix);
		PhysicsFactory.createBoxBody(arkanoidPhysicsWorld, right_wall,
				BodyType.StaticBody, wallPlatFix);
		PhysicsFactory.createBoxBody(arkanoidPhysicsWorld, left_wall,
				BodyType.StaticBody, wallPlatFix);
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
		

	}
}
