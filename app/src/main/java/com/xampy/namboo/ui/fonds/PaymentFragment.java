package com.xampy.namboo.ui.fonds;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooUserHelper;
import com.xampy.namboo.api.payement.PayGatePaymentAPI;
import com.xampy.namboo.ui.commonDialogFragment.CommonAskActivateInternetConnectionDialogFragment;
import com.xampy.namboo.ui.commonDialogFragment.CommonUploadingProgressDialogFragment;
import com.xampy.namboo.ui.fonds.paymentData.buyCreditData.BuyCreditOptionsAdapter;
import com.xampy.namboo.ui.home.homeData.uiRentingData.HomeRentingAdapter;

import static com.xampy.namboo.MainActivity.mCurrentUser;
import static com.xampy.namboo.MainActivity.mPayGateAPI;
import static com.xampy.namboo.ui.fonds.paymentData.buyCreditData.BuyCreditOptionsDummyContent.BUY_CREDIT_OPTIONS_DUMMY_ITEMS;
import static com.xampy.namboo.ui.home.homeData.uiRentingData.HomeRentingDummyContent.HOME_RENTING_ITEMS;

public class PaymentFragment extends Fragment {

    private ConstraintLayout mCheckTransactionState;
    private LinearLayout mChoosingBuyingOptions;
    private TextView mCreditValue;
    private CommonUploadingProgressDialogFragment mProgressBar;
    private int mCreditToAdd;

    public interface OnPaymentFragmentInteractionListener {

        void onBuyCreditOptionsChoose(String count, String value);
    }

