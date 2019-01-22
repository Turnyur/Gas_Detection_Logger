package cm.turnyur.gasdatalogger.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

	
	private static final String DATABASE_NAME =
			"microcontroller.db" ;
			private static final int DATABASE_VERSION = 1;
			public static final String TABLE_NAME =
			"gases" ;
			public static final String COLUMN_ID = "empId" ;
			public static final String COLUMN_TEMP=
			"temperature" ;
			public static final String COLUMN_GASINTEN =
			"gasIntensity" ;
			public static final String COLUMN_TIME =
			"rec_time" ;
			public static final String COLUMN_DATE=
			"rec_date" ;
			
			private static final String TABLE_CREATE =
					"CREATE TABLE " + TABLE_NAME + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_TEMP + " TEXT, " +
					COLUMN_GASINTEN + " TEXT, " +
					COLUMN_TIME+ " TEXT, " +
					COLUMN_DATE + " TEXT " +
				
					")" ;
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int
			oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL( "DROP TABLE IF EXISTS "+TABLE_NAME);
		db.execSQL(TABLE_CREATE);
	}

}
