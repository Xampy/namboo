package com.xampy.namboo.ui.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
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
import com.xampy.namboo.ui.commonDialogFragment.CommonUploadingProgressDialogFragment;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner mCountryChoosingSpinner;
    private EditText mPhoneNumber;
    private EditText mSmsCodeEditText;


    //AUTHENTIFICATION
    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private OnRegisterInteractionListener mListener;
    private String mPhoneNumberString;
    private TextView mGetCodeText;
    private CommonUploadingProgressDialogFragment mRegisterProgressState;
    private long mPhoneVerificationStartTime;


    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
        }

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumber.setError("téléphone invalide");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(getContext(), "Erreur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        // [END phone_auth_callbacks]
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity() , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(getContext(), "Authentification réussi", Toast.LENGTH_SHORT).show();

                            //Notify the main activity that
                            //Authentication succeeds
                            mRegisterProgressState.dismiss();
                            mListener.onRegistrationSuccessful(user, "+228" + mPhoneNumberString);

                            //Close the current register fragment
                            //( getActivity() ).onBackPressed();
                            //( (MainActivity) getActivity() ).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);



                        } else {
                            mRegisterProgressState.dismiss();
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mSmsCodeEditText.setError("Invalid code.");

                            }
                        }
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        //Cities type choosing spinner
        mCountryChoosingSpinner = (Spinner) root.findViewById(R.id.register_country_spinner);
        ArrayAdapter<CharSequence> countryt_choose_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.register_countries_spinner, android.R.layout.simple_spinner_item);
        countryt_choose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountryChoosingSpinner.setAdapter(countryt_choose_adapter);

        //Phone
        mPhoneNumber = (EditText) root.findViewById(R.id.register_phone_mobile_edit_text);
        mGetCodeText = (TextView) root.findViewById(R.id.register_get_code_text_view);

        //Get code listener
        final TextView get_code = (TextView) root.findViewById(R.id.register_get_code_text_view);
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the phone number is empty
                String phone = mPhoneNumber.getText().toString().trim();
                if (!validatePhoneNumber()) {
                    mPhoneNumber.setError("");
                    mPhoneNumber.requestFocus();
                    return;
                }else {
                    mGetCodeText.setEnabled(false);   //Disable the get code btn
                    //Check and sign a user
                    mPhoneNumberString = phone;
                    try {
                        startPhoneNumberVerification("+228" + mPhoneNumber.getText().toString());
                        mPhoneVerificationStartTime = System.currentTimeMillis();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while (System.currentTimeMillis() - mPhoneVerificationStartTime < 90000);


                            }
                        }).start();

                        mRegisterProgressState = new CommonUploadingProgressDialogFragment();
                        mRegisterProgressState.setCancelable(false);
                        mRegisterProgressState.show(((MainActivity) getActivity()).getSupportFragmentManager(), "UPLOAD_PROGRESS");
                    }catch (Exception e){
                        mRegisterProgressState.dismiss();
                        Log.d("Registeering Namboo", e.toString());
                        Toast.makeText(getContext(), "Erreur survenue...", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        return root;
    }

    /**
     * Check if the phone number entered is correct
     * or not
     * @return  true if corect
     */
    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumber.getText().toString();
        if(phoneNumber.length() == 8){
            if(TextUtils.isDigitsOnly(phoneNumber)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterInteractionListener) {
            mListener = (OnRegisterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRegisterInteractionListener {
        void onRegistrationSuccessful(FirebaseUser firebaseUser, String phoneNumber);
    }


}