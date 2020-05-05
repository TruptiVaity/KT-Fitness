package com.example.ktfit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlanProvider extends ContentProvider {
    private PlanDbHelper mDbHelper;
    /** Tag for the log messages */
    public static final String LOG_TAG = PlanProvider.class.getSimpleName();
    private static final int PLAN = 100;
    private static final int PLAN_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PlanContract.CONTENT_AUTHORITY,PlanContract.PATH_PLAN,PLAN);
        sUriMatcher.addURI(PlanContract.CONTENT_AUTHORITY,PlanContract.PATH_PLAN +"/#",PLAN_ID);
    }
    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PlanDbHelper object to gain access to the plan database.
        mDbHelper = new PlanDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAN:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on pets table
                cursor = database.query(PlanContract.PlanEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PLAN_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PlanContract.PlanEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PlanContract.PlanEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        /**
         * Updates the data at the given selection and selection arguments, with the new ContentValues.
         */
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAN:
                return inserPlan(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri inserPlan(Uri uri, ContentValues values){
        //Check that the name is not null
        String name = values.getAsString(PlanContract.PlanEntry.COLUMN_DAY);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        // TODO: Finish sanity checking the rest of the attributes in ContentValues
        Integer time = values.getAsInteger((PlanContract.PlanEntry.COLUMN_START_TIME_HOUR));
        if(time == null ){
            throw new IllegalArgumentException("Pet requires a valid gender");
        }
        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer repeat = values.getAsInteger(PlanContract.PlanEntry.COLUMN_REPEAT);
        if (repeat != null) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }
        // TODO: Insert a new pet into the pets database table with the given ContentValues
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Once we know the ID of the new row in the table,
        long id = database.insert(PlanContract.PlanEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAN:
                // Delete all rows that match the selection and selection args
                return database.delete(PlanContract.PlanEntry.TABLE_NAME, selection, selectionArgs);
            case PLAN_ID:
                // Delete a single row given by the ID in the URI
                selection = PlanContract.PlanEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(PlanContract.PlanEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAN:
                return updatePlan(uri, contentValues, selection, selectionArgs);
            case PLAN_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PlanContract.PlanEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePlan(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePlan(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // TODO: Update the selected pets in the pets database table with the given ContentValues

        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PlanContract.PlanEntry.COLUMN_DAY)) {
            String name = values.getAsString(PlanContract.PlanEntry.COLUMN_DAY);
            if (name == null) {
                throw new IllegalArgumentException("Please select the day");
            }
        }
        if (values.containsKey(PlanContract.PlanEntry.COLUMN_MONTH)) {
            String name = values.getAsString(PlanContract.PlanEntry.COLUMN_MONTH);
            if (name == null) {
                throw new IllegalArgumentException("Please select the day");
            }
        }
        if (values.containsKey(PlanContract.PlanEntry.COLUMN_YEAR)) {
            String name = values.getAsString(PlanContract.PlanEntry.COLUMN_YEAR);
            if (name == null) {
                throw new IllegalArgumentException("Please select the day");
            }
        }


        // If the {@link Plan Entry start time hour} key is present,
        // check that the gender value is valid.
        if (values.containsKey(PlanContract.PlanEntry.COLUMN_START_TIME_HOUR)) {
            Integer time = values.getAsInteger(PlanContract.PlanEntry.COLUMN_START_TIME_HOUR);
            if (time == null ) {
                throw new IllegalArgumentException("Please enter valid time");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(PlanContract.PlanEntry.COLUMN_REPEAT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer repeat = values.getAsInteger(PlanContract.PlanEntry.COLUMN_REPEAT);
            if (repeat != null) {
                throw new IllegalArgumentException("Please update the repeat value");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(PlanContract.PlanEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
