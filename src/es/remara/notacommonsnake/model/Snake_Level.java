package es.remara.notacommonsnake.model;

import java.util.ArrayList;

public class Snake_Level {
	
	/*
	 * Parámetros
	 */
	private int idsnake_level;
	private int score;
	private int idsession;
	private ArrayList<Arkanoid_Level> arkanoid_levels;
	private ArrayList<Food_Level> foods_level;
	
	/*
	 * Getters Setters
	 */
	public int getIdsnake_level() {
		return idsnake_level;
	}
	public void setIdsnake_level(int idsnake_level) {
		this.idsnake_level = idsnake_level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getIdsession() {
		return idsession;
	}
	public void setIdsession(int idsession) {
		this.idsession = idsession;
	}
	public ArrayList<Arkanoid_Level> getArkanoid_levels() {
		return arkanoid_levels;
	}
	public void setArkanoid_levels(ArrayList<Arkanoid_Level> arkanoid_levels) {
		this.arkanoid_levels = arkanoid_levels;
	}
	
	public void addArkanoid_Level(Arkanoid_Level arkanoid_level){
		this.arkanoid_levels.add(arkanoid_level);
	}
	
	public ArrayList<Food_Level> getFoods_level() {
		return foods_level;
	}
	public void setFoods_level(ArrayList<Food_Level> foods_level) {
		this.foods_level = foods_level;
	}
	public void addFoodLevel(Food_Level food_level){
		this.foods_level.add(food_level);
	}
	/*
	 * Constructor
	 */
	public Snake_Level(){
		//
		// Constructor Logic here
		//
		this.arkanoid_levels = new ArrayList<Arkanoid_Level>();
	}
}
