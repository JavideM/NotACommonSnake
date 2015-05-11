package es.remara.notacommonsnake.manager;

import es.remara.notacommonsnake.model.Session;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

	 // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
	
	/*
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
	
	
	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sessionsCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
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
}
