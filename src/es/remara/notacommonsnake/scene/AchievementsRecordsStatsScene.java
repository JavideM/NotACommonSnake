package es.remara.notacommonsnake.scene;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

import es.remara.notacommonsnake.R;
import es.remara.notacommonsnake.base.BaseScene;
import es.remara.notacommonsnake.manager.SceneManager;
import es.remara.notacommonsnake.manager.SceneManager.SceneType;
import es.remara.notacommonsnake.model.Session;

public class AchievementsRecordsStatsScene extends BaseScene {

	/*
	 * Parameters
	 */
	private Entity achievementsPannel;
	private Entity statisticPannel;
	private Entity recordsPannel;
	private Sprite btnstats;
	private Sprite btntrophy;
	private Sprite btnmedal;

	private List<Text> textlist;

	private final float top_ini_position = camera.getHeight()
			- (camera.getHeight() / 12 + camera.getHeight() / 12);
	private final float bottom_ini_position = camera.getHeight()
			- (camera.getHeight() / 12 + camera.getHeight() / 12 * 8);

	@Override
	public void createScene() {
		createBackground();
		createControls();
		createAchievementsPannel();
		createStatsPannel();
		createRecordsPannel();

	}

	// Background
	private void createBackground() {
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				resourcesManager.background_grass_region, vbom));
	}

	private void createControls() {
		btntrophy = new Sprite(camera.getWidth() / 3, 35,
				resourcesManager.trophy_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				recordsPannel.setVisible(false);
				statisticPannel.setVisible(false);
				achievementsPannel.setVisible(true);
				return true;
			}
		};
		btnstats = new Sprite(camera.getWidth() / 2, 35,
				resourcesManager.stats_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				recordsPannel.setVisible(false);
				statisticPannel.setVisible(true);
				achievementsPannel.setVisible(false);
				return true;
			}
		};
		btnmedal = new Sprite(2 * camera.getWidth() / 3, 35,
				resourcesManager.medal_region, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				recordsPannel.setVisible(true);
				statisticPannel.setVisible(false);
				achievementsPannel.setVisible(false);
				return true;
			}
		};
		attachChild(btntrophy);
		registerTouchArea(btntrophy);
		attachChild(btnstats);
		registerTouchArea(btnstats);
		attachChild(btnmedal);
		registerTouchArea(btnmedal);
	}

	private void createAchievementsPannel() {
		achievementsPannel = new Entity();
		/*
		 * Pannel
		 */
		Sprite pannel = new Sprite(camera.getWidth() / 2,
				camera.getHeight() / 2 + 30,
				resourcesManager.orange_pannel_region, vbom);
		achievementsPannel.attachChild(pannel);
		attachChild(achievementsPannel);
		achievementsPannel.setVisible(false);

		/*
		 * Pannel Content
		 */
		// Title
		Text text = new Text(camera.getWidth() / 2, camera.getHeight()
				- camera.getHeight() / 12, resourcesManager.fonttitle,
				activity.getString(R.string.achievements_title),
				new TextOptions(HorizontalAlign.CENTER), this.vbom);
		achievementsPannel.attachChild(text);
	}

	private void createRecordsPannel() {
		recordsPannel = new Entity();
		/*
		 * Color Pannel
		 */
		Sprite pannel = new Sprite(camera.getWidth() / 2,
				camera.getHeight() / 2 + 30,
				resourcesManager.salmon_pannel_region, vbom) {
			float positionIn = 0;

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				// Scroll functionality
				if (pSceneTouchEvent.isActionDown())
					positionIn = pTouchAreaLocalY;
				float distance = (positionIn - pTouchAreaLocalY) / 10;
				if ((textlist.get(0).getY() - distance) >= top_ini_position - 5
						&& (textlist.get(textlist.size() - 1).getY() - distance <= bottom_ini_position + 5)) {
					for (int i = 0; i < textlist.size(); i++) {
						textlist.get(i).setPosition(textlist.get(i).getX(),
								textlist.get(i).getY() - distance);
						if (textlist.get(i).getY() >= top_ini_position + 5
								|| textlist.get(i).getY() <= bottom_ini_position - 5) {
							textlist.get(i).setVisible(false);
						} else {
							textlist.get(i).setVisible(true);
						}
					}
				}
				return true;
			}
		};
		recordsPannel.attachChild(pannel);
		registerTouchArea(pannel);

		/*
		 * Pannel Content
		 */
		// Title
		Text text = new Text(camera.getWidth() / 2, camera.getHeight()
				- camera.getHeight() / 12, resourcesManager.fonttitle,
				activity.getString(R.string.records_title), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		recordsPannel.attachChild(text);

		// Records
		int count = 1;
		textlist = new ArrayList<Text>();
		ArrayList<Session> sessions = (ArrayList<Session>) dbmanager
				.getAllSessionsByScore();
		for (Session session : sessions) {
			String content = count + ". " + session.getPlayer_name();
			// Name and position
			Text textPlayer = new Text(camera.getHeight() / 2,
					camera.getHeight()
							- (camera.getHeight() / 12 + camera.getHeight()
									/ 12 * count), resourcesManager.fontARS,
					content, new TextOptions(HorizontalAlign.LEFT), this.vbom);
			textPlayer.setPosition(textPlayer.getX() + textPlayer.getWidth()
					/ 2, textPlayer.getY());
			// Score
			content = session.getScore() + "ptos";
			Text textScore = new Text(600, camera.getHeight()
					- (camera.getHeight() / 12 + camera.getHeight() / 12
							* count), resourcesManager.fontARS, content,
					new TextOptions(HorizontalAlign.LEFT), this.vbom);
			textScore.setPosition(textScore.getX() - textScore.getWidth() / 2,
					textScore.getY());
			recordsPannel.attachChild(textPlayer);
			recordsPannel.attachChild(textScore);
			textlist.add(textPlayer);
			textlist.add(textScore);
			if (count > 8) {
				textPlayer.setVisible(false);
				textScore.setVisible(false);
			}
			count++;
		}
		attachChild(recordsPannel);
	}

	private void createStatsPannel() {
		statisticPannel = new Entity();
		/*
		 * Color Pannel
		 */
		Sprite pannel = new Sprite(camera.getWidth() / 2,
				camera.getHeight() / 2 + 30,
				resourcesManager.blue_pannel_region, vbom);
		statisticPannel.attachChild(pannel);
		attachChild(statisticPannel);
		statisticPannel.setVisible(false);

		/*
		 * Pannel Content
		 */
		// Title
		Text text = new Text(camera.getWidth() / 2, camera.getHeight()
				- camera.getHeight() / 12, resourcesManager.fonttitle,
				activity.getString(R.string.stats_title), new TextOptions(
						HorizontalAlign.CENTER), this.vbom);
		statisticPannel.attachChild(text);
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
