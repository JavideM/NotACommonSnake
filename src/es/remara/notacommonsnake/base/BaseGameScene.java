package es.remara.notacommonsnake.base;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

public abstract class BaseGameScene extends BaseScene {

	private int score = 0;
	
	private HUD gameHUD;
	private Text scoreText;
	
	
	public void createHUD()	{
		gameHUD = new HUD();
		
		scoreText = new Text(20, 420, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		scoreText.setAnchorCenter(0, 0);	
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);
		
		camera.setHUD(gameHUD);
	}
	
	public void addScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

}
