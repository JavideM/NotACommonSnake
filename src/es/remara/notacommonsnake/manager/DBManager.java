package es.remara.notacommonsnake.manager;

import java.util.ArrayList;
import java.util.List;

import es.remara.notacommonsnake.model.Achievement;
import es.remara.notacommonsnake.model.Session;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

	 // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
	
	/**
	 * Tablas
	 */
	
	/*
	 * Session
	 */
	// Table Name
	private final String SESSIONS_TABLE = "SESSIONS";
	
	//Columns
	private final String IDSESSION = "IDSESSION";
	private final String PLAYER_NAME = "PLAYER_NAME";
	private final String SCORE = "SCORE";
	
	private final String sessionsCreate = "CREATE TABLE " + SESSIONS_TABLE  +
												" ("+ IDSESSION + " INTEGER primary key not null, " +
												PLAYER_NAME + " TEXT not null, " +
												SCORE + " INTEGER not null)";
	
	/*
	 * Achievements
	 */
	//Table Name
	private final String ACHIEVEMENTS_TABLE = "ACHIEVEMENTS";
	
	//Columns
	private final String IDACHIEVEMENT = "IDACHIEVEMENT";
	private final String ACH_NAME = "NAME";
	private final String ACH_DESCRIP = "DESCRIPTION";
	private final String ACH_DONE = "DONE";
	private final String ACH_IDSESSION = IDSESSION;
	
	private String achievementsCreate = "CREATE TABLE " + ACHIEVEMENTS_TABLE + 
											" ("+IDACHIEVEMENT + " INTEGER primary key, " +
												ACH_NAME + " TEXT not null, " +
												ACH_DESCRIP + " TEXT, " +
												ACH_DONE + " TEXT, " +
												ACH_IDSESSION + " INTEGER, " +
												"FOREIGN KEY("+ACH_IDSESSION+") REFERENCES "+ SESSIONS_TABLE +"("+IDSESSION+"))";
	
	
	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sessionsCreate);
		db.execSQL(achievementsCreate);
		insertAchievements(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST " + SESSIONS_TABLE);
		db.execSQL("DROP TABLE IF EXIST " + ACHIEVEMENTS_TABLE);
		
		onCreate(db);
	}

	/*
	 * Table Sessions CRUD 
	 */
	
	public void saveSession(Session session)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME, session.getPlayer_name()); 
        values.put(SCORE, session.getScore());
        
        db.insert(SESSIONS_TABLE, null, values);
        db.close();
	}
	
	public List<Session> getAllSessionsByScore() {
        List<Session> sessionList = new ArrayList<Session>();
        // Select All Query
        String selectQuery = "SELECT * " +
    						" FROM " + SESSIONS_TABLE + " ORDER BY " +SCORE+ " DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Session session = new Session();
                session.setIdSession(Integer.parseInt(cursor.getString(0)));
                session.setPlayer_name(cursor.getString(1));
                session.setScore(Integer.parseInt(cursor.getString(2)));
                // Adding session to list
                sessionList.add(session);
            } while (cursor.moveToNext());
        }
       
        // return session list
        return sessionList;
    }
	
	/*
	 * Achievements
	 */
	
	private void insertAchievements(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(ACH_NAME, "First meal.");
		values.put(ACH_DESCRIP, "Eat your first food.");
		values.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values);
	}
	
	public List<Achievement> getAllAchievements() {
        List<Achievement> achievementList = new ArrayList<Achievement>();
        // Select All Query
        String selectQuery = "SELECT * " +
    						" FROM " + ACHIEVEMENTS_TABLE + " ORDER BY " + IDACHIEVEMENT + "";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.setId(Integer.parseInt(cursor.getString(0)));
                achievement.setName(cursor.getString(1));
                achievement.setDescription(cursor.getString(2));
                achievement.setCheck(Integer.parseInt(cursor.getString(3)));
                achievement.setIdSession(Integer.parseInt(cursor.getString(4)));
                // Adding session to list
                achievementList.add(achievement);
            } while (cursor.moveToNext());
        }
       
        // return session list
        return achievementList;
    }
}