package com.xampy.namboo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.xampy.namboo.api.dataModel.DataBaseUser;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.xampy.namboo.api.database.AppDataBaseManager;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooUserHelper;
import com.xampy.namboo.api.payement.PayGatePaymentAPI;
import com.xampy.namboo.api.payement.VolleyRequestQueueSingleton;
import com.xampy.namboo.ui.login.LoginFragment;
import com.xampy.namboo.ui.login.LoginWithInfoFragment;
import com.xampy.namboo.ui.login.RegisterFragment;
import com.xampy.namboo.ui.manyRoomData.ManyRoomFragment;
import com.xampy.namboo.ui.oneRoomPresentation.OneRoomFragment;
import com.xampy.namboo.ui.oneRoomPresentation.OneRoomImagePhotoViewFragment;
import com.xampy.namboo.ui.oneRoomPresentation.oneRoomData.RoomDataImagesDummyContent;
import com.xampy.namboo.ui.posts.MakePostFragment;
import com.xampy.namboo.ui.profile.ProfileFragment;
import com.xampy.namboo.ui.search.searchData.SearchDataModelRoom;
import com.xampy.namboo.ui.search.searchData.SearchDataModelService;
import com.xampy.namboo.ui.search.searchFragment.SearchFragment;
import com.xampy.namboo.ui.services.ServicesFragment;

