package com.xampy.namboo.ui.profile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.xampy.namboo.api.database.AppDataBaseManager;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooBoostedHelper;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooPostHelper;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooUserHelper;
import com.xampy.namboo.ui.commonDialogFragment.CommonActivateAccountMessageDialogFragment;
import com.xampy.namboo.ui.commonDialogFragment.CommonAskActivateInternetConnectionDialogFragment;
import com.xampy.namboo.ui.commonDialogFragment.CommonConfirmActionDialogFragment;
import com.xampy.namboo.ui.commonDialogFragment.CommonUploadingProgressDialogFragment;
import com.xampy.namboo.ui.fonds.ConfirmCreditBuyDialogFragment;
import com.xampy.namboo.ui.profile.profileData.myPosts.MyPostsDataAdapter;
import com.xampy.namboo.ui.profile.profileData.myPosts.MyPostsFireStoreAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.xampy.namboo.MainActivity.mCurrentUser;

public class ProfileFragment extends Fragment  {

    private LinearLayout mSetProfileName;
    private LinearLayout mSetProfileStatus;
    private CircleImageView mUserProfileImage;
    private Uri mUriImageSelected;
    private MyPostsFireStoreAdapter mMyPostsDataFirebaseAdapter;
    private LinearLayout mSetProfilePassword;
    private LinearLayout mSetProfileGPSPosition;
    private LinearLayout mSetProfileCity;
    private LinearLayout mSetProfileDistrict;
    private StorageReference mStorageReference;
    private CommonUploadingProgressDialogFragment mUpdatingProgressState;
    private TextView mAskingAccountActivation;

    public interface ProfileFragmentInteractionListener {
        public void onUsernameUpdated(String new_userName);
        public void onUserStatusUpdated(String new_status);
        public void onUserPasswordUpdated(String new_pass);
    }

    private ProfileFragmentInteractionListener mListener;


    private NotificationsViewModel notificationsViewModel;
    private MyPostsDataAdapter mMyPostsDataAdapter;
    private RecyclerView mMyPostsDataView;
    private FrameLayout mMyPostsListFrame;
    private FrameLayout mMyMoreFrame;
    private TextView mMyPostsText;
    private TextView mMyMoreText;


    // Creating identifier to identify REST REQUEST (Update username)
    private static final int UPDATE_USERNAME = 30;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_USER_NAME = "user_name";
    private static final String ARG_JOB_TYPE = "user_job";

    // TODO: Rename and change types of parameters
    private String mUserPhoneNumber;
    private String mUsername;
    private String mUserJobType;


    private int RC_CHOOSE_PROFILE_IMAGE = 100;

