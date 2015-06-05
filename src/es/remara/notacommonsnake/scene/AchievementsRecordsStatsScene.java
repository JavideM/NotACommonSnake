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
import es.remara.notacommonsnake.model.Achievement;
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
	private Sprite btnback;

	private List<Text> recordslist;
	private List<Text> achievementslist;

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
		btnback = new Sprite(45, 35, resourcesManager.back_region, vbom){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					onBackKeyPressed();
				
				return true;
			}
		};
		attachChild(btntrophy);
		registerTouchArea(btntrophy);
		attachChild(btnstats);
		registerTouchArea(btnstats);
		attachChild(btnmedal);
		registerTouchArea(btnmedal);
		attachChild(btnback);
		registerTouchArea(btnback);
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
		//Main Content
		//Achievements
				int count = 1;
				achievementslist = new ArrayList<Text>();
				ArrayList<Achievement> achievements = (ArrayList<Achievement>) dbmanager
						.getAllAchievementsDone();
				for (Achievement achievement : achievements) {
					String content = count + ". " + achievement.getName();
					// Name and position
					Text textName = new Text(camera.getHeight() / 2,
							camera.getHeight()
									- (camera.getHeight() / 12 + camera.getHeight()
											/ 12 * count), resourcesManager.fontARS,
							content, new TextOptions(HorizontalAlign.LEFT), this.vbom);
					textName.setPosition(textName.getX() + textName.getWidth()
							/ 2, textName.getY());
					// Description
					content = achievement.getDescription();
					Text textDescript = new Text(600, camera.getHeight()
							- (camera.getHeight() / 12 + camera.getHeight() / 12
									* count), resourcesManager.fontARS, content,
							new TextOptions(HorizontalAlign.LEFT), this.vbom);
					textDescript.setPosition(textDescript.getX() - textDescript.getWidth() / 2,
							textDescript.getY());
					achievementsPannel.attachChild(textName);
					achievementsPannel.attachChild(textDescript);
					achievementslist.add(textName);
					achievementslist.add(textDescript);
					if (count > 8) {
						textName.setVisible(false);
						textDescript.setVisible(false);
					}
					count++;
				}
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
				if(recordsPannel.isVisible()){
					if (pSceneTouchEvent.isActionDown())
						positionIn = pTouchAreaLocalY;
					float distance = (positionIn - pTouchAreaLocalY) / 10;
					if ((recordslist.get(0).getY() - distance) >= top_ini_position - 5
							&& (recordslist.get(recordslist.size() - 1).getY() - distance <= bottom_ini_position + 5)) {
						for (int i = 0; i < recordslist.size(); i++) {
							recordslist.get(i).setPosition(recordslist.get(i).getX(),
									recordslist.get(i).getY() - distance);
							if (recordslist.get(i).getY() >= top_ini_position + 5
									|| recordslist.get(i).getY() <= bottom_ini_position - 5) {
								recordslist.get(i).setVisible(false);
							} else {
								recordslist.get(i).setVisible(true);
							}
						}
					}
				}
				return true;
			}
		};
		recordsPannel.attachChild(pannel);

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
		recordslist = new ArrayList<Text>();
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
			content = session.getScore() + "pts";
			Text textScore = new Text(600, camera.getHeight()
					- (camera.getHeight() / 12 + camera.getHeight() / 12
							* count), resourcesManager.fontARS, content,
					new TextOptions(HorizontalAlign.LEFT), this.vbom);
			textScore.setPosition(textScore.getX() - textScore.getWidth() / 2,
					textScore.getY());
			recordsPannel.attachChild(textPlayer);
			recordsPannel.attachChild(textScore);
			recordslist.add(textPlayer);
			recordslist.add(textScore);
			if (count > 8) {
				textPlayer.setVisible(false);
				textScore.setVisible(false);
			}
			count++;
		}
		if(count > 1)
			registerTouchArea(pannel);
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
		Sprite wip = new Sprite(camera.getWidth()/2, camera.getHeight()/2, resourcesManager.wip_region, vbom);
		statisticPannel.attachChild(wip);
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
