package com.example.moody.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.moody.metadata.Metadata;

public class SQLService implements PersistanceService {

    public boolean persistMetadata(Metadata metadata) {
        return true;
    }

    public static class TableEntry implements BaseColumns {
        public static final String TABLE_NAME = "metadata";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_NUMBER_OF_CHARACTERS = "char_count";
        public static final String COLUMN_NAME_TOTAL_TIME = "total_time";
        public static final String COLUMN_NAME_NUMBER_OF_SPECIAL_CHARACTERS = "special_char_count";
        public static final String COLUMN_NAME_NUMBER_OF_ERROR = "error_count";
        public static final String COLUMN_NAME_BATCH_ID = "batch_id";
    }

    public static class TableMood implements BaseColumns{
        public static final String TABLE_NAME = "inputdata";
        public static final String COLUMN_NAME_BATCH_ID = "batch_id";
        public static final String COLUMN_NAME_INPUT_TEXT = "input_text";
        public static final String COLUMN_NAME_MOOD = "mood";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TableEntry.TABLE_NAME + " (" +
                    TableEntry._ID + " INTEGER PRIMARY KEY," +
                    TableEntry.COLUMN_NAME_TIMESTAMP + " TIMESTAMP," +
                    TableEntry.COLUMN_NAME_NUMBER_OF_CHARACTERS + " INTEGER," +
                    TableEntry.COLUMN_NAME_TOTAL_TIME + " BIGINT," +
                    TableEntry.COLUMN_NAME_NUMBER_OF_SPECIAL_CHARACTERS + " INTEGER," +
                    TableEntry.COLUMN_NAME_BATCH_ID + " VARCHAR," +
                    TableEntry.COLUMN_NAME_NUMBER_OF_ERROR + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TableEntry.TABLE_NAME;

    private static final String SQL_CREATE_TABLEMOOD=
            "CREATE TABLE "+TableMood.TABLE_NAME+"("+
            TableMood.COLUMN_NAME_BATCH_ID+" varchar not NULL "+
            "constraint TABLEMOOD_pk primary key,"+
            TableMood.COLUMN_NAME_INPUT_TEXT +" varchar,"+
            TableMood.COLUMN_NAME_MOOD +" int not null)";

    private static final String SQL_DELETE_MOOD =
            "DROP TABLE IF EXISTS " + TableMood.TABLE_NAME;


    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public class MoodDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public MoodDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLEMOOD);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_MOOD);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}
