package cm.turnyur.gasdatalogger.DB;

import java.util.ArrayList;
import java.util.List;

import com.turnyur.gasdatalogger.MODEL.ObjectGas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ObjectGasOperations {

	public static final String LOGTAG = "EMP_MNGMNT_SYS";
	SQLiteOpenHelper dbhandler;
	SQLiteDatabase database;
	public static int rowCount=0;
	private static final String[] allColumns = { DatabaseHandler.COLUMN_ID,
			DatabaseHandler.COLUMN_GASINTEN, DatabaseHandler.COLUMN_TEMP,
			DatabaseHandler.COLUMN_TIME, DatabaseHandler.COLUMN_DATE };

	public ObjectGasOperations(Context context) {
		dbhandler = new DatabaseHandler(context);
	}

	public void open() {
		Log.i(LOGTAG, "Database Opened");
		database = dbhandler.getWritableDatabase();
	}

	
	public void openRead() {
		Log.i(LOGTAG, "Read Read Database Opened");
		database = dbhandler.getReadableDatabase();
	}
	
	public void close() {
		Log.i(LOGTAG, "Database Closed");
		dbhandler.close();
	}

	public ObjectGas addGasObjectToDB(ObjectGas mObjectGas) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.COLUMN_GASINTEN,
				mObjectGas.getGasIntensity());
		values.put(DatabaseHandler.COLUMN_TEMP, mObjectGas.getTemperature());
		values.put(DatabaseHandler.COLUMN_TIME, mObjectGas.getRec_time());
		values.put(DatabaseHandler.COLUMN_DATE, mObjectGas.getRec_date());
		long insertid = database.insert(DatabaseHandler.TABLE_NAME, null,
				values);
		Log.i(LOGTAG, "addGasObjectToDB");
		// This part of code still suspicious
		mObjectGas.setObect_ID(insertid);
		return mObjectGas;
	}
//Added recently to correct bugs in StoredData.java
	public int getGasDataStoredRowCount(){
		int rowCount;
		Cursor cursor = database.query(DatabaseHandler.TABLE_NAME, allColumns,
				null, null, null, null, null);
		rowCount=cursor.getCount();
		return rowCount;
	}
	public List<ObjectGas> getAllGasObjectsFromDB() {
		Cursor cursor = database.query(DatabaseHandler.TABLE_NAME, allColumns,
				null, null, null, null, null);
		
		List<ObjectGas> myGasData_objects = new ArrayList<ObjectGas>();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ObjectGas e = new ObjectGas();
				e.setObect_ID(cursor.getLong(cursor
						.getColumnIndex(DatabaseHandler.COLUMN_ID)));
				e.setGasIntensity(cursor.getString(cursor
						.getColumnIndex(DatabaseHandler.COLUMN_GASINTEN)));
				e.setTemperature(cursor.getString(cursor
						.getColumnIndex(DatabaseHandler.COLUMN_TEMP)));
				e.setRec_time(cursor.getString(cursor
						.getColumnIndex(DatabaseHandler.COLUMN_TIME)));
				e.setRec_date(cursor.getString(cursor
						.getColumnIndex(DatabaseHandler.COLUMN_DATE)));

				myGasData_objects.add(e);
			}
		}
		return myGasData_objects;
	}

	public ObjectGas getGasData(long id) {
		
		
		Cursor cursor = database.query(DatabaseHandler.TABLE_NAME, allColumns,
				DatabaseHandler.COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
	//	rowCount = cursor.getCount();
		if (cursor != null)
			cursor.moveToFirst();
		ObjectGas e = new ObjectGas(Long.parseLong(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4));
		Log.i(LOGTAG, "getGasData");
		// return Employee
		return e;
	}

	public int checkDB() {
		String sql = "SELECT * FROM gases";
		Cursor cursor = database.rawQuery(sql, null);
		//rowCount=cursor.getCount();
		return 0; // rowCount;
	}  
}
