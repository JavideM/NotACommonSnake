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
	
	private ArrayList<Session> sessions;
	

	@Override
	public void createScene() {
		createBackground();
		createControls();
		chargeData();
		createAchievementsPannel();
		createStatsPannel();
		createRecordsPannel();
		
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

	//Charge data
	public void chargeData(){
		sessions = (ArrayList<Session>) dbmanager
				.getAllSessionsByScore();
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
		btnback = new Sprite(camera.getWidth() - 45, 35, resourcesManager.back_region, vbom){
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
				resourcesManager.orange_pannel_region, vbom){
			float positionIn = 0;

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				// Scroll functionality
				if(achievementsPannel.isVisible()){
					if (pSceneTouchEvent.isActionDown())
						positionIn = pTouchAreaLocalY;
					float distance = (positionIn - pTouchAreaLocalY) / 10;
					if ((achievementslist.get(0).getY() - distance) >= top_ini_position - 5
							&& (achievementslist.get(achievementslist.size() - 1).getY() - distance <= bottom_ini_position + 5)) {
						for (int i = 0; i < achievementslist.size(); i++) {
							achievementslist.get(i).setPosition(achievementslist.get(i).getX(),
									achievementslist.get(i).getY() - distance);
							if (achievementslist.get(i).getY() >= top_ini_position + 5
									|| achievementslist.get(i).getY() <= bottom_ini_position - 5) {
								achievementslist.get(i).setVisible(false);
							} else {
								achievementslist.get(i).setVisible(true);
							}
						}
					}
				}
				return true;
			}
		};
		achievementsPannel.attachChild(pannel);
		registerTouchArea(pannel);
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
			Text textName = new Text(camera.getWidth() / 5,
					camera.getHeight()
							- (camera.getHeight() / 12 + camera.getHeight()
									/ 12 * count), resourcesManager.fontARS,
					content, new TextOptions(HorizontalAlign.LEFT), this.vbom);
			textName.setPosition(textName.getX() + textName.getWidth()
					/ 2, textName.getY());
			// Description
			content = achievement.getDescription();
			Text textDescript = new Text(camera.getWidth()/2, camera.getHeight()
					- (camera.getHeight() / 12 + camera.getHeight() / 12
							* count), resourcesManager.fontARS, content,
					new TextOptions(HorizontalAlign.LEFT), this.vbom);
			textDescript.setPosition(textDescript.getX() + textDescript.getWidth() / 2,
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
		for (Session session : sessions) {
			String content = count + ". " + session.getPlayer_name();
			// Name and position
			Text textPlayer = new Text(camera.getWidth() / 4,
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
		//Main Content
		//Total score
		Text titleTotal = new Text(camera.getWidth() / 4,
				camera.getHeight()
				- (camera.getHeight() / 12 * 2), resourcesManager.fontARS,
						activity.getString(R.string.totalscore), new TextOptions(HorizontalAlign.LEFT), this.vbom);
		titleTotal.setPosition(titleTotal.getX() + titleTotal.getWidth()/2, titleTotal.getY());
		statisticPannel.attachChild(titleTotal);
		Text textTotal = new Text(3*camera.getWidth() / 4,
				camera.getHeight()
						- (camera.getHeight() / 12 * 2 ), resourcesManager.fontARS,
				""+totalScore(), new TextOptions(HorizontalAlign.RIGHT), this.vbom);
		statisticPannel.attachChild(textTotal);
		textTotal.setPosition(textTotal.getX() - textTotal.getWidth()/2, textTotal.getY());
		
		//Higher level
		Text titleHigher = new Text(camera.getWidth() / 4,
				camera.getHeight()
				- (camera.getHeight() / 12 *8 - 3*camera.getHeight()/ 8), resourcesManager.fontARS,
						activity.getString(R.string.higherlevel), new TextOptions(HorizontalAlign.LEFT), this.vbom);
		titleHigher.setPosition(titleHigher.getX() + titleHigher.getWidth()/2, titleHigher.getY());
		statisticPannel.attachChild(titleHigher);
		Text textHigherLevel = new Text(3*camera.getWidth() / 4,
				camera.getHeight()
						- (camera.getHeight() / 12 *8 - 3*camera.getHeight()/ 8), resourcesManager.fontARS,
				""+higherLevel(), new TextOptions(HorizontalAlign.RIGHT), this.vbom);
		statisticPannel.attachChild(textHigherLevel);
		textHigherLevel.setPosition(textHigherLevel.getX() - textHigherLevel.getWidth()/2, textHigherLevel.getY());
		
		//Sessions played
		Text titleSessions = new Text(camera.getWidth() / 4,
				camera.getHeight()
				- (camera.getHeight() / 12 *8 - camera.getHeight()/ 4), resourcesManager.fontARS,
						activity.getString(R.string.sessionsplayed), new TextOptions(HorizontalAlign.LEFT), this.vbom);
		titleSessions.setPosition(titleSessions.getX() + titleSessions.getWidth()/2, titleSessions.getY());
		statisticPannel.attachChild(titleSessions);
		Text textSessions = new Text(3*camera.getWidth() / 4,
				camera.getHeight()
						- (camera.getHeight() / 12 *8 - camera.getHeight()/ 4), resourcesManager.fontARS,
				""+sessions.size(), new TextOptions(HorizontalAlign.RIGHT), this.vbom);
		statisticPannel.attachChild(textSessions);
		textSessions.setPosition(textSessions.getX() - textSessions.getWidth()/2, textSessions.getY());
		
		//Average score
		Text titleAverage = new Text(camera.getWidth() / 4,
				camera.getHeight()
				- (camera.getHeight() / 12 *8 - camera.getHeight()/ 8), resourcesManager.fontARS,
						activity.getString(R.string.avergaescore), new TextOptions(HorizontalAlign.LEFT), this.vbom);
		titleAverage.setPosition(titleAverage.getX() + titleAverage.getWidth()/2, titleAverage.getY());
		statisticPannel.attachChild(titleAverage);
		Text textaverage = new Text(3*camera.getWidth() / 4,
				camera.getHeight()
						- (camera.getHeight() / 12 *8 - camera.getHeight()/ 8), resourcesManager.fontARS,
				""+averageScore(), new TextOptions(HorizontalAlign.RIGHT), this.vbom);
		statisticPannel.attachChild(textaverage);
		textaverage.setPosition(textaverage.getX() - textaverage.getWidth()/2, textaverage.getY());
		
		//Average per level
		Text titlePerLevel = new Text(camera.getWidth() / 4,
				camera.getHeight()
				- (camera.getHeight() / 12 *8), resourcesManager.fontARS,
						activity.getString(R.string.scoreperlevel), new TextOptions(HorizontalAlign.LEFT), this.vbom);
		titlePerLevel.setPosition(titlePerLevel.getX() + titlePerLevel.getWidth()/2, titlePerLevel.getY());
		statisticPannel.attachChild(titlePerLevel);
		Text textaverageScorePerLevel = new Text(3*camera.getWidth() / 4,
				camera.getHeight()
						- (camera.getHeight() / 12 * 8), resourcesManager.fontARS,
				""+averageScorePerLevel(), new TextOptions(HorizontalAlign.RIGHT), this.vbom);
		statisticPannel.attachChild(textaverageScorePerLevel);
		textaverageScorePerLevel.setPosition(textaverageScorePerLevel.getX() - textaverageScorePerLevel.getWidth()/2, textaverageScorePerLevel.getY());
	}

	
	/*
	 * Data Operations
	 */
	public double averageScore(){
		int total = totalScore();
		int countSessions = sessions.size();
		return (countSessions == 0)? 0:total/sessions.size();
	}
	
	public int totalScore(){
		int total= 0;
		for (Session session : sessions) {
			total += session.getScore();
		}
		return total;
	}
	
	public double averageScorePerLevel(){
		int total = totalScore();
		int totallevels = 0;
		for (Session session : sessions) {
			totallevels += session.getLevel() + 1;
		}
		return (totallevels == 0)? 0: total/totallevels;
	}

	public int higherLevel(){
		int higherLevel = 0;
		for (Session session : sessions) 
			if(session.getLevel() > higherLevel)
				higherLevel = session.getLevel();
		return higherLevel;
	}
}
