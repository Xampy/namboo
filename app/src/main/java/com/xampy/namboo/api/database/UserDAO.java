package com.xampy.namboo.api.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.xampy.namboo.api.dataModel.DataBaseUser;

import org.w3c.dom.CDATASection;

public class UserDAO  extends DAOBase{

    //[START table columns]
    //The user some information
    public static final String USER_KEY = "id";
    public static final String USER_UID = "uid";
    public static final String USER_USERNAME = "username";
    public static final String USER_CREDIT_AMOUNT = "amount";
    public static final String USER_PASSWORD = "password";
    public static final String USER_STATUS = "status";
    public static final String USER_ACCOUNT_ACTIVATED = "account_activated";
    public static final String USER_TEL = "tel";
    public static final String USER_LAST_TRANSACTION_REFERENCE = "last_tx_reference";
    //[END table columns]

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_TABLE_CREATE =
            "CREATE TABLE " +USER_TABLE_NAME + " (" +
                    USER_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_UID + " TEXT NOT NULL, " +
                    USER_ACCOUNT_ACTIVATED + " TEXT NOT NULL, " +
                    USER_USERNAME + " TEXT NOT NULL, " +
                    USER_PASSWORD + " TEXT NOT NULL, " +
                    USER_STATUS + " TEXT NOT NULL, " +
                    USER_LAST_TRANSACTION_REFERENCE + " TEXT NULL, " +
                    USER_CREDIT_AMOUNT + " TEXT NOT NULL, " +
                    USER_TEL + " TEXT NOT NULL);";


    public static final String USER_TABLE_DROP = "DROP TABLE IF EXISTS " +
            USER_TABLE_NAME + ";";

    public static boolean userAlreadyExists = false;

    /**
     * Constant user ID
     * We need only on user
     */
    private final int USER_ID = 1;

    public UserDAO(DataBaseHandler dataBaseHandler) {
        super(dataBaseHandler);
    }



    /**
     * Add a user to the dataBase
     * @param user
     */
    public void addUser(DataBaseUser user){

        if(this.userAlreadyExists == false){
            ContentValues user_values = new ContentValues();

            String username = user.getUsername();
            if(username.length() > 0){
                user_values.put(USER_USERNAME, user.getUsername());
                user_values.put(USER_PASSWORD, user.getPassword());
                user_values.put(USER_STATUS, user.getStatus());
                user_values.put(USER_TEL, user.getTel());
                user_values.put(USER_UID, user.getUid());
                user_values.put(USER_LAST_TRANSACTION_REFERENCE, user.getLast_transaction_reference());
                user_values.put(USER_CREDIT_AMOUNT, user.getCredit_amount());
                user_values.put(USER_ACCOUNT_ACTIVATED, user.isAccount_activated());

                //InsertData in the data base
                this.writableDataBase.insert(UserDAO.USER_TABLE_NAME, null, user_values);
            }

            //Then we have a user
            this.userAlreadyExists = true;

            Log.i("DATABASE USER TABLE", "Insert User Username = " + user.getUsername());
        }
    }

    /**
     * Update the user name in the data base
     * @param user
     */
    public void updateUserUserName(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        String username = user.getUsername();
        if(username.length() > 0){
            user_values.put(USER_USERNAME, username);


            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }

    }

    /**
     * Update the user name in the data base
     * @param username the new username
     */
    public void updateUserUserName(String username){

        ContentValues user_values = new ContentValues();

        if(username.length() > 0){
            user_values.put(USER_USERNAME, username);

            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }

    }

    /**
     * Update the user telephone number in the data base
     * @param user
     */
    public void updateUserUserTel(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        String userTel = user.getTel();
        if(userTel.length() > 0){
            user_values.put(USER_TEL, userTel);


            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }

    }

    /**
     * Updta the user telephone number
     * @param userTel
     */
    public void updateUserTel(String userTel){

        ContentValues user_values = new ContentValues();

        if(userTel.length() > 0){
            user_values.put(USER_TEL, userTel);

            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }
    }



    //#########################################################################################
    public void updateUserUserPassword(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        String userPass = user.getPassword();
        if(userPass.length() > 0){
            user_values.put(USER_PASSWORD, userPass);


            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }

    }


    //###########################################################################################
    public void updateUserStatus(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        String userStatus = user.getStatus();
        if(userStatus.length() > 0){
            user_values.put(USER_STATUS, userStatus);


            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }

    }

    //###########################################################################################
    public void updateUserLastTransaction(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        String userLast_transaction_reference = user.getLast_transaction_reference();
        //userLast_transaction_reference.length();
        //The user last transaction reference cans null
        user_values.put(USER_LAST_TRANSACTION_REFERENCE,  userLast_transaction_reference);


        this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                UserDAO.USER_KEY + "=" + USER_ID, null);

    }

    //###########################################################################################
    public void updateUserCreditAmount(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        int userCredit_amount = user.getCredit_amount();
        if( userCredit_amount> 0){
            user_values.put(USER_CREDIT_AMOUNT,  userCredit_amount);


            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }

    }

    //###########################################################################################
    public void updateUserAccountActivatedState(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        boolean userAccount_activated = user.isAccount_activated();
        user_values.put(USER_ACCOUNT_ACTIVATED,  userAccount_activated);


        this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                UserDAO.USER_KEY + "=" + USER_ID, null);

    }






    /**
     * Update the user telephone number in the data base
     * @param user
     */
    public void updateUserUid(DataBaseUser user){

        ContentValues user_values = new ContentValues();

        String userUid = user.getUid();
        if(userUid!=null && userUid.length() > 0){
            user_values.put(USER_UID, userUid);


            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }

    }

    /**
     * uPDATE THE USER FIRBASE UID
     * @param userUid
     */
    public void updateUserUid(String userUid){

        ContentValues user_values = new ContentValues();

        if(userUid!=null && userUid.length() > 0){
            user_values.put(USER_UID, userUid);


            this.writableDataBase.update(UserDAO.USER_TABLE_NAME, user_values,
                    UserDAO.USER_KEY + "=" + USER_ID, null);
        }
    }


    /**
     * Get the user data form database
     * @return
     */
    public DataBaseUser getUser(){
        DataBaseUser user = null;

        Cursor cursor = this.redableDataBase.rawQuery(
                "SELECT * FROM " + UserDAO.USER_TABLE_NAME, null);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            user =  new DataBaseUser(cursor.getLong(cursor.getColumnIndex(UserDAO.USER_KEY)),
                    cursor.getString(cursor.getColumnIndex(UserDAO.USER_UID)),
                    cursor.getString(cursor.getColumnIndex(UserDAO.USER_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(UserDAO.USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(UserDAO.USER_STATUS)),
                    cursor.getString(cursor.getColumnIndex(UserDAO.USER_TEL)),
                    cursor.getString(cursor.getColumnIndex(UserDAO.USER_LAST_TRANSACTION_REFERENCE)),
                    Integer.parseInt(
                            cursor.getString(cursor.getColumnIndex(UserDAO.USER_CREDIT_AMOUNT))
                    ),
                    Integer.parseInt(
                            cursor.getString(cursor.getColumnIndex(UserDAO.USER_ACCOUNT_ACTIVATED))
                    ) == 1
            );
        }
        cursor.close();

        return user;
    }
}
