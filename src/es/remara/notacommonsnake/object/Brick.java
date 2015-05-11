package es.remara.notacommonsnake.object;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Brick extends Sprite {

	public Body brickBody;

	private final FixtureDef brickFix = PhysicsFactory.createFixtureDef(0.0f,
			0.0f, 0.0f);

	public Brick(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pSpriteVertexBufferObject,
			PhysicsWorld pWorld) {
		super(pX, pY, pTextureRegion, pSpriteVertexBufferObject);

		brickBody = PhysicsFactory.createBoxBody(pWorld, this,
				BodyType.KinematicBody, this.brickFix);
		brickBody.setUserData(Brick.this);
		pWorld.registerPhysicsConnector(new PhysicsConnector(this, brickBody,
				true, false));
	}
}
