package edu.ohio_state.cse.nagger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Sayam Ganguly on 10/14/2016.
 */
public class DatabaseHelper {
    private static final String DATABASE_NAME = "Nagger.db";
    private static final int DATABASE_VERSION = 1;
    public static final String USER_TABLE = "User";
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;
    private static final String INSERT_USER = "insert into " + USER_TABLE + "(email, username) values (?,?)";
    private static String mTableName;

    public DatabaseHelper(Context context){
        this.context = context;
        NaggerOpenHelper openHelper = new NaggerOpenHelper(context);
        this.db = openHelper.getWritableDatabase();
    }

    public static void setTableNAme(String mtablename){
        mTableName = mtablename;
    }

    public long insert(String email, String username){
        this.insertStatement = this.db.compileStatement(INSERT_USER);
        this.insertStatement.bindString(1,email);
        this.insertStatement.bindString(2,username);
        return this.insertStatement.executeInsert();
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
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db , int oldVersion, int newVersion){
            switch (mTableName) {
                case USER_TABLE: {
                    db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
                    onCreate(db);
                }
            }
        }

    }
}