    private DashboardViewModel dashboardViewModel;
    private BuyCreditOptionsAdapter mBuyCreditOptionsAdapter;
    private RecyclerView mBuyCreditOptionsView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_payment, container, false);


        //On option choose how many namboo will be added
        mCreditToAdd = 0;


        //Cause of two call one form the bottom navigation
        //and one from a button on home fragment
        //We have to check also the login state form
        //main activity

        if(MainActivity.mUserAlreadyLogged){
            root.findViewById(R.id.payment_not_logged).setVisibility(View.GONE);
            root.findViewById(R.id.payment_already_logged).setVisibility(View.VISIBLE);
        }else {
            root.findViewById(R.id.payment_already_logged).setVisibility(View.GONE);
            root.findViewById(R.id.payment_not_logged).setVisibility(View.VISIBLE);
        }

        //[Start redirect to loin page with text view click]
        ((TextView)root.findViewById(R.id.payment_no_login_text_view_to_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the activity login fragment
                ((MainActivity) getActivity()).openLoginFragment();
            }
        });
        //[Start redirect to loin page with text view click]


        //[START set the user amount ]
        //Set the credit amount
        mCreditValue = (TextView) root.findViewById(R.id.payment_user_current_credits_value);
        if(mCurrentUser != null)
            mCreditValue.setText(
                    String.valueOf(mCurrentUser.getCredit_amount())
            );
        //[START set the user amount ]

        mBuyCreditOptionsAdapter = new BuyCreditOptionsAdapter(BUY_CREDIT_OPTIONS_DUMMY_ITEMS, mListener);
        mBuyCreditOptionsView = root.findViewById(R.id.payment_buy_credits_options_list);
        mBuyCreditOptionsView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        mBuyCreditOptionsView.setAdapter( mBuyCreditOptionsAdapter);




        //Hide the payment confirmation
        //frame from the user interaction
        mCheckTransactionState = (ConstraintLayout) root.findViewById(R.id.payment_confirm_action_check_cancel_constraint_layout);
        mChoosingBuyingOptions = (LinearLayout) root.findViewById(R.id.payment_buying_options);

        //If a transaction is on going
        //if true show the confirmation block else hide it
        if(mPayGateAPI.get_isTransactionOnGoingState() ||
                ( mPayGateAPI.getmTx_Reference() != null && !mPayGateAPI.getmTx_Reference().equals("@none")
                        && mPayGateAPI.getmTx_Reference().length() > 0)){
            //We got an transaction process
            //Hide buying options option
            mChoosingBuyingOptions.setVisibility(View.GONE);
            mCheckTransactionState.setVisibility(View.VISIBLE);

        }else {
            mCheckTransactionState.setVisibility(View.GONE);
            mChoosingBuyingOptions.setVisibility(View.VISIBLE);
        }

        //Handling click on verify and cancel
        ( (TextView) root.findViewById(R.id.payment_action_check)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Internet Connection checking
                if(
                        ( (MainActivity)getActivity()).isNetworkConnected() ) {

                    //Launch a payment verification
                    mProgressBar =
                            new CommonUploadingProgressDialogFragment();
                    mProgressBar.setCancelable(false);
                    mProgressBar.show(getActivity().getSupportFragmentManager(), "CHECKING_TRANSACT");
                    mPayGateAPI.setListener(mPayListener);
                    mPayGateAPI.getTransactionConfirmation();
                }else{
                    //We show error
                    CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                            new CommonAskActivateInternetConnectionDialogFragment();

                    activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                }
            }
        });


        ( (TextView) root.findViewById(R.id.payment_action_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset payment api data
                /*mPayGateAPI.resetDataParameters();

                mCheckTransactionState.setVisibility(View.GONE);
                mChoosingBuyingOptions.setVisibility(View.VISIBLE);*/
            }
        });



        return root;
    }

    private OnPaymentFragmentInteractionListener mListener = new OnPaymentFragmentInteractionListener() {
        @Override
        public void onBuyCreditOptionsChoose(String count, final String value) {

            //We want to add namboo
            mCreditToAdd =  Integer.parseInt( count.split(" ", 2)[0] );
            Log.d("ASK namboo", "Want " + mCreditToAdd);


            //Open confirmation fragment
            ConfirmCreditBuyDialogFragment dialogFragment = ConfirmCreditBuyDialogFragment.newInstance(
                    count,
                    value
            );

            Log.d("PAYMENT INFO", count + " value " + value);

            //Adding Listener to the dialog
            dialogFragment.setmListener(new ConfirmCreditBuyDialogFragment.ConfirmCreditBuyDialogFragmentInteractionListener() {
                @Override
                public void onContinueClicked(final ConfirmCreditBuyDialogFragment fragment) {

                    //Close the dialog
                    fragment.dismiss();
                    if(
                            ( (MainActivity)getActivity()).isNetworkConnected() ) {

                        //Open progress bar here
                        mProgressBar =
                                new CommonUploadingProgressDialogFragment();
                        mProgressBar.setCancelable(false);
                        mProgressBar.show(getActivity().getSupportFragmentManager(), "INIT_TRANSACTION");
                        mPayGateAPI.setListener(mPayListener);

                        //Launch initialisation
                        String[] splited = value.split(" ", 3);
                        String v = null;
                        if(splited.length > 2){
                            v = splited[0] + splited[1];
                        }else {
                            v = splited[0];
                        }
                        Log.d("ACOUNT INFO", v);
                        mPayGateAPI.initializeTransaction( Integer.parseInt( v ) );


                    }else{
                        //We show error
                        CommonAskActivateInternetConnectionDialogFragment activateNetwork =
                                new CommonAskActivateInternetConnectionDialogFragment();

                        activateNetwork.show(getActivity().getSupportFragmentManager(), "CHECK_INTERNET");
                    }

                }
            });
            dialogFragment.show(getActivity().getSupportFragmentManager(), "CONFIRM_BUY");
        }
    };



    private PayGatePaymentAPI.PayGatePaymentAPIListener mPayListener = new PayGatePaymentAPI.PayGatePaymentAPIListener() {
        @Override
        public void onTransactionInitializationHas_Success(boolean success_state) {

            if(mProgressBar != null)
                mProgressBar.dismiss();


            //Init new Paygate transaction
            if(success_state) {
                mChoosingBuyingOptions.setVisibility(View.GONE);
                mCheckTransactionState.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTransactionCompleteHas_Success(boolean success_state) {
            //Handle transaction complete event here

            //reset the paygate parameters here
            mPayGateAPI.resetDataParameters();

            //Launch amount updating amount here

            //[START updating the user credit amount
            if(mCreditToAdd != 0){
                int new_value = mCurrentUser.getCredit_amount() + mCreditToAdd;

                mCurrentUser.setCredit_amount( new_value );
                mCreditValue.setText(String.valueOf(  new_value  ));

                try {
                    FirestoreNambooUserHelper.updateUserAmount(
                            new_value, mCurrentUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Crédits ajoutés", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




            //Update the data amount count

            if(mProgressBar != null)
                mProgressBar.dismiss();

            if(success_state) {

                mCheckTransactionState.setVisibility(View.GONE);
                mChoosingBuyingOptions.setVisibility(View.VISIBLE);
            }


            Toast.makeText(getContext(), "TRANSACTION réussi", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTransactionCompleteHas_Cancelled() {
            if(mProgressBar != null)
                mProgressBar.dismiss();

            Toast.makeText(getContext(), "TRANSACTION annulée", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onTransactionCompleteHas_OnGoing() {
            if(mProgressBar != null)
                mProgressBar.dismiss();


            Toast.makeText(getContext(), "TRANSACTION en attente", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onTransactionCompleteHas_Expired() {
            //reset the paygate parameters here
            mPayGateAPI.resetDataParameters();

            if(mProgressBar != null)
                mProgressBar.dismiss();

            Toast.makeText(getContext(), "TRANSACTION en attente", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorOccurred() {
            //Close the dialog

            if(mProgressBar != null)
                mProgressBar.dismiss();

            Toast.makeText(getContext(), "Erreur survenue", Toast.LENGTH_SHORT).show();

        }
    };
}
