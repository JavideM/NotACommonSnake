package es.remara.notacommonsnake.object;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Snake_Body_Part extends Sprite{
	
	private float true_rotation;
	private VertexBufferObjectManager vbom;
	
	
	public float getTrue_rotation() {
		return true_rotation;
	}

	public void setTrue_rotation(float true_rotation) {
		this.true_rotation = true_rotation;
	}

	public Snake_Body_Part(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObject){
		super( pX,  pY,  pTextureRegion,  pVertexBufferObject);
		true_rotation = this.getRotation();
		vbom = pVertexBufferObject;
	}
	
	public Snake_Body_Part clone(){
		Snake_Body_Part clone = new Snake_Body_Part(getX(), getY(), getTextureRegion(), vbom);
		clone.setTrue_rotation(getTrue_rotation());
		return clone;
	}
}
