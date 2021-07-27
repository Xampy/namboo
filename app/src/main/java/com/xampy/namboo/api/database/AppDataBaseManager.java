package com.xampy.namboo.api.database;

import android.content.Context;
import android.util.Log;

import com.xampy.namboo.api.dataModel.DataBaseUser;
import com.xampy.namboo.api.dataModel.DataBaseUser;

public class AppDataBaseManager {

    public  static UserDAO DB_USER_TABLE;

    private DataBaseHandler dataBaseHandler = null;


    public AppDataBaseManager(Context context) {

        this.dataBaseHandler =  new DataBaseHandler(context);
        this.DB_USER_TABLE = new UserDAO(this.dataBaseHandler);

    }

    /**
     * Add a default user to the database
     */
    public void initDatabaseContent() {
        this.DB_USER_TABLE.addUser(new DataBaseUser(1, "@none", "@none", "@none", "@none", "@none"));
        Log.i("USER DATABASE", "User created");
    }
}
