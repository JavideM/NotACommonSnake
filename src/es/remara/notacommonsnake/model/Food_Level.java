package es.remara.notacommonsnake.model;

public class Food_Level {
	
	/*
	 * Parameters
	 */
	private int idfood_level;
	private int idsnake_level;
	private String Food_Type;
	
	/*
	 * Getters Setters
	 */
	public int getIdfood_level() {
		return idfood_level;
	}
	public void setIdfood_level(int idfood_level) {
		this.idfood_level = idfood_level;
	}
	public int getIdsnake_level() {
		return idsnake_level;
	}
	public void setIdsnake_level(int idsnake_level) {
		this.idsnake_level = idsnake_level;
	}
	public String getFood_Type() {
		return Food_Type;
	}
	public void setFood_Type(String food_Type) {
		Food_Type = food_Type;
	}
	
	/*
	 * Contructor
	 */
	public Food_Level(){
		//
		// Contructor logic here
		//
	}
}
