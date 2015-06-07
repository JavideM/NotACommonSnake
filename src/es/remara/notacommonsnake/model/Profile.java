package es.remara.notacommonsnake.model;

import java.util.ArrayList;

import es.remara.notacommonsnake.manager.DBManager;
import es.remara.notacommonsnake.manager.ResourcesManager;

public class Profile{
  /*
  * Parameters
  */
  private int idprofile;
  private String name;
  private boolean active;
  private ArrayList<Session> sessions;
  
  /*
  * Getters Setters
  */
  public int getIdprofile(){
    return this.idprofile;
  }
  public void setIdprofile(int idprofile){
    this.idprofile = idprofile;
  }
  public String getName(){
    return this.name;
  }
  public void setName(String name){
    this.name = name;
  }
  public boolean isActive(){
    return this.active;
  }
  public void setActive(boolean active){
    this.active = active;
  }
  public ArrayList<Session> getSessions(){
	  return this.sessions;
  }
  public void addSession(Session session){
	  this.sessions.add(session);
  }
  /*
  * Contructor
  */
  public Profile(){
    //
    // Contructor Logic Here
    //
	this.sessions = new ArrayList<Session>();
  }
  
  /*
   * Methods
   */
  public void save() {
	  ResourcesManager.getInstance().dbmanager.saveProfile(this);
	}
  
  public void update(){
	  ResourcesManager.getInstance().dbmanager.updateProfile(this);
  }
  
  public void delete(){
	  ResourcesManager.getInstance().dbmanager.deleteProfile(this);
  }
  
  public void activeProfile()
  {
	  ResourcesManager.getInstance().dbmanager.setActiveProfile(this);
	  this.active = true;
  }
}
