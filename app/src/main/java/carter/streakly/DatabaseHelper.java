package carter.streakly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carter Klein on 8/4/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "streaks.db"; // Name of DB
    public static final String TABLE_NAME = "streak_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "STREAKNAME";
    public static final String COL_3 = "STREAKCATEGORY";
    public static final String COL_4 = "DATESTARTED";
    public static final String COL_5 = "DAYSKEPT";
    public static final String COL_6 = "STARTTIME";
    public static final String COL_7 = "ISGOING";
    public static final String COL_8 = "CHECKEDTIME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,STREAKNAME TEXT,STREAKCATEGORY TEXT,DATESTARTED TEXT,DAYSKEPT INTEGER, STARTTIME LONG, ISGOING INTEGER, CHECKEDTIME LONG);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String STREAKNAME, String STREAKCATEGORY, int DAYSKEPT, long STARTTIME, int ISGOING, long CHECKEDTIME){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, STREAKNAME);
        contentValues.put(COL_3, STREAKCATEGORY);
        //contentValues.put(COL_4, DATESTARTED);
        contentValues.put(COL_5, DAYSKEPT);
        contentValues.put(COL_6, STARTTIME);
        contentValues.put(COL_7, ISGOING);
        contentValues.put(COL_8, CHECKEDTIME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String whichName, String streakName, String streakCategory, int daysKept, long startTime, int isGoing, long checkedTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, streakName);
        contentValues.put(COL_3, streakCategory);
        contentValues.put(COL_5, daysKept);
        contentValues.put(COL_6, startTime);
        contentValues.put(COL_7, isGoing);
        contentValues.put(COL_8, checkedTime);
        db.update(TABLE_NAME, contentValues, "STREAKNAME = ?", new String[] {whichName});
        return true;
    }

    public boolean updateDataSimple(String whichName, String streakName, String streakCategory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, streakName);
        contentValues.put(COL_3, streakCategory);
        db.update(TABLE_NAME, contentValues, "STREAKNAME = ?", new String[] {whichName});
        return true;
    }

    public boolean updateDataCheckedTime(String whichName, long checkedTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8, checkedTime);
        db.update(TABLE_NAME, contentValues, "STREAKNAME = ?", new String[] {whichName});
        return true;
    }

    public Integer deleteData(String streakName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "STREAKNAME = ?", new String[] {streakName});
    }
}
