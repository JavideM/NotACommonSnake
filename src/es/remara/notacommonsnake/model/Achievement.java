package es.remara.notacommonsnake.model;

public class Achievement {
	/*
	 * Parameters
	 */
	private int id;
	private String name;
	private String description;
	private int idSession;
	private int check;

	/*
	 * Getters & Setters
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIdSession() {
		return idSession;
	}

	public void setIdSession(int idSession) {
		this.idSession = idSession;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	/*
	 * Constructor
	 */
	public Achievement() {
		//
		// Contructor logic
		//
	}

	/*
	 * Methods
	 */

}