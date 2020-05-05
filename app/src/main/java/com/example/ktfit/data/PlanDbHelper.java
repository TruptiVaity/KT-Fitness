package com.example.ktfit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static java.sql.Types.INTEGER;
import static java.sql.Types.NULL;

public class PlanDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = PlanDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "plan.db";

    private static final int DATABASE_VERSION = 2;

    public PlanDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the Plans table
        String SQL_CREATE_PLAN_TABLE = new StringBuilder().append("CREATE TABLE ").append(PlanContract.PlanEntry.TABLE_NAME)
                .append("(").append(PlanContract.PlanEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(PlanContract.PlanEntry.COLUMN_DAY).append(" INTEGER NOT NULL, ")
                .append(PlanContract.PlanEntry.COLUMN_MONTH).append(" INTEGER NOT NULL, ")
                .append(PlanContract.PlanEntry.COLUMN_YEAR).append(" INTEGER NOT NULL, ")
                .append(PlanContract.PlanEntry.COLUMN_START_TIME_HOUR).append(" INTEGER NOT NULL, ")
                .append(PlanContract.PlanEntry.COLUMN_START_TIME_MINUTE).append(" INTEGER NOT NULL, ")
                .append(PlanContract.PlanEntry.COLUMN_END_TIME_HOUR).append(" INTEGER NOT NULL, ")
                .append(PlanContract.PlanEntry.COLUMN_END_TIME_MINUTE).append(" INTEGER NOT NULL, ")
                .append(PlanContract.PlanEntry.COLUMN_FRIEND).append(" TEXT, ")
                .append(PlanContract.PlanEntry.COLUMN_REPEAT).append(" INTEGER NOT NULL DEFAULT 0);").toString();
        db.execSQL(SQL_CREATE_PLAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