    /*public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param_user_name Parameter 1.
     * @param param_phone_number Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static ProfileFragment newInstance(
            String param_user_name,
            String param_phone_number,
            String param_job_type) {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, param_user_name);
        args.putString(ARG_PHONE_NUMBER, param_phone_number);
        args.putString(ARG_JOB_TYPE , param_job_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USER_NAME);
            mUserPhoneNumber = getArguments().getString(ARG_PHONE_NUMBER);
            mUserJobType = getArguments().getString(ARG_JOB_TYPE);
        }
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mStorageReference =FirebaseStorage.getInstance().getReference("USERS_PROFILE_IMAGES");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_profile, container, false);



        //Cause of two call one form the bootom navigation
        //and one from a button on home fragment
        //We have to check also the login state form
        //main activity
        if(MainActivity.mUserAlreadyLogged){
            root.findViewById(R.id.profile_not_logged).setVisibility(View.GONE);
            root.findViewById(R.id.profile_already_logged).setVisibility(View.VISIBLE);
        }else {
            root.findViewById(R.id.profile_already_logged).setVisibility(View.GONE);
            root.findViewById(R.id.profile_not_logged).setVisibility(View.VISIBLE);
        }

        ((TextView)root.findViewById(R.id.profile_no_login_text_view_to_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the activity login fragment
                ((MainActivity) getActivity()).openLoginFragment();
            }
        });


        //[START image clicking updating etc ....]
        mUserProfileImage = (CircleImageView) root.findViewById(R.id.profile_user_image);
        mUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the file choosing dialog
                Intent image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(image, RC_CHOOSE_PROFILE_IMAGE);
            }
        });
        //[END image clicking updating etc ....]

        //[START setting name phone number and so
        if(!mCurrentUser.getUid().equals("@none")){
            FirestoreNambooUserHelper.getUser(mCurrentUser.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    UserNambooFirestore user = documentSnapshot.toObject(UserNambooFirestore.class);
                    //Update view in the profile fragment
                    assert user != null;
                    ( (TextView) root.findViewById(R.id.profile_user_is_agent) ).setText(user.getServiceType());
                    ( (TextView) root.findViewById(R.id.profile_user_is_agent_second_value) ).setText(user.getServiceType());

                    String image =  user.getUrlPicture();
                    if(image.startsWith("http") ) {
                        //Download the image to image view on profile
                        Glide.with(getContext())
                                .load(image).
                                into( ( (ImageView) root.findViewById(R.id.profile_user_image) ));
                    }
                }
            });
        }

        ( (TextView) root.findViewById(R.id.profile_user_name_value) ).setText(mCurrentUser.getUsername());
        ( (TextView) root.findViewById(R.id.profile_user_name_second_value) ).setText(mCurrentUser.getUsername());

        ( (TextView) root.findViewById(R.id.profile_user_phone_value) ).setText(mCurrentUser.getTel());

        ( (TextView) root.findViewById(R.id.profile_user_is_agent) ).setText(mCurrentUser.getStatus());
        ( (TextView) root.findViewById(R.id.profile_user_is_agent_second_value) ).setText(mCurrentUser.getStatus());
        //[START setting name phone number and so

        //[START my post adapters
        /*mMyPostsDataAdapter = new MyPostsDataAdapter(MY_POSTS_DATA_DUMMY_ITEMS, null);
        mMyPostsDataView = (RecyclerView) root.findViewById(R.id.profile_user_posts_list);
        mMyPostsDataView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMyPostsDataView.setAdapter(mMyPostsDataAdapter);*/

        String uid = mCurrentUser.getUid();
        if(!uid.equals("@none")) {
            mMyPostsDataFirebaseAdapter = new MyPostsFireStoreAdapter(
                    generateOptionsForAdapter(
                            FirestoreNambooPostHelper.getCurrentUserPosts(uid)
                    ),
                    Glide.with(getContext()),
                    new MyPostsFireStoreAdapter.MyPostListener() {
                        @Override
                        public void onDataChanged() {
                            //
                        }

                        @Override
                        public void onBoostMyPostClicked(final String uid, final MyPostsFireStoreAdapter.MyPostViewHolder holder) {

                            final ConfirmCreditBuyDialogFragment dialogFragment = ConfirmCreditBuyDialogFragment.newInstance(
                                    "20 Crédits", "-"
                            );
                            dialogFragment.setmListener(new ConfirmCreditBuyDialogFragment.ConfirmCreditBuyDialogFragmentInteractionListener() {
                                @Override
                                public void onContinueClicked(ConfirmCreditBuyDialogFragment fragment) {
                                    //Check if we have the required amount


                                    //Check Internet Connectivity
                                    if(
                                            ( (MainActivity)getActivity()).isNetworkConnected() ){


                                        //Update the data then
                                        //Activate account cost 10 credits
                                        if (true) { //mCurrentUser.getCredit_amount() - 20 > 0
                                            dialogFragment.dismiss();
                                            mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                                            mUpdatingProgressState.setCancelable(false);
                                            mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPDATE_BOOST");


                                            try{
                                                FirestoreNambooPostHelper.updatePostBoostedState(true, uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        try {
                                                            FirestoreNambooBoostedHelper.createBoostedPost(uid, holder.mItem.getmUserSenderUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getContext(), "Annonce boostée", Toast.LENGTH_SHORT).show();
                                                                    mUpdatingProgressState.dismiss();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {e.printStackTrace();
                                                                    Toast.makeText(getContext(), "Erreur survenue booted", Toast.LENGTH_SHORT).show();
                                                                    mUpdatingProgressState.dismiss();
                                                                }
                                                            });
                                                        }catch (Exception e){
                                                            Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();
                                                            mUpdatingProgressState.dismiss();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();
                                                        mUpdatingProgressState.dismiss();
                                                    }
                                                });
                                            }catch (Exception e){
                                                Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();
                                                mUpdatingProgressState.dismiss();
                                            }

                                        }else {
                                            Toast.makeText(getContext(), "Crédits insuffisants", Toast.LENGTH_SHORT).show();
                                        }



                                    }else {
                                        //We show error
                                        CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                                new CommonAskActivateInternetConnectionDialogFragment();

                                        activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                                    }

                                }
                            });//[END boosting ]

                            dialogFragment.show(getActivity().getSupportFragmentManager(), "UPDATE_POST_BOOST");
                        }

