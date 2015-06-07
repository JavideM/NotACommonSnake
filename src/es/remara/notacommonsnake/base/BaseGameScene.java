package es.remara.notacommonsnake.base;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

public abstract class BaseGameScene extends BaseScene {

	private int score = 0;

	private HUD gameHUD;
	private Text scoreText;
	public Text titleText;
	public Text timeText;
	private Text modeText;

	public int getScore() {
		return score;
	}

	public void createHUD() {
		gameHUD = new HUD();
		// Score
		scoreText = new Text(20, 435, resourcesManager.font,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setAnchorCenter(0, 0);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);

		// Title
		titleText = new Text(665, 435, resourcesManager.font,
				"Level: 0123456789", new TextOptions(HorizontalAlign.RIGHT),
				vbom);
		titleText.setAnchorCenter(0, 0);
		titleText.setText("");
		gameHUD.attachChild(titleText);

		// Mode
		modeText = new Text(20, 12, resourcesManager.font,
				"Very very very very looooong text really long in case",
				new TextOptions(HorizontalAlign.LEFT), vbom);
		modeText.setAnchorCenter(0, 0);
		modeText.setText("");
		gameHUD.attachChild(modeText);

		// Mode
		timeText = new Text(665, 12, resourcesManager.font, "99.9 s",
				new TextOptions(HorizontalAlign.RIGHT), vbom);
		timeText.setAnchorCenter(0, 0);
		timeText.setText("");
		gameHUD.attachChild(timeText);

		camera.setHUD(gameHUD);
	}

	public void setLevelTitle(String string) {
		titleText.setText("Level " + string);
	}

	public void setTitle(String title) {
		titleText.setText(title);
	}

	public void setTime(String time) {
		timeText.setText(time);
	}

	public void addScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

	public void resetScore() {
		scoreText.setText("Score: " + 0);
		score = 0;
	}

	public void setModetext(String mode) {
		modeText.setText(mode);
	}
}
