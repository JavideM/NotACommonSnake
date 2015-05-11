package es.remara.notacommonsnake.scene;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.adt.align.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;
import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.model.Session;

public class AchievementsRecordsStatsScene extends BaseScene{

	@Override
	public void createScene() {
		attachChild(new Sprite(camera.getWidth()/2, camera.getHeight()/2, resourcesManager.background_grass_region, vbom));
		BitmapTextureAtlas mFontTexture = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		Font font = new Font(this.engine.getFontManager(), mFontTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true,
				Color.WHITE);

		engine.getTextureManager().loadTexture(mFontTexture);
		engine.getFontManager().loadFont(font);
		Text text = new Text(200, camera.getHeight() - camera.getHeight()/12, font, "TOP SCORES" , new TextOptions(HorizontalAlign.CENTER), this.vbom);
		attachChild(text);
		
		int count = 1;
		
		ArrayList<Session> sessions = (ArrayList<Session>) dbmanager.getAllSessionsByScore();
		for(Session session: sessions){
			String textContent = count + "." +session.getPlayer_name() + " ........." + session.getScore() + "pts";
			Text textPlay = new Text(camera.getHeight()/2, camera.getHeight() - (camera.getHeight()/12 + camera.getHeight()/12*count), font ,textContent, new TextOptions(HorizontalAlign.CENTER), this.vbom);
			attachChild(textPlay);
			if(count == 10)
				break;
			count++;
		}
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine, this);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_ARS;
	}

	@Override
	public void disposeScene() {
		detachChildren();
		this.detachSelf();
		this.dispose();
	}

}
