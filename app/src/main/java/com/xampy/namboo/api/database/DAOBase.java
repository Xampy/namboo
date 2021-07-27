package com.xampy.namboo.api.database;

import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
    protected DataBaseHandler dbHandler = null;
    protected SQLiteDatabase redableDataBase = null;
    protected SQLiteDatabase writableDataBase = null;

    public DAOBase(DataBaseHandler dbHandler){
        this.dbHandler= dbHandler;

        this.redableDataBase = this.dbHandler.getReadableDatabase();
        this.writableDataBase = this.dbHandler.getWritableDatabase();

    }
}
