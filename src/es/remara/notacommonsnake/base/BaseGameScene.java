package es.remara.notacommonsnake.base;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

public abstract class BaseGameScene extends BaseScene {

	private int score = 0;

	private HUD gameHUD;
	private Text scoreText;
	private Text titleText;

	public int getScore(){
		return score;
	}
	
	public void createHUD() {
		gameHUD = new HUD();

		scoreText = new Text(20, 435, resourcesManager.font,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setAnchorCenter(0, 0);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);
		
		titleText = new Text(665, 435, resourcesManager.font,
				"Level: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		titleText.setAnchorCenter(0, 0);
		titleText.setText("");
		gameHUD.attachChild(titleText);

		camera.setHUD(gameHUD);
	}

	public void setLevelTitle(String string){
		titleText.setText("Level " + string);
	}
	
	public void setTitle(String string){
		titleText.setText(string);
	}
	
	public void addScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

}
