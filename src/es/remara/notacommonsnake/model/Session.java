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
	private ArrayList<Snake_Level> snake_levels;

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
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void AddSnake_Level(Snake_Level snake_level){
		this.snake_levels.add(snake_level);
	}
	
	public ArrayList<Snake_Level> getSnake_levels() {
		return snake_levels;
	}


	// Constructor
	public Session() {
		//
		// Constructor logic
		//
		this.snake_levels = new ArrayList<Snake_Level>();
	}

	/*
	 * Methods
	 */

	public void save(DBManager dbmanager) {
		dbmanager.saveSession(this);
	}

	public void update(DBManager dbmanager) {

	}
}
