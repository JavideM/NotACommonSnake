package es.remara.notacommonsnake.model;

import es.remara.notacommonsnake.manager.DBManager;

public class Session {

	/*
	 * Parameters
	 */

	private int idSession;
	private String player_name;
	private int score;

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

	// Constructor
	public Session() {
		//
		// Constructor logic
		//
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
