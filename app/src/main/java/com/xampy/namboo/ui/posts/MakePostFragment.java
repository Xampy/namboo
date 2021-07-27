package com.xampy.namboo.ui.posts;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooPostHelper;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooUserHelper;
import com.xampy.namboo.ui.commonDialogFragment.CommonAskActivateInternetConnectionDialogFragment;
import com.xampy.namboo.ui.commonDialogFragment.MakePostProgressDialogFragment;
import com.xampy.namboo.ui.custom.EditTextSpaceDelimiter;
import com.xampy.namboo.ui.fonds.ConfirmCreditBuyDialogFragment;
import com.xampy.namboo.ui.posts.postsData.addRoomImagesList.AddRoomImagesDataAdapter;
import com.xampy.namboo.ui.posts.postsData.addRoomImagesList.AddRoomImagesDataDummyContent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xampy.namboo.ui.posts.postsData.addRoomImagesList.AddRoomImagesDataDummyContent;

import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.xampy.namboo.MainActivity.mCurrentUser;
import static com.xampy.namboo.MainActivity.mUserAlreadyLogged;
import static com.xampy.namboo.ui.posts.postsData.addRoomImagesList.AddRoomImagesDataDummyContent.ADD_ROOM_IMAGES_DATA_DUMMY_ITEMS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakePostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_LOGGED = "param_logged";
    private static final int FILE_SELECT_CODE = 2000;

    private final String PRICE_INPUT_DELIMITER = " ";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mAddRoomImagesDataView;
    private AddRoomImagesDataAdapter mAddRoomImagesDataAdapter;
    private Spinner mPostTypeSpinner;
    private Spinner mRoomTypeSpinner;
    private Spinner mCityChoosingSpinner;
    private Spinner mDistrictChoosingSpinner;
    private Button mPostButton;
    private OnMakePostFragmentInteractionListener mAddRoomImagesDataListener = new OnMakePostFragmentInteractionListener() {
        @Override
        public void onAddImageClicked(AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem mItem, ImageView mImage, int position) {
            Toast.makeText(getContext(), "Photo " + position + "is clicked", Toast.LENGTH_SHORT).show();
        }
    };
    private Uri mLastImageGot;
    private TextView mWantToLogin;
    private boolean mParamUserLoginState;
    private int mCurrentImageClickedId;
    private ImageView[] mImagesChooser;
    private Uri[] mImagesUri;
    private String[] mImagesUriString;
    private int mCountImageUploaded;

    private StorageReference mStorageReference;
    private MakePostProgressDialogFragment mMakePostProgressBar;

    public interface MakePostFragmentListener {
        void OnImageFilesUploaded(int count_updated, View view);
    }

    private MakePostFragmentListener listener = new MakePostFragmentListener() {
        @Override
        public void OnImageFilesUploaded(int count_updated, View v) {
            if(count_updated == 6){
                //Toast.makeText(getContext(), "All images are uploaded", Toast.LENGTH_LONG).show();
                sendPostInformation(v);
            }
        }
    };

    public interface MakePostProgressFragmentListener {
        void onProgressDataUpdated(int data);
    }

    public MakePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakePostFragment newInstance(String param1, String param2, boolean userLogState) {
        MakePostFragment fragment = new MakePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM_LOGGED, userLogState);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParamUserLoginState = getArguments().getBoolean(ARG_PARAM_LOGGED);
        }

        mStorageReference =FirebaseStorage.getInstance().getReference("POSTS_IMAGES");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_make_post, container, false);

        //If the user is already logged
        //Show the post parameters

        //Cause of two call one form the bootom navigation
        //and one from a button on home fagment
        //We have to check also the login state form
        //main activity
        if(mParamUserLoginState || mUserAlreadyLogged){
            root.findViewById(R.id.make_post_not_logged).setVisibility(View.GONE);
            root.findViewById(R.id.make_post_already_logged).setVisibility(View.VISIBLE);

        }else {
            root.findViewById(R.id.make_post_already_logged).setVisibility(View.GONE);
            root.findViewById(R.id.make_post_not_logged).setVisibility(View.VISIBLE);
        }

        //[START Adding position for particular
        Toast.makeText(getContext(), mCurrentUser.getStatus(), Toast.LENGTH_SHORT).show();
        if(mCurrentUser.getStatus().equals("Particulier"))
            ((LinearLayout)root.findViewById(R.id.make_post_add_position)).setVisibility(View.VISIBLE);
        else
            ((LinearLayout)root.findViewById(R.id.make_post_add_position)).setVisibility(View.GONE);
        //[END Adding position for particular





        //If not connected show the connection frame
        mWantToLogin = (TextView) root.findViewById(R.id.make_post_no_login_text_view_to_login);
        mWantToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the connection fragment
                ((MainActivity)getActivity()).openLoginFragment();
            }
        });



        //[START This is no longer available]
        mAddRoomImagesDataAdapter = new AddRoomImagesDataAdapter(
                AddRoomImagesDataDummyContent.ADD_ROOM_IMAGES_DATA_DUMMY_ITEMS ,
                this.mAddRoomImagesDataListener);
        mAddRoomImagesDataView = (RecyclerView) root.findViewById(R.id.make_post_images_list);
        mAddRoomImagesDataView.setLayoutManager(
                new GridLayoutManager(getContext(),
                        3, GridLayoutManager.VERTICAL, false));
        mAddRoomImagesDataView.setAdapter(mAddRoomImagesDataAdapter);
        //[END  suppress it later ]


        //[START choosing images]
        mImagesChooser = new ImageView[]{
                (ImageView) root.findViewById(R.id.make_post_image_1),
                (ImageView) root.findViewById(R.id.make_post_image_2),
                (ImageView) root.findViewById(R.id.make_post_image_3),
                (ImageView) root.findViewById(R.id.make_post_image_4),
                (ImageView) root.findViewById(R.id.make_post_image_5),
                (ImageView) root.findViewById(R.id.make_post_image_6)};

        mImagesUri = new Uri[]{null, null, null, null, null, null};
        mImagesUriString = new String[] {"", "", "", "", "", ""};
        mCurrentImageClickedId = -1;

        for (int i=0; i < 6; i++){


            final int finalI = i;
            mImagesChooser[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentImageClickedId = finalI;
                    showFileChooser();
                }
            });
        }
        //[END]




        //[START price input text listener]
        final EditTextSpaceDelimiter mPriceEditText = (EditTextSpaceDelimiter) root.findViewById(R.id.make_post_price_edit_text);

        //[END price input text listener]







        
        //Post type choosing spinner
        mPostTypeSpinner = (Spinner) root.findViewById(R.id.make_post_post_type_spinner);
        ArrayAdapter<CharSequence> post_type_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.make_post_posts_types, android.R.layout.simple_spinner_item);
        post_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPostTypeSpinner.setAdapter(post_type_adapter);

        //Room type choosing spinner
        mRoomTypeSpinner = (Spinner) root.findViewById(R.id.make_post_room_type_spinner);
        ArrayAdapter<CharSequence> room_type_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.make_post_rooms_types, android.R.layout.simple_spinner_item);
        room_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRoomTypeSpinner.setAdapter(room_type_adapter);

        mRoomTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String v = (String) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), v + " is choose", Toast.LENGTH_SHORT).show();

                if(v.equals("Terrain")){
                    //Toast.makeText(getContext(), v + " is hiding others", Toast.LENGTH_SHORT).show();
                    //Hide some FrameLayout
                    (root.findViewById(R.id.make_post_parameters_principal_room_frame)).setVisibility(View.GONE);
                    (root.findViewById(R.id.make_post_parameters_bedroom_frame)).setVisibility(View.GONE);
                }else {
                    (root.findViewById(R.id.make_post_parameters_principal_room_frame)).setVisibility(View.VISIBLE);
                    (root.findViewById(R.id.make_post_parameters_bedroom_frame)).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Cities type choosing spinner
        mCityChoosingSpinner = (Spinner) root.findViewById(R.id.make_post_city_spinner);
        ArrayAdapter<CharSequence> city_choose_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.make_post_cities, android.R.layout.simple_spinner_item);
        city_choose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCityChoosingSpinner.setAdapter(city_choose_adapter);

        //Cities type choosing spinner
        mDistrictChoosingSpinner = (Spinner) root.findViewById(R.id.make_post_district_type_spinner);
        ArrayAdapter<CharSequence> district_choose_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.make_post_district, android.R.layout.simple_spinner_item);
        district_choose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDistrictChoosingSpinner.setAdapter(district_choose_adapter);


        mPostButton = (Button) root.findViewById(R.id.make_post_post_button);
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        checkEditableView((EditText) root.findViewById(R.id.make_post_price_edit_text)) &&
                        checkEditableView((EditText) root.findViewById(R.id.make_post_parameters_edit_text_description)) &&
                                checkEditableView((EditText) root.findViewById(R.id.make_post_parameters_edit_text_bedroom_count)) &&
                                checkEditableView((EditText) root.findViewById(R.id.make_post_parameters_edit_text_principal_room_count)) &&
                                checkEditableView((EditText) root.findViewById(R.id.make_post_surface_edit_text))) {



                    //Consult credit amount here
                    //To know if we continue or abandon
                    ConfirmCreditBuyDialogFragment dialogFragment = ConfirmCreditBuyDialogFragment.newInstance(
                            "2 Crédits", "-"
                    );

                    //Updating the user amount will be done one success field


                    dialogFragment.setmListener(new ConfirmCreditBuyDialogFragment.ConfirmCreditBuyDialogFragmentInteractionListener() {
                        @Override
                        public void onContinueClicked(ConfirmCreditBuyDialogFragment fragment) {
                            //Check if we have the required amount

                            //Activate account cost 2 credits
                            if( mCurrentUser.getCredit_amount() - 2 > 0){
                                fragment.dismiss();
                                //We process transaction

                                //if updating data base succeed
                                //We upload



                                //Upload first images if success then upload the
                                //room information
                                //[START send image to firestore ]
                                mCountImageUploaded = 0;

                                //Make sure there is at least one image
                                boolean has_one_image_at_least = false;

                                for (int i = 0; i < 6 && !has_one_image_at_least; i++) {
                                    if( mImagesUri[i] != null){
                                        has_one_image_at_least = true;
                                    }
                                }

                                if(has_one_image_at_least) {

                                    if(
                                            ( (MainActivity)getActivity()).isNetworkConnected() ) {

                                        //The progress bar
                                        mMakePostProgressBar = new MakePostProgressDialogFragment();
                                        mMakePostProgressBar.setCancelable(false);

                                        //Block user intertaction
                                        mMakePostProgressBar.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");
                                        for (int i = 0; i < 6; i++) {
                                            Uri current_uri = mImagesUri[i];

                                            final int finalI = i;
                                            if (current_uri != null) {
                                                StorageReference mImageRef = mStorageReference.child(
                                                        System.currentTimeMillis() + "." + getExtension(current_uri)
                                                );

                                                mImageRef.putFile(current_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        //Update the user profile image

                                                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        String imagePath = uri.toString();
                                                                        mImagesUriString[finalI] = imagePath;

                                                                        Log.i("Upload image", mImagesUriString[finalI]);
                                                                        mCountImageUploaded += 1;
                                                                        listener.OnImageFilesUploaded(mCountImageUploaded, root);

                                                                    }
                                                                });
                                                    }
                                                });
                                            } else {
                                                mCountImageUploaded += 1;
                                                listener.OnImageFilesUploaded(mCountImageUploaded, root);
                                            }
                                        }
                                        //[END]
                                    }else{
                                        //[START We show internet error ]
                                        CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                                new CommonAskActivateInternetConnectionDialogFragment();

                                        activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                                        //[START We show internet error ]
                                    }


                                    //[Start making creating post here

                                    //[END]
                                    //Toast.makeText(getContext(), "Données correctes", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), "Au moins une image", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                //Showing alert for insufficient amount
                                fragment.dismiss();
                                Toast.makeText(getContext(), "Crédits insuffisants", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    dialogFragment.show(getActivity().getSupportFragmentManager(), "CONFIRM_ACTIVATION");


                }else {
                    Toast.makeText(getContext(), "Données incorrectes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }





    private String getExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    /**
     * Upload data to firebase
     * @param root
     */
    private void sendPostInformation(final View root){

        //[START updating the user amount after post]
        FirestoreNambooUserHelper.updateUserAmount(
                mCurrentUser.getCredit_amount() - 2,
                mCurrentUser.getUid()
                //[END updating the user amount after post]
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //[START after paid we post ]
                FirestoreNambooPostHelper.createPost(
                        //Post ypt
                        ((Spinner) (root.findViewById(R.id.make_post_post_type_spinner))).getSelectedItem().toString(),

                        //Price
                        //Remove space between numbers ! Space are allowed on typing data
                        //
                        //We call the inverse function at the other side(when showing post
                        //to convert int to string and set spacing
                        Integer.parseInt(
                                MakePostFragment.this.trimSpace(
                                        ((EditText) root.findViewById(R.id.make_post_price_edit_text) ).getText().toString()
                                ) ),

                        //Room type : Maison, Terrain etc
                        ((Spinner) (root.findViewById(R.id.make_post_room_type_spinner))).getSelectedItem().toString(),

                        //Surface
                        Integer.parseInt(
                                ((EditText) root.findViewById(R.id.make_post_surface_edit_text) ).getText().toString()),

                        //Principal room count
                        Integer.parseInt(
                                ((EditText) root.findViewById(R.id.make_post_parameters_edit_text_principal_room_count) ).getText().toString()),

                        //Bed room count
                        Integer.parseInt(
                                ((EditText) root.findViewById(R.id.make_post_parameters_edit_text_bedroom_count) ).getText().toString()),

                        //Description
                        ((EditText) root.findViewById(R.id.make_post_parameters_edit_text_description) ).getText().toString(),

                        //City
                        ((Spinner) (root.findViewById(R.id.make_post_city_spinner))).getSelectedItem().toString(),

                        //District
                        ((Spinner) (root.findViewById(R.id.make_post_district_type_spinner))).getSelectedItem().toString(),

                        mCurrentUser.getUid(),
                        joinStrings(mImagesUriString)

                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if(mMakePostProgressBar != null)
                            mMakePostProgressBar.dismiss();
                        Toast.makeText(getContext(), "Annonce postée avec succès", Toast.LENGTH_SHORT).show();

                        //Go back immediately
                        ((MainActivity)getActivity()).onBackPressed();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Post failed
                        if(mMakePostProgressBar != null)
                            mMakePostProgressBar.dismiss();
                        Toast.makeText(getContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    }
                });
                //[END after paid we post ]
            }
        });
        //[END updating the user amount after post] */

        /*FirestoreNambooPostHelper.createPost(
                //Post ypt
                ((Spinner) (root.findViewById(R.id.make_post_post_type_spinner))).getSelectedItem().toString(),

                //Price
                //Remove space between numbers ! Space are allowed on typing data
                //
                //We call the inverse function at the other side(when showing post
                //to convert int to string and set spacing
                Integer.parseInt(
                        MakePostFragment.this.trimSpace(
                            ((EditText) root.findViewById(R.id.make_post_price_edit_text) ).getText().toString()
                        ) ),

                //Room type : Maison, Terrain etc
                ((Spinner) (root.findViewById(R.id.make_post_room_type_spinner))).getSelectedItem().toString(),

                //Surface
                Integer.parseInt(
                        ((EditText) root.findViewById(R.id.make_post_surface_edit_text) ).getText().toString()),

                //Principal room count
                Integer.parseInt(
                        ((EditText) root.findViewById(R.id.make_post_parameters_edit_text_principal_room_count) ).getText().toString()),

                //Bed room count
                Integer.parseInt(
                        ((EditText) root.findViewById(R.id.make_post_parameters_edit_text_bedroom_count) ).getText().toString()),

                //Description
                ((EditText) root.findViewById(R.id.make_post_parameters_edit_text_description) ).getText().toString(),

                //City
                ((Spinner) (root.findViewById(R.id.make_post_city_spinner))).getSelectedItem().toString(),

                //District
                ((Spinner) (root.findViewById(R.id.make_post_district_type_spinner))).getSelectedItem().toString(),

                mCurrentUser.getUid(),
                joinStrings(mImagesUriString)

        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if(mMakePostProgressBar != null)
                   mMakePostProgressBar.dismiss();
                Toast.makeText(getContext(), "Annonce postée avec succès", Toast.LENGTH_SHORT).show();

                //Go back immediately
                ((MainActivity)getActivity()).onBackPressed();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Post failed
                if(mMakePostProgressBar != null)
                    mMakePostProgressBar.dismiss();
                Toast.makeText(getContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private String joinStrings(String[] strings){
        String delimiter = ";";
        String result = "", prefix = "";
        for (String s: strings) {
            result += prefix + s;
            prefix = delimiter;
        }
        return result;
    }


    /**
     * Check if the length in an editable text is valid
     * @param view
     * @return
     */
    private boolean checkEditableView(EditText view){
        if( (view.getText().toString()).length() > 0){
            return true;
        }
        return false;
    }

    /**
     * Remove all space in the string at beginning, at the end
     * and at middle.
     * @param s the entry string
     * @return a condensed string with no space in
     */
    private String trimSpace(String s){

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != PRICE_INPUT_DELIMITER.charAt(0) ){
                result.append(s.charAt(i));
            }
        }

        return result.substring(0);
    }

    /**
     * Open a files chooser for choosing images
     * for the post
     */
    private void showFileChooser() {
        try {
            Intent image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(image, FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "S'il vous plait installer un gestionnaire de fichier",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setLastImageGotUri(Uri image){
        this.mLastImageGot = image;
        Log.i("Choose File", image.toString());
    }
    private Uri getLastImageGotUri(){
        return mLastImageGot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri_image = null;
        if (requestCode == FILE_SELECT_CODE) {
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    uri_image = data.getData();
                    Log.i("Choosing image ", uri_image.toString());

                    mImagesChooser[mCurrentImageClickedId].setImageURI(uri_image);
                    mImagesUri[mCurrentImageClickedId] = uri_image;

                }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface OnMakePostFragmentInteractionListener {
        /**
         * Interface For click on photo image inorder to choose an image
         * @param mItem the image on which click was made
         * @param mImage
         */
        void  onAddImageClicked(
                AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem mItem,
                ImageView mImage, int position);
    }


    @Override
    public void onDestroy() {
        this.mLastImageGot = null;
        super.onDestroy();
    }
}
