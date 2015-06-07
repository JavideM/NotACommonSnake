package es.remara.notacommonsnake.model;

import java.util.ArrayList;

import es.remara.notacommonsnake.manager.ResourcesManager;

public class Session {

	/*
	 * Parameters
	 */

	private int idSession;
	private String player_name;
	private int score;
	private int level;
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
	
	public ArrayList<Achievement> getAchievements_achieved() {
		return this.achievements_achieved;
	}



	public void addAchievement(Achievement achievement) {
		this.achievements_achieved.add(achievement);
	}
	// Constructor
	public Session() {
		//
		// Constructor logic
		//

		this.achievements_achieved = new ArrayList<Achievement>();
	}

	/*
	 * Methods
	 */

	public void save() {
		ResourcesManager.getInstance().dbmanager.saveSession(this);
	}

	public void update() {

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
