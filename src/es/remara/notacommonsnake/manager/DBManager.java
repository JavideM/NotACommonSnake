package es.remara.notacommonsnake.manager;

import java.util.ArrayList;
import java.util.List;

import es.remara.notacommonsnake.model.Achievement;
import es.remara.notacommonsnake.model.Profile;
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
	
	/*
	 * Profiles
	 */
	// Table name
    private final String PROFILES_TABLE = "PROFILES";

    // Columns
    private final String IDPROFILE = "IDPROFILE";
    private final String PROF_NAME = "NAME";
    private final String PROF_ACTIVE = "ACTIVE";

    // Script
    private String profilesCreate = "CREATE TABLE " + PROFILES_TABLE + "("
            										+ IDPROFILE + "INTEGER primary key, " 
        											+ PROF_NAME + " TEXT NOT NULL, "
    												+ PROF_ACTIVE + " INTEGER NOT NULL)";
	
	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sessionsCreate);
		db.execSQL(achievementsCreate);
		db.execSQL(profilesCreate);
		insertAchievements(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST " + SESSIONS_TABLE);
		db.execSQL("DROP TABLE IF EXIST " + ACHIEVEMENTS_TABLE);
		db.execSQL("DROP TABLE IF EXIST " + PROFILES_TABLE);
		
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
	 * Profiles CRUD
	 */
	public void saveProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROF_NAME, profile.getName());
        values.put(PROF_ACTIVE, profile.isActive()?1:0);

        db.insert(PROFILES_TABLE, null, values);
        db.close();
    }
	
	public boolean isThereAnActiveProfile()
	{
        // Select Active Profile Query
        String selectQuery = "SELECT * " +
    						" FROM " + PROFILES_TABLE + " WHERE " +PROF_ACTIVE+ "=1 DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor.getCount() == 1;
	}
	
	public void setActiveProfile(Profile profile)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		//Update condition
		String cond = IDPROFILE + " = " + profile.getIdprofile(); 
		
		ContentValues values = new ContentValues();
		values.put(PROF_ACTIVE, 1);
		
		db.update(PROFILES_TABLE, values, cond, null);
		
		db.close();
	}
	
	public List<Profile> getAllProfiles()
	{
		List<Profile> profiles = new ArrayList<Profile>();
		// Select All query
		String selectQuery = "SELECT * " +
								"FROM " + PROFILES_TABLE + " ORDER BY " + PROF_NAME;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//looping through all rows and adding to the list
		if(cursor.moveToFirst())
		{
			do{
				Profile profile = new Profile();
				profile.setIdprofile(Integer.parseInt(cursor.getString(0)));
				profile.setName(cursor.getString(1));
				profile.setActive(cursor.getString(2).equals("1"));
				profiles.add(profile);
			}while(cursor.moveToNext());
		}
		
		return profiles;
		
	}
	
	/*
	 * Achievements CRUD
	 */
	
	private void insertAchievements(SQLiteDatabase db) {
		//First points
		ContentValues values = new ContentValues();
		values.put(ACH_NAME, "First meal");
		values.put(ACH_DESCRIP, "Eat your first food.");
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
		values = new ContentValues();
		values.put(ACH_NAME, "300 AU AU");
		values.put(ACH_DESCRIP, "Score 300 points.");
		values.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values);
		
		db.execSQL("CREATE TRIGGER ach1_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_SCORE + " >= 300 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 2, "+ ACH_IDSESSION + "=new." +IDSESSION
					+" WHERE "+ IDACHIEVEMENT +" = 1 AND "+ ACH_DONE+ "=0;" +
				"END;");
		
		// Over 9000 points
		values = new ContentValues();
		values.put(ACH_NAME, "It's over 9000!");
		values.put(ACH_DESCRIP, "Score more than 9000 points.");
		values.put(ACH_DONE, "0");
		
		db.insert(ACHIEVEMENTS_TABLE, null, values);
		
		db.execSQL("CREATE TRIGGER ach1_trigger AFTER" +
				" INSERT ON " + SESSIONS_TABLE + " WHEN new."+ SESSION_SCORE + " > 9000 " +
				" BEGIN " +
					"UPDATE " + ACHIEVEMENTS_TABLE +
					" 	SET " + ACH_DONE + " = 3, "+ ACH_IDSESSION + "=new." +IDSESSION
					+" WHERE "+ IDACHIEVEMENT +" = 1 AND "+ ACH_DONE+ "=0;" +
				"END;");
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
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.setId(Integer.parseInt(cursor.getString(0)));
                achievement.setName(cursor.getString(1));
                achievement.setDescription(cursor.getString(2));
                achievement.setCheck(Integer.parseInt(cursor.getString(3)) == 1);
//                achievement.setIdsession(Integer.parseInt(cursor.getString(4)));
                // Adding achievement to list
                achievementList.add(achievement);
            } while (cursor.moveToNext());
        }
       
        // return achievements list
        return achievementList;
    }
}