import java.io.Serializable;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements
        OneRoomFragment.OnHomeOneRoomFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        RegisterFragment.OnRegisterInteractionListener,
        ProfileFragment.ProfileFragmentInteractionListener,
        EasyPermissions.PermissionCallbacks {


    public final static String PAYMENT_URL = "https://paygateglobal.com/api/v1/pay";
    public final static String PAYMENT_PAYGATE_TOGO_KEY = "98a3a78f-7847-4ed5-941a-501ba5d81a6c";

    private FrameLayout mAllFragmentFrameLayout;
    private View mNavHost;
    private View mNavView;
    private LoginFragment mLoginFragment;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //Authentication
    public static boolean mUserAlreadyLogged = false;
    public static FirebaseUser mAuthFirebaseUser;
    public static DataBaseUser mCurrentUser;
    private AppDataBaseManager mAppDataBaseManager;
    public static String[] mPositionLonLat;


    //[START PAYMENT TRANSACTION VARIABLES]
    //Already init a transaction but not checked yet
    //By default ther is no transaction
    public static boolean mTransaction_Wait_for_Checking = false;
    public  VolleyRequestQueueSingleton mVolleyRequest;
    public static PayGatePaymentAPI mPayGateAPI;
    private int[] mCarouselImages;

    //[END PAYMENT TRANSACTION VARIABLES]

    @Override
    protected void onStart() {
        super.onStart();

        //Connect to firebase
        if (mUserAlreadyLogged) {
            //FirebaseAuth.getInstance().signInWith
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mUserAlreadyLogged = getIntent().getBooleanExtra("USER_LOGIN_STATE", false);
        if (mUserAlreadyLogged) {
            //Get the firebase user
            //mAuthFirebaseUser = NambooPostActivity.getResultedUser();
        }




        //[START database checking]
        this.mAppDataBaseManager = new AppDataBaseManager(this.getApplicationContext());
        mCurrentUser = mAppDataBaseManager.DB_USER_TABLE.getUser();

        //[START update user credit amount ]
        FirestoreNambooUserHelper.getUser(mCurrentUser.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserNambooFirestore user = documentSnapshot.toObject(UserNambooFirestore.class);
                //Update view in the profile fragment
                if(null != user ){
                    mCurrentUser.setCredit_amount(
                            user.getAccountAmount()
                    );
                }
            }
        });
        //[END update user credit amount ]

        //We are sure that the current user data are not null
        //At least we have the default value
        //[END database checking]

        //[START PAYMENT INITIALIZATION]
        mVolleyRequest = VolleyRequestQueueSingleton.getInstance(getApplicationContext());
        mPayGateAPI = PayGatePaymentAPI.getInstance(mVolleyRequest);
        //[END ]


        mAllFragmentFrameLayout = (FrameLayout) findViewById(R.id.main_all_fragment_frame);
        mNavHost = (View) findViewById(R.id.nav_host_fragment);
        mNavView = (View) findViewById(R.id.nav_view);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_make_post,
                R.id.navigation_services,
                R.id.navigation_credit, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Fix the supported toolbar
        //this.toolbar = findViewById(R.id.activity_main_toolbar);
        //setSupportActionBar(toolbar);

        //CConfigure the navigation drawer
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        this.navigationView.setCheckedItem(R.id.activity_main_drawer_home);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().hide();


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //Toast.makeText(this, "User not already got ", Toast.LENGTH_SHORT).show();
        } else{
            //Toast.makeText(this, "User already got ", Toast.LENGTH_SHORT).show();
        }






        //[START Carousel view here]
        //END
    };

    //########################################################################################
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.activity_main_drawer_search) {
            //OPen the search fragment
            openSearchFragment();
        } else if (item.getItemId() == R.id.activity_main_drawer_services) {
            //OPen the search fragment
            openServicesFragment();
        } else if (item.getItemId() == R.id.activity_main_drawer_sell) {
            //OPen the search fragment
            openManyRoomDataFragment("Vente(s)");
        } else if (item.getItemId() == R.id.activity_main_drawer_rent) {

            //OPen the search fragment
            openManyRoomDataFragment("Location(s)");

        }else if (item.getItemId() == R.id.activity_main_drawer_share) {

            this.shareAppWithOthers();

        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * OPen the make post fragment
     */
    public void openMakePostFragment() {

        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mNavView.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);

        MakePostFragment makePostFragment = MakePostFragment.newInstance(
                "",
                "",
                mUserAlreadyLogged);   //The user is authenticated or not
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //getSupportFragmentManager().popBackStack();
        ft.replace(R.id.main_all_fragment_frame, makePostFragment, "MAKE_P  OST").commit();
    }

    /**
     * Personalized search
     */
    public void openSearchFragment() {

        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mNavView.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);

        SearchFragment searchFragment = SearchFragment.newInstance("", "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //getSupportFragmentManager().popBackStack();
        ft.add(R.id.main_all_fragment_frame, searchFragment, "ALL_SEARCH").commit();
    }

    /**
     * OPen information on a single room
     */
    public void openOneRoomDataFragment() {
        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mNavView.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);

        OneRoomFragment searchFragment = OneRoomFragment.newInstance(null, "", "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //getSupportFragmentManager().popBackStack();
        ft.replace(R.id.main_all_fragment_frame, searchFragment, "ALL_ONE_ROOM").commit();
    }

    public void callPhone(String call_number) {
        //Needed Permission list
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + call_number));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        } else {
            EasyPermissions.requestPermissions(this, "Nous avons besoin de votre permission pour autoriser les appels",
                    123, perms);
        }
    }

    public void openGoogleMapFromCoords(String coords){
        //2 as maximum result from splitting the coords
        //Longitude and latitude
        String[] c = coords.split(";", 2);
        Uri map_uri = Uri.parse(
                "geo:" + c[1] + "," + c[0]);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW,  map_uri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {

            startActivity(mapIntent);
        }
        else {
            Toast.makeText(this, "Pas d'application trouvé", Toast.LENGTH_SHORT).show();
        }
    }


    public void shareAppWithOthers(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }



    public void openWhatsappFromNumber(String phone_number){
        //Open whatsapp intent for handling messages

        Intent whatsappIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("smsto:" + phone_number ));
        whatsappIntent.setPackage("com.whatsapp");

        if ( whatsappIntent.resolveActivity(getPackageManager()) != null) {

            startActivity( whatsappIntent);
        }
        else {
            Toast.makeText(this, "Whatsapp n'est pas intallée", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Open information on post
     * But before the permission of phone call is granted
     * @param post a post information as object
     */
    //@AfterPermissionGranted(123)
    public void openOneRoomDataFragment(PostNambooFirestore post) {
        //Needed Permission list
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {

            //Make visible the frameLayout
            mNavHost.setVisibility(View.GONE);
            mNavView.setVisibility(View.GONE);
            mAllFragmentFrameLayout.setVisibility(View.VISIBLE);

            OneRoomFragment searchFragment = OneRoomFragment.newInstance((Serializable) post, "", "");
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.addToBackStack(null);
            //getSupportFragmentManager().popBackStack();
            ft.replace(R.id.main_all_fragment_frame, searchFragment, "ALL_ONE_ROOM").commit();
        } else {
            EasyPermissions.requestPermissions(this, "Nous avons besoin de votre permission pour autoriser les appels",
                    123, perms);
        }
    }

    /**
     * Open many room <Location> fragment
     */
    public void openManyRoomDataFragment(String fragment_title) {

        if (fragment_title == "Services") {
            openServicesFragment();
        } else {
            //Make visible the frameLayout
            mNavHost.setVisibility(View.GONE);
            mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
            mNavView.setVisibility(View.GONE);

            ManyRoomFragment searchFragment = ManyRoomFragment.newInstance(fragment_title, "");
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.addToBackStack(null);
            //getSupportFragmentManager().popBackStack();
            ft.replace(R.id.main_all_fragment_frame, searchFragment, "ALL_MANY_ROOM").commit();
        }
    }

    public void openSearchServiceUserManyRoomDataFragment(String user_service) {


            //Make visible the frameLayout
            mNavHost.setVisibility(View.GONE);
            mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
            mNavView.setVisibility(View.GONE);

            ManyRoomFragment searchFragment = ManyRoomFragment.newSearchServiceUserInstance("Proposants Services", user_service);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.addToBackStack(null);
            //getSupportFragmentManager().popBackStack();
            ft.replace(R.id.main_all_fragment_frame, searchFragment, "ALL_MANY_ROOM").commit();

    }


    public void openSearchResultServiceManyRoomDataFragment(String fragment_title, SearchDataModelService data) {

        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
        mNavView.setVisibility(View.GONE);

        ManyRoomFragment searchFragment = ManyRoomFragment.newSearchInstance(fragment_title, data);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        //getSupportFragmentManager().popBackStack();
        ft.addToBackStack(null);
        ft.add(R.id.main_all_fragment_frame, searchFragment, "ALL_MANY_ROOM_SEARCH").commit();
    }

    public void openSearchResultRoomManyRoomDataFragment(String fragment_title, SearchDataModelRoom data) {

        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
        mNavView.setVisibility(View.GONE);

        ManyRoomFragment searchFragment = ManyRoomFragment.newSearchRoomInstance(fragment_title, data);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        //getSupportFragmentManager().popBackStack();
        ft.addToBackStack(null);
        ft.add(R.id.main_all_fragment_frame, searchFragment, "ALL_MANY_ROOM_SEARCH").commit();
    }



    /**
     * Open the login fragment
     */
    public void openLoginFragment() {
        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
        mNavView.setVisibility(View.GONE);

        LoginFragment loginFragment = new LoginFragment().newInstance("", "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //getSupportFragmentManager().popBackStack();
        ft.replace(R.id.main_all_fragment_frame, loginFragment, "LOGIN_SAMPLE").commit();
    }

    /**
     * Open login fragment
     * with information
     */
    public void openLoginWithInfoFragment() {
        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
        mNavView.setVisibility(View.GONE);

        LoginWithInfoFragment loginFragment = new LoginWithInfoFragment().newInstance("", "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //getSupportFragmentManager().popBackStack();
        ft.replace(R.id.main_all_fragment_frame, loginFragment, "LOGIN_INFO").commit();
    }

    /**
     * Open registration fragment
     */
    public void openLoginRegisterFragment() {
        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
        mNavView.setVisibility(View.GONE);

        RegisterFragment loginFragment = new RegisterFragment().newInstance("", "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //ft.add(R.id.main_all_fragment_frame, loginFragment, "LOGIN_REGISTER").commit();
        //Close the current fragment
        //getSupportFragmentManager().popBackStack();
        ft.replace(R.id.main_all_fragment_frame, loginFragment, "LOGIN_REGISTER").commit();
    }

    public void openServicesFragment() {
        //Make visible the frameLayout
        mNavHost.setVisibility(View.GONE);
        mAllFragmentFrameLayout.setVisibility(View.VISIBLE);
        mNavView.setVisibility(View.GONE);

        ServicesFragment servicesFragment = new ServicesFragment().newInstance("", "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //ft.add(R.id.main_all_fragment_frame, loginFragment, "LOGIN_REGISTER").commit();
        //Close the current fragment
        //getSupportFragmentManager().popBackStack();
        ft.replace(R.id.main_all_fragment_frame, servicesFragment, "LOGIN_REGISTER").commit();
    }

    @Override
    public void onRoomDataImageClicked(RoomDataImagesDummyContent.RoomDataImagesDummyItem item) {
        //Pass Image as parameters
        OneRoomImagePhotoViewFragment fragment;
        if (item.mImage.startsWith("http"))
            fragment = OneRoomImagePhotoViewFragment.newInstance(item.mImage, "");
        else
            fragment = OneRoomImagePhotoViewFragment.newInstance("", "");
        //RoomImageDialogFragment roomImageDialogFragment = new RoomImageDialogFragment();
        //roomImageDialogFragment.show(getSupportFragmentManager(), "ROOM_ONE");


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        //ft.add(R.id.main_all_fragment_frame, loginFragment, "LOGIN_REGISTER").commit();
        //Close the current fragment
        //getSupportFragmentManager().popBackStack();
        ft.replace(R.id.main_all_fragment_frame, fragment, "PHOT_View").commit();
    }
    //#############################################################################################


    @Override
    protected void onDestroy() {
        //AppDataBaseManager.DB_USER_TABLE
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {



            super.onBackPressed();


            if (mAllFragmentFrameLayout.getVisibility() == View.VISIBLE) {

                //Count the numbers of remaining fragment stack
                if( getSupportFragmentManager().getBackStackEntryCount() == 0 ){


                    mAllFragmentFrameLayout.setVisibility(View.GONE);
                    mNavView.setVisibility(View.VISIBLE);
                    mNavHost.setVisibility(View.VISIBLE);
                }
            } else {

            }
        }

    }


    //#######################################################
    //Registration
    @Override
    public void onRegistrationSuccessful(FirebaseUser firebaseUser, final String tel) {
        //Make the loggin paramters succeed
        this.onBackPressed();

        this.mUserAlreadyLogged = true;
        mAuthFirebaseUser = firebaseUser; this.onBackPressed();

        //[START updating the user information in the data base
        if (tel.length() > 0) {
            //Create a firebase user here
            //[START create firebase user]
            FirestoreNambooUserHelper.createUser(mAuthFirebaseUser.getUid(), tel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //We try to create a user here
                    mCurrentUser.setTel(tel);
                    mCurrentUser.setUid(mAuthFirebaseUser.getUid());

                    AppDataBaseManager.DB_USER_TABLE.updateUserUid(mCurrentUser);
                    AppDataBaseManager.DB_USER_TABLE.updateUserUserTel(mCurrentUser);

                    Log.i("DATABASE UPDATE",
                            "Saving with " + mCurrentUser.getTel() + " Uid: " + mCurrentUser.getUid());
                }
            });
            //[END create firebase user]
        }
        //[END updating the user information in the data base
    }

    //#######################################################
    //Profile
    @Override
    public void onUsernameUpdated(String new_userName) {

        //Try to update the username in firebase
        //If success we update
        //The current user data
        mCurrentUser.setUsername(new_userName);
        AppDataBaseManager.DB_USER_TABLE.updateUserUserName(mCurrentUser);

        Toast.makeText(this, "Nom mis à jour", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUserStatusUpdated(String new_status) {
        //Update the database with the new status
        mCurrentUser.setStatus(new_status);
        AppDataBaseManager.DB_USER_TABLE.updateUserStatus(mCurrentUser);

        Toast.makeText(this, "Status mis à jour", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserPasswordUpdated(String new_pass) {
        mCurrentUser.setPassword(new_pass);
        AppDataBaseManager.DB_USER_TABLE.updateUserUserPassword(mCurrentUser);

        Toast.makeText(this, "Mot de passe mis à jour", Toast.LENGTH_SHORT).show();
    }


    private OnFailureListener onFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Ereur inconnue", Toast.LENGTH_SHORT).show();
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "Permission(s) accordée(s)", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    //#############################################################################################
    //User getting position
    public boolean getUserCurrentLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        }
        else {
            EasyPermissions.requestPermissions(this, "Nous avons besoin de votre permission ",
                    10, perms);
        }
        return false;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }



    //#############################################################################################
    //PERMISSION CHECKER
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
