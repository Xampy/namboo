package com.xampy.namboo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.DataBaseUser;
import com.xampy.namboo.api.database.AppDataBaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.xampy.namboo.api.dataModel.DataBaseUser;
import com.xampy.namboo.api.database.AppDataBaseManager;

import java.util.concurrent.TimeUnit;


public class NambooPostActivity extends AppCompatActivity {

    private static final long DELAY_TO_APP = 2000;
    private long startTime;
    private DataBaseUser currentUser;
    private boolean mUserLogged;
    private FirebaseAuth mAuth;

    private static FirebaseUser ResultedUser;
    private boolean mContinueTo;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namboo_post);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        mContinueTo = false;

        final long startTime = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(System.currentTimeMillis() - startTime < 2000);

                openMainActivity();
            }
        }).start();




    }

    private void openMainActivity() {

        //[START get instance of firebase auth
        //FirebaseAuth auth = FirebaseAuth.getInstance();
        //[END get instance of firebase auth

        //[START database checking]
        AppDataBaseManager appDataBaseManager = new AppDataBaseManager(getApplicationContext());
        currentUser = AppDataBaseManager.DB_USER_TABLE.getUser();        //Start getting the user
        mUserLogged = false;

        //Check if user is null
        //and if, we init the database with data
        if (currentUser == null) {
            appDataBaseManager.initDatabaseContent();  //Construct a new default user
            currentUser = AppDataBaseManager.DB_USER_TABLE.getUser();
            Log.i("USER DATABASE", currentUser.getUsername());
        } else {
            //Avoid default user values
            if (currentUser.getTel().startsWith("+") && !currentUser.getUid().equals("@none")) {
                mUserLogged = true;

            } else {
                mUserLogged = false;
                mContinueTo = true; //Allow access
            }
        }

        Intent home_activity = new Intent(NambooPostActivity.this, MainActivity.class);
        home_activity.putExtra("USER_LOGIN_STATE", mUserLogged);
        startActivity(home_activity);

        //Finish this fullscreen activity
        finish();
        //[END database checking]
    }
}
