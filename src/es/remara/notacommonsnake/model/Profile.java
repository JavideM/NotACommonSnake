package es.remara.notacommonsnake.model;

import java.util.ArrayList;

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
  /*
  * Contructor
  */
  public Profile(){
    //
    // Contructor Logic Here
    //
  }
}
