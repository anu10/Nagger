package edu.ohio_state.cse.nagger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Time;
import java.util.Date;


public class DatabaseHelper {
    private static final String DATABASE_NAME = "nagger.db";
    private static final int DATABASE_VERSION = 1;
    public static final String USER_TABLE = "User";
    public static final String REMINDER_TABLE = "Reminder";
    public static final String NOTIFICATION_TABLE = "Notification";

    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;
    private static final String INSERT_USER = "insert into " + USER_TABLE + "(email, username) values (?,?)";
    private static final String INSERT_REMINDER = "insert into " + REMINDER_TABLE + "(reminderId, title, description, time, date) values (?,?,?,?,?)";
    private static final String INSERT_NOTIFICATION = "insert into" + NOTIFICATION_TABLE + "(reminderId, senderEmail, receiverEmail, Status) values (?,?,?,?)";

    private static String mTableName;

    public DatabaseHelper(Context context){
        this.context = context;
        NaggerOpenHelper openHelper = new NaggerOpenHelper(context);
        this.db = openHelper.getWritableDatabase();
    }

    public static void setTableNAme(String mtablename){
        mTableName = mtablename;
    }

    public long insertUser(String email, String username){
        this.insertStatement = this.db.compileStatement(INSERT_USER);
        this.insertStatement.bindString(1,email);
        this.insertStatement.bindString(2,username);
        return this.insertStatement.executeInsert();
    }

    public long insertReminder(String reminderId, String title, String description, Date date, Time time){
        this.insertStatement = this.db.compileStatement(INSERT_REMINDER);
        this.insertStatement.bindString(1,reminderId);
        this.insertStatement.bindString(2,title);
        this.insertStatement.bindString(3,description);
        this.insertStatement.bindString(4,date.toString());
        this.insertStatement.bindString(5,time.toString());
        return this.insertStatement.executeInsert();
    }

    public long insertNotification(String reminderId, String senderEmail, String receiverEmail, String status){
        this.insertStatement = this.db.compileStatement(INSERT_REMINDER);
        this.insertStatement.bindString(1,reminderId);
        this.insertStatement.bindString(2,senderEmail);
        this.insertStatement.bindString(3,receiverEmail);
        this.insertStatement.bindString(4,status);
        return this.insertStatement.executeInsert();
    }
    public Cursor selectAll()
    {
        Cursor cur = db.rawQuery("SELECT * FROM USER", null);
        return cur;
    }


    private static class NaggerOpenHelper extends SQLiteOpenHelper{

        NaggerOpenHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            switch (mTableName){
                case USER_TABLE:{
                    db.execSQL("CREATE TABLE " + USER_TABLE + "(email TEXT PRIMARY KEY, username TEXT)");
                    break;
                }
                case REMINDER_TABLE:{
                    db.execSQL("CREATE TABLE " + REMINDER_TABLE + "(reminderId TEXT PRIMARY KEY, title TEXT, description TEXT, date DATE, time TIME)");
                    break;
                }
                case NOTIFICATION_TABLE:{
                    db.execSQL("CREATE TABLE " + NOTIFICATION_TABLE + "(reminderId TEXT PRIMARY KEY, senderEmail TEXT, receiverEmail TEXT, status TEXT)");
                    break;
                }
            }
        }



        @Override
        public void onUpgrade(SQLiteDatabase db , int oldVersion, int newVersion){
            switch (mTableName) {
                case USER_TABLE: {
                    db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
                    onCreate(db);
                }
                case REMINDER_TABLE: {
                    db.execSQL("DROP TABLE IF EXISTS " + REMINDER_TABLE);
                    onCreate(db);
                }
                case NOTIFICATION_TABLE: {
                    db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
                    onCreate(db);
                }
            }
        }
    }
}