                        @Override
                        public void onDeleteMyPost(final String uid) {
                            CommonConfirmActionDialogFragment confirm_fragment = new CommonConfirmActionDialogFragment();
                            confirm_fragment.setListener(new CommonConfirmActionDialogFragment.CommonConfirmActionDialogFragmentInteractionListener() {
                                @Override
                                public void onYesClicked() {
                                    //Initialize  the delete here

                                    //Check Internet Connectivity
                                    if(
                                            ( (MainActivity)getActivity()).isNetworkConnected() ){

                                        mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                                        mUpdatingProgressState.setCancelable(false);
                                        mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPDATE_BOOST");


                                        try{
                                            FirestoreNambooPostHelper.deletePost(uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "Annonce Supprimée", Toast.LENGTH_SHORT).show();
                                                    mUpdatingProgressState.dismiss();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();
                                                    mUpdatingProgressState.dismiss();
                                                }
                                            });
                                        }catch (Exception e){
                                            Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();
                                            mUpdatingProgressState.dismiss();
                                        }

                                    }else {
                                        //We show error
                                        CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                                new CommonAskActivateInternetConnectionDialogFragment();

                                        activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                                    }
                                }

                                @Override
                                public void onNoClicked() {
                                    //Nothing to do
                                }
                            });

                            confirm_fragment.show(getActivity().getSupportFragmentManager(), "CONFIRM_DELETE");
                        }
                    });

            mMyPostsDataView = (RecyclerView) root.findViewById(R.id.profile_user_posts_list);
            mMyPostsDataView.setLayoutManager(new LinearLayoutManager(getContext()));
            mMyPostsDataView.setAdapter(mMyPostsDataFirebaseAdapter);
            //[END my post adapters]
        }


        mMyPostsListFrame = (FrameLayout)root.findViewById(R.id.profile_my_posts_list_frame);
        mMyMoreFrame = (FrameLayout)root.findViewById(R.id.profile_more_frame);
        mMyPostsText = (TextView) root.findViewById(R.id.profile_my_post_text_view);
        mMyMoreText = (TextView) root.findViewById(R.id.profile_more_text_view);


        mMyPostsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide frame
                mMyPostsListFrame.setVisibility(View.VISIBLE);
                mMyMoreFrame.setVisibility(View.GONE);

                ((TextView)v).setTextColor(Color.rgb(59, 99, 255));
                mMyMoreText.setTextColor(Color.rgb(0, 0, 0));
            }
        });


        mMyMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide frame
                mMyMoreFrame.setVisibility(View.VISIBLE);
                mMyPostsListFrame.setVisibility(View.GONE);

                ((TextView)v).setTextColor(Color.rgb(59, 99, 255));
                mMyPostsText.setTextColor(Color.rgb(0, 0, 0));
            }
        });


        //My profile settings
        mSetProfileName = (LinearLayout)root.findViewById(R.id.profile_set_name_layout);
        mSetProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show the update name dialog
                ProfileSetNameDialogFragment dialog = new ProfileSetNameDialogFragment();
                dialog.setListener(new ProfileSetNameDialogFragment.NambooProfileNameSetterDialogListener() {
                    @Override
                    public void onNambooProfileNameSetterSaveButtonClicked(DialogFragment fragment) {
                        //Call this interface
                        //Get the edit
                        final String t = ( (ProfileSetNameDialogFragment)fragment ).getEditTextNameValue();
                        if(t.length() > 0){
                            //Check the difference between the new and the old

                            //Call the Firebase to update name
                            //if(t != null){}
                            //Check Internet Connectivity
                            if(
                                    ( (MainActivity)getActivity()).isNetworkConnected() ){


                                mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                                mUpdatingProgressState.setCancelable(false);
                                mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");


                                try {
                                    FirestoreNambooUserHelper.updateUsername(t, mCurrentUser.getUid())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //If success set the new user name
                                                    ( (TextView) root.findViewById(R.id.profile_user_name_value) ).setText(t);
                                                    ( (TextView) root.findViewById(R.id.profile_user_name_second_value) ).setText(t);

                                                    //Update username on database in the main activity
                                                    mListener.onUsernameUpdated(t);

                                                    //Close progress bar dialog
                                                    if(mUpdatingProgressState != null){
                                                        mUpdatingProgressState.dismiss();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mUpdatingProgressState.dismiss();
                                            Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();

                                    mUpdatingProgressState.dismiss();
                                    Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                //We show error
                                CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                        new CommonAskActivateInternetConnectionDialogFragment();

                                activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                            }

                        }
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "SET_NAME");
            }
        });

        //[START setting my status]
        //My profile settings
        mSetProfileStatus = (LinearLayout)root.findViewById(R.id.profile_set_status_layout);
        mSetProfileStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileSetStatutDialogFragment dialog = new ProfileSetStatutDialogFragment();
                dialog.setListener(new ProfileSetStatutDialogFragment.NambooProfileStatutSetterDialogListener() {
                    @Override
                    public void onNambooProfileStatusSetterSaveButtonClicked(DialogFragment fragment) {
                        //Call this interface
                        //Get the edit
                        final String status = ( (ProfileSetStatutDialogFragment)fragment ).getSpinnerChoiceValue();
                        //Check the difference between the new and the old

                        if(
                                ( (MainActivity)getActivity()).isNetworkConnected() ){


                            mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                            mUpdatingProgressState.setCancelable(false);
                            mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");

                            try {
                                //Call the Firebase to update user status
                                FirestoreNambooUserHelper.updateUserServiceType(status, mCurrentUser.getUid())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                //Save data to database
                                                mListener.onUserStatusUpdated(status);
                                                mCurrentUser.setStatus(status);
                                                AppDataBaseManager.DB_USER_TABLE.updateUserStatus(mCurrentUser);

                                                //Update view in the profile fragment
                                                ( (TextView) root.findViewById(R.id.profile_user_is_agent) ).setText(mCurrentUser.getStatus());
                                                ( (TextView) root.findViewById(R.id.profile_user_is_agent_second_value) ).setText(mCurrentUser.getStatus());

                                                if(mUpdatingProgressState != null){
                                                    mUpdatingProgressState.dismiss();
                                                }

                                                //Update account activation setting here
                                                if(!mCurrentUser.isAccount_activated()){
                                                    Toast.makeText(getContext(), mCurrentUser.getStatus(), Toast.LENGTH_SHORT).show();

                                                    //The new status if different from Particular and Sales
                                                    //The account is already activated. Then we continue
                                                    if(!status.equals("Particulier") && !mCurrentUser.getStatus().equals("Agent Immobilier")) {

                                                        mAskingAccountActivation.setVisibility(View.VISIBLE);
                                                        CommonActivateAccountMessageDialogFragment dialogFragment = new CommonActivateAccountMessageDialogFragment();
                                                        dialogFragment.show(getActivity().getSupportFragmentManager(), "ACTIVATE_ACCOUNT");
                                                    }else {
                                                        mAskingAccountActivation.setVisibility(View.GONE);
                                                    }
                                                }else {
                                                    //The account is activated
                                                    if(!status.equals("Particulier") && !mCurrentUser.getStatus().equals("Agent Immobilier")) {
                                                        //We pass
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mUpdatingProgressState.dismiss();
                                        Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }catch (Exception e){
                                mUpdatingProgressState.dismiss();
                                Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            //We show error
                            CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                    new CommonAskActivateInternetConnectionDialogFragment();

                            activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                        }

                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "SET_STATUS");
            }
        });
        //[END setting my status]


        //[START password setter]
        mSetProfilePassword = (LinearLayout)root.findViewById(R.id.profile_set_pass_layout);
        mSetProfilePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileSetPasswordDialogFragment dialog = new ProfileSetPasswordDialogFragment();
                dialog.setListener(new ProfileSetPasswordDialogFragment.NambooProfilePasswordSetterDialogListener() {
                    @Override
                    public void onNambooProfilePasswordSetterSaveButtonClicked(DialogFragment fragment) {
                        //Get the result form edit text
                    }

                    @Override
                    public void ononNambooProfilePasswordSetterUpdated(final String new_pass) {
                        //Ask firebase to update the password


                        if(
                                ( (MainActivity)getActivity()).isNetworkConnected() ) {

                            mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                            mUpdatingProgressState.setCancelable(false);
                            mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");

                            try {
                                FirestoreNambooUserHelper.updateUserPassword(new_pass, mCurrentUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //We update the database
                                        mListener.onUserPasswordUpdated(new_pass);

                                        //The password field is always hiden
                                        if(mUpdatingProgressState != null){
                                            mUpdatingProgressState.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mUpdatingProgressState.dismiss();
                                        Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }catch (Exception e){
                                mUpdatingProgressState.dismiss();
                                Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            //We show error
                            CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                    new CommonAskActivateInternetConnectionDialogFragment();

                            activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                        }

                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "SET_PASSWORD");
            }
        });
        //[END password setter]

        //[START city setter]
        mSetProfileCity = (LinearLayout)root.findViewById(R.id.profile_set_ville_layout);
        mSetProfileCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProfileSetCityDialogFragment dialog = new ProfileSetCityDialogFragment();
                dialog.setListener(new ProfileSetCityDialogFragment.NambooProfileCitySetterDialogListener() {
                    @Override
                    public void onNambooProfileCitySetterSaveButtonClicked(DialogFragment fragment) {
                        final String city = dialog.getSpinnerChoiceValue();

                        if(
                                ( (MainActivity)getActivity()).isNetworkConnected() ) {

                            mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                            mUpdatingProgressState.setCancelable(false);
                            mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");


                            try {
                                FirestoreNambooUserHelper.updateUserCity(city, mCurrentUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ((TextView)root.findViewById(R.id.profile_user_ville_value)).setText(city);
                                        Toast.makeText(getContext(), "Ville mise à jour", Toast.LENGTH_SHORT).show();

                                        if(mUpdatingProgressState != null){
                                            mUpdatingProgressState.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mUpdatingProgressState.dismiss();
                                        Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }catch (Exception e){
                                mUpdatingProgressState.dismiss();
                                Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                            }


                        }else{
                            //We show error
                            CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                    new CommonAskActivateInternetConnectionDialogFragment();

                            activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                        }
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "SET_CITY");
            }
        });
        //[END city setter]

        //[START district setter]
        mSetProfileDistrict = (LinearLayout)root.findViewById(R.id.profile_set_quartier_layout);
        mSetProfileDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProfileSetDistrictDialogFragment dialog = new ProfileSetDistrictDialogFragment();
                dialog.setListener(new ProfileSetDistrictDialogFragment.NambooProfileDistrictSetterDialogListener() {
                    @Override
                    public void onNambooProfileDistrictSetterSaveButtonClicked(DialogFragment fragment) {
                        final String district = dialog.getSpinnerChoiceValue();

                        if(
                                ( (MainActivity)getActivity()).isNetworkConnected() ) {

                            try {
                                mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                                mUpdatingProgressState.setCancelable(false);
                                mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");

                                FirestoreNambooUserHelper.updateUserDistrict(district, mCurrentUser.getUid())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ((TextView)root.findViewById(R.id.profile_user_quartier_value)).setText(district);
                                                Toast.makeText(getContext(), "Quartier Mis à jours", Toast.LENGTH_SHORT).show();

                                                if(mUpdatingProgressState != null){
                                                    mUpdatingProgressState.dismiss();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mUpdatingProgressState.dismiss();
                                        Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mUpdatingProgressState.dismiss();
                                        Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }catch (Exception e){
                                mUpdatingProgressState.dismiss();
                                Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            //We show error
                            CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                    new CommonAskActivateInternetConnectionDialogFragment();

                            activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                        }
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "SET_DISTRICT");
            }
        });


        //[START Position GPS setter]
        mSetProfileGPSPosition = (LinearLayout)root.findViewById(R.id.profile_set_position_layout);
        mSetProfileGPSPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request the user position
                boolean to_locate = ( (MainActivity) getActivity()).getUserCurrentLocation();


                //Get location here
                if(to_locate){

                    if(
                            ( (MainActivity)getActivity()).isNetworkConnected() ) {
                        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    if(MainActivity.mPositionLonLat == null) {
                                        String l = location.getLongitude() + ";" + location.getLatitude();
                                        Log.i("GOT POSITIONT", l);


                                        mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                                        mUpdatingProgressState.setCancelable(false);
                                        mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");

                                        try {
                                            FirestoreNambooUserHelper.updateUserPosition(l, mCurrentUser.getUid()).addOnSuccessListener(
                                                    new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getContext(), "Position mise à jour", Toast.LENGTH_SHORT).show();

                                                            if(mUpdatingProgressState != null){
                                                                mUpdatingProgressState.dismiss();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    mUpdatingProgressState.dismiss();
                                                    Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }catch (Exception e){
                                            mUpdatingProgressState.dismiss();

                                            Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();
                                            Log.i("GOT POSITION ", e.toString());
                                        }

                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mUpdatingProgressState.dismiss();
                                Toast.makeText(getContext(), "Erreur...", Toast.LENGTH_LONG).show();
                                Log.i("GOT POSITION", e.toString());
                            }
                        });
                    }else{
                        //We show error
                        CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                new CommonAskActivateInternetConnectionDialogFragment();

                        activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                    }

                }else {
                    Toast.makeText(getContext(), "Activez votre GPS", Toast.LENGTH_LONG).show();
                }
            }
        });
        //[START Position GPS setter]




        mAskingAccountActivation = (TextView) root.findViewById(R.id.profile_user_ask_activating_account);
        mAskingAccountActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmCreditBuyDialogFragment dialogFragment = ConfirmCreditBuyDialogFragment.newInstance(
                        "10 Crédits", "-"
                );
                dialogFragment.setmListener(new ConfirmCreditBuyDialogFragment.ConfirmCreditBuyDialogFragmentInteractionListener() {
                    @Override
                    public void onContinueClicked(ConfirmCreditBuyDialogFragment fragment) {
                        //Check if we have the required amount

                        //Activate account cost 10 credits
                        if(true){ //mCurrentUser.getCredit_amount() - 10 > 0
                            fragment.dismiss();
                            //We procees transaction
                            if(
                                    ( (MainActivity)getActivity()).isNetworkConnected() ) {

                                mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                                mUpdatingProgressState.setCancelable(false);
                                mUpdatingProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPDATE_ACTIVATION_STATE");

                                try {
                                    FirestoreNambooUserHelper.updateUserActivationState(true,
                                            mCurrentUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mCurrentUser.setAccount_activated(true);
                                            AppDataBaseManager.DB_USER_TABLE.updateUserAccountActivatedState(mCurrentUser);

                                            //Hode the activation asking button
                                            mAskingAccountActivation.setVisibility(View.GONE);


                                            Toast.makeText(getContext(), "Activé avec success", Toast.LENGTH_SHORT).show();
                                            if(mUpdatingProgressState != null){
                                                mUpdatingProgressState.dismiss();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();
                                            if(mUpdatingProgressState != null){
                                                mUpdatingProgressState.dismiss();
                                            }
                                        }
                                    });
                                }catch (Exception e){
                                    if(mUpdatingProgressState != null){
                                        mUpdatingProgressState.dismiss();
                                    }
                                }

                            }else{
                                //We show error
                                CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                        new CommonAskActivateInternetConnectionDialogFragment();

                                activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                            }

                        }else {
                            //Showing alert for insufficient amount
                            fragment.dismiss();
                            Toast.makeText(getContext(), "Crédits insuffisants", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialogFragment.show(getActivity().getSupportFragmentManager(), "CONFIRM_ACTIVATION");
            }
        });
        //######################################################################################################
        if(!mCurrentUser.isAccount_activated()){
            Toast.makeText(getContext(), mCurrentUser.getStatus(), Toast.LENGTH_SHORT).show();
            if(!mCurrentUser.getStatus().equals("Particulier") && !mCurrentUser.getStatus().equals("Agent Immobilier")) {
                mAskingAccountActivation.setVisibility(View.VISIBLE);
                CommonActivateAccountMessageDialogFragment dialogFragment = new CommonActivateAccountMessageDialogFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "ACTIVATE_ACCOUNT");
            }
        }
        return root;
    }

    private FirestoreRecyclerOptions<PostNambooFirestore> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<PostNambooFirestore>()
                .setQuery(query, PostNambooFirestore.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (ProfileFragmentInteractionListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("MainActivityActivity must implement the interface");
        }
    }

    @Override
    public void onDetach() {
        if(mListener != null)
            mListener = null;
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==RC_CHOOSE_PROFILE_IMAGE){
            if(resultCode == RESULT_OK) {
                mUriImageSelected = data.getData();
                Log.i("Choosing image ", mUriImageSelected.toString());
                mUserProfileImage.setImageURI(mUriImageSelected);

                //Remenber if successfully added to firestore
                //copy image to application folder

                //[START send image to firestore ]
                StorageReference mImageRef = mStorageReference.child(
                        System.currentTimeMillis() + mCurrentUser.getTel() + "_." + getExtension(mUriImageSelected)
                );


                mUpdatingProgressState = new CommonUploadingProgressDialogFragment();
                mUpdatingProgressState.setCancelable(false);
                mUpdatingProgressState.show(getActivity().getSupportFragmentManager(), "UPLOAD_IMAGE");
                mImageRef.putFile(mUriImageSelected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Update the user profile image
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imagePath = uri.toString();
                                FirestoreNambooUserHelper.updateUserImageURl(
                                        imagePath, mCurrentUser.getUid())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Image updated", Toast.LENGTH_SHORT).show();

                                                //Dismiss progress bar
                                                mUpdatingProgressState.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Erreur survenu", Toast.LENGTH_SHORT).show();

                                        //Dismiss progress bar
                                        mUpdatingProgressState.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Erreur survenu", Toast.LENGTH_SHORT).show();

                                //Dismiss progress bar
                                mUpdatingProgressState.dismiss();
                            }
                        });

                    }
                });
                //[END]
            }
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private OnFailureListener onFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Ereur inconnue", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
