package es.remara.notacommonsnake.model;

public class Achievement {
	/*
	 * Parameters
	 */
	private int id;
	private String name;
	private String description;
	private int idsession;
	private boolean check;

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

	public int getIdsession() {
		return this.idsession;
	}

	public void setIdsession(int idsession) {
		this.idsession = idsession;
	}

	public boolean getCheck() {
		return check;
	}

	public void setCheck(boolean check) {
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
