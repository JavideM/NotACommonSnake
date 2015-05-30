package es.remara.notacommonsnake.model;

import java.util.ArrayList;

import es.remara.notacommonsnake.manager.DBManager;

public class Session {

	/*
	 * Parameters
	 */

	private int idSession;
	private String player_name;
	private int score;
	private int level;
	private ArrayList<Snake_Level> snake_levels;
	private ArrayList<Achievement> achievements_achieved;

	/*
	 * Getters setters
	 */

	public int getIdSession() {
		return idSession;
	}

	public void setIdSession(int idSession) {
		this.idSession = idSession;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void AddSnake_Level(Snake_Level snake_level){
		this.snake_levels.add(snake_level);
	}
	
	public ArrayList<Achievement> getAchievements_achieved() {
		return this.achievements_achieved;
	}

	public ArrayList<Snake_Level> getSnake_levels() {
		return snake_levels;
	}

	public void addAchievement(Achievement achievement) {
		this.achievements_achieved.add(achievement);
	}
	// Constructor
	public Session() {
		//
		// Constructor logic
		//
		this.snake_levels = new ArrayList<Snake_Level>();
		this.achievements_achieved = new ArrayList<Achievement>();
	}

	/*
	 * Methods
	 */

	public void save(DBManager dbmanager) {
		dbmanager.saveSession(this);
	}

	public void update(DBManager dbmanager) {

	}
	
	// Choose the next level
		public int nextlevel() {
			if(level == 9)
				this.level = 0;
			else
				this.level = level + 1;
			return this.level;
		}
}
