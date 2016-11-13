package edu.ohio_state.cse.nagger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Time;
import java.util.Date;

public class DatabaseHelper {
    public static final String All_TABLE = "All";
    private static final String DATABASE_NAME = "nagger.db";
    private static final int DATABASE_VERSION = 1;
    private static final String INSERT_REMINDER = "insert into Reminder(sender, description, date, time) values (?,?,?,?)";
    private static final String INSERT_USER = "insert into User(email, username) values (?,?)";
    public static final String NOTIFICATION_TABLE = "Notification";
    public static final String REMINDER_TABLE = "Reminder";
    public static final String USER_TABLE = "User";
    private static String mTableName;
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;

    public DatabaseHelper(Context context) {
        this.context = context;
        this.db = new NaggerOpenHelper(this.context).getWritableDatabase();
    }

    public static void setTableName(String mtablename) {
        mTableName = mtablename;
    }

    public long insertUser(String email, String username) {
        this.insertStatement = this.db.compileStatement(INSERT_USER);
        this.insertStatement.bindString(1, email);
        this.insertStatement.bindString(2, username);
        return this.insertStatement.executeInsert();
    }

    public long insertReminder(Reminder reminder) {
        this.insertStatement = this.db.compileStatement(INSERT_REMINDER);
        this.insertStatement.bindString(1, reminder.getSender());
        this.insertStatement.bindString(2, reminder.getReminderDesc());
        this.insertStatement.bindString(3, reminder.getDate().toString());
        this.insertStatement.bindString(4, reminder.getTime().toString());
        return this.insertStatement.executeInsert();
    }

    public Cursor selectAll(String tableName) {
        Cursor cur = db.rawQuery("SELECT * FROM " + tableName, null);
        return cur;
    }

    public boolean deleteReminder(Reminder reminder){
        return db.delete(REMINDER_TABLE,
                "reminderId = " + String.valueOf(reminder.getReminderID()),null) > 0;
    }

    public void deleteUser(User user){
//        return db.delete(USER_TABLE,"username = " + user.getUserName().toString(),null) > 0;
        db.execSQL("delete from " + USER_TABLE);
    }

    private static class NaggerOpenHelper extends SQLiteOpenHelper {
        NaggerOpenHelper(Context context) {
            super(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            mTableName = DatabaseHelper.mTableName;
            switch (mTableName) {
                case USER_TABLE:
                    db.execSQL("CREATE TABLE User(email VARCHAR(50) PRIMARY KEY, username VARCHAR(50))");
                    return;
                case REMINDER_TABLE:
                    db.execSQL("CREATE TABLE Reminder(reminderId INTEGER PRIMARY KEY AUTOINCREMENT, sender VARCHAR(50), description TEXT, date TEXT, time TEXT)");
                    return;
                case NOTIFICATION_TABLE:
                    db.execSQL("CREATE TABLE Notification(reminderId TEXT PRIMARY KEY, senderEmail TEXT, receiverEmail TEXT, status TEXT)");
                    return;
                case All_TABLE:
                    db.execSQL("CREATE TABLE User(email VARCHAR(50) PRIMARY KEY, username VARCHAR(50))");
                    db.execSQL("CREATE TABLE Reminder(reminderId INTEGER PRIMARY KEY AUTOINCREMENT, sender VARCHAR(50), description TEXT, date DATE, time TIME)");
                    return;
                default:
                    return;
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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