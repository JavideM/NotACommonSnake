package es.remara.notacommonsnake.manager;

import java.util.ArrayList;
import java.util.List;

import es.remara.notacommonsnake.GameActivity;
import es.remara.notacommonsnake.R;
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
	private final String SESSION_PLAYER_NAME = "PLAYER_NAME";
	private final String SESSION_SCORE = "SCORE";
	private final String SESSION_LEVEL = "LEVEL";
	//Script
	private final String sessionsCreate = "CREATE TABLE " + SESSIONS_TABLE  +
												" ("+ IDSESSION + " INTEGER primary key not null, " +
												SESSION_PLAYER_NAME + " TEXT not null, " +
												SESSION_SCORE + " INTEGER not null, " +
												SESSION_LEVEL + " INTEGER not null)";
	
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
	//Script
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
        values.put(SESSION_PLAYER_NAME, session.getPlayer_name()); 
        values.put(SESSION_LEVEL, session.getLevel());
        values.put(SESSION_SCORE, session.getScore());
        
        db.insert(SESSIONS_TABLE, null, values);
        db.close();
	}
	
	public List<Session> getAllSessionsByScore() {
        List<Session> sessionList = new ArrayList<Session>();
        // Select All Query
        String selectQuery = "SELECT * " +
    						" FROM " + SESSIONS_TABLE + " ORDER BY " +SESSION_SCORE+ " DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Session session = new Session();
                session.setIdSession(Integer.parseInt(cursor.getString(0)));
                session.setPlayer_name(cursor.getString(1));
                session.setScore(Integer.parseInt(cursor.getString(2)));
                session.setLevel(Integer.parseInt(cursor.getString(3)));
                // Adding session to list
                sessionList.add(session);
            } while (cursor.moveToNext());
        }
       
        // return session list
        return sessionList;
    }
	
	/*
	 * Achievements CRUD
	 */
	
	private void insertAchievements(SQLiteDatabase db) {
		//First points
		ContentValues values = new ContentValues();
		values.put(ACH_NAME, R.string.ach1title);
		values.put(ACH_DESCRIP, R.string.ach1descrip);
		values.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values);
		
		db.execSQL("CREATE TRIGGER ach1_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_SCORE + " >= 100 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 1 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		//300 points 
		ContentValues values2 = new ContentValues();
		values2.put(ACH_NAME, R.string.ach2title);
		values2.put(ACH_DESCRIP, R.string.ach2descrip);
		values2.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values2);
		
		db.execSQL("CREATE TRIGGER ach2_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_SCORE + " >= 300 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 2 AND "+ ACH_DONE+ "=0;" +
				"END;");
	
		// Over 9000 points
		ContentValues values3 = new ContentValues();
		values3.put(ACH_NAME, R.string.ach3title);
		values3.put(ACH_DESCRIP, R.string.ach3descrip);
		values3.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values3);
		
		db.execSQL("CREATE TRIGGER ach3_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_SCORE + " >= 9000 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 3 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		//Complete levels
		ContentValues values4 = new ContentValues();
		values4.put(ACH_NAME, R.string.ach4title);
		values4.put(ACH_DESCRIP, R.string.ach4descrip);
		values4.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values4);
		
		db.execSQL("CREATE TRIGGER ach4_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 0 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 4 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values5 = new ContentValues();
		values5.put(ACH_NAME, R.string.ach5title);
		values5.put(ACH_DESCRIP, R.string.ach5descrip);
		values5.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values5);
		
		db.execSQL("CREATE TRIGGER ach5_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 1 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 5 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values6 = new ContentValues();
		values6.put(ACH_NAME, R.string.ach6title);
		values6.put(ACH_DESCRIP, R.string.ach6descrip);
		values6.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values6);
		
		db.execSQL("CREATE TRIGGER ach6_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 2 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 6 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values7 = new ContentValues();
		values7.put(ACH_NAME, R.string.ach7title);
		values7.put(ACH_DESCRIP, R.string.ach7descrip);
		values7.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values7);
		
		db.execSQL("CREATE TRIGGER ach7_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 3 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 7 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values8 = new ContentValues();
		values8.put(ACH_NAME, R.string.ach8title);
		values8.put(ACH_DESCRIP, R.string.ach8descrip);
		values8.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values8);
		
		db.execSQL("CREATE TRIGGER ach8_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 4 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 8 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values9 = new ContentValues();
		values9.put(ACH_NAME, R.string.ach9title);
		values9.put(ACH_DESCRIP, R.string.ach9descrip);
		values9.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values9);
		
		db.execSQL("CREATE TRIGGER ach9_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 5 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 9 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values10 = new ContentValues();
		values10.put(ACH_NAME, R.string.ach10title);
		values10.put(ACH_DESCRIP, R.string.ach10descrip);
		values10.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values10);
		
		db.execSQL("CREATE TRIGGER ach10_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 6 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 10 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values11 = new ContentValues();
		values11.put(ACH_NAME, R.string.ach11title);
		values11.put(ACH_DESCRIP, R.string.ach11descrip);
		values11.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values11);
		
		db.execSQL("CREATE TRIGGER ach11_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 7 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 11 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		ContentValues values12 = new ContentValues();
		values12.put(ACH_NAME, R.string.ach12title);
		values12.put(ACH_DESCRIP, R.string.ach12descrip);
		values12.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values12);
		
		db.execSQL("CREATE TRIGGER ach12_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_LEVEL + " > 8 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 1, "+ ACH_IDSESSION + "=new." +IDSESSION 
					+ " WHERE "+ IDACHIEVEMENT +" = 12 AND "+ ACH_DONE+ "=0;" +
				"END;");
	}
	
	public List<Achievement> getAllAchievements() {
        List<Achievement> achievementList = new ArrayList<Achievement>();
        // Select All Query
        String selectQuery = "SELECT * " +
    						" FROM " + ACHIEVEMENTS_TABLE + " ORDER BY " + IDACHIEVEMENT + "";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        GameActivity activity = ResourcesManager.getInstance().activity;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.setId(Integer.parseInt(cursor.getString(0)));
                achievement.setName(activity.getString(Integer.parseInt(cursor.getString(1))));
                achievement.setDescription(activity.getString(Integer.parseInt(cursor.getString(2))));
                achievement.setCheck(Integer.parseInt(cursor.getString(3)) == 1);
                achievement.setIdsession(Integer.parseInt(cursor.getString(4)));
                // Adding achievement to list
                achievementList.add(achievement);
            } while (cursor.moveToNext());
        }
       
        // return achievements list
        return achievementList;
    }
	
	public List<Achievement> getAllAchievementsDone() {
        List<Achievement> achievementList = new ArrayList<Achievement>();
        // Select All Query
        String selectQuery = "SELECT * " +
    						" FROM " + ACHIEVEMENTS_TABLE + " WHERE " + ACH_DONE + " = 1"  + " ORDER BY " + IDACHIEVEMENT;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        GameActivity activity = ResourcesManager.getInstance().activity;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.setId(Integer.parseInt(cursor.getString(0)));
                achievement.setName(activity.getString(Integer.parseInt(cursor.getString(1))));
                achievement.setDescription(activity.getString(Integer.parseInt(cursor.getString(2))));
                achievement.setCheck(Integer.parseInt(cursor.getString(3)) == 1);
                achievement.setIdsession(Integer.parseInt(cursor.getString(4)));
                // Adding achievement to list
                achievementList.add(achievement);
            } while (cursor.moveToNext());
        }
       
        // return achievements list
        return achievementList;
    }
}