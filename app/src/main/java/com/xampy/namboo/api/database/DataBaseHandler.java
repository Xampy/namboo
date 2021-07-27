package com.xampy.namboo.api.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler  extends SQLiteOpenHelper {

    protected final static String DATA_BASE_NAME = "namboo.db";
    protected final static int DATA_BASE_VERSION = 1;
    private Context context;


    public DataBaseHandler(Context context) {
        super(context.getApplicationContext(), DATA_BASE_NAME, null, DATA_BASE_VERSION);
        this.context = context;
    }
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDAO.USER_TABLE_CREATE);
        Log.i("DATABASE", "On create invoked");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( UserDAO.USER_TABLE_DROP );
        this.onCreate(db);
        Log.i( "DATABASE", "onUpgrade invoked" );
    }
}
