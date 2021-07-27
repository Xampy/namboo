package com.xampy.namboo.api.payement;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xampy.namboo.api.database.AppDataBaseManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.xampy.namboo.MainActivity.mCurrentUser;

public class PayGatePaymentAPI {

    public final static String PAYGATE_PAYMENT_URL = "https://paygateglobal.com/api/v1/pay";
    public final static String PAYGATE_PAYMENT_CHECK_URL = "https://paygateglobal.com/api/v1/status";
    public final static String PAYMENT_PAYGATE_TOGO_KEY = "98a3a78f-7847-4ed5-941a-501ba5d81a6c"; //"a0471f0f-99d4-495b-b3ab-496ba9b4d1d7";


    private static final String TX_REFERENCE_STRING = "tx_reference";
    private static final String STATUS_STRING = "status";
    private static final String IDENTIFIER_STRING = "identifier";
    private static final String PHONE_NUMBER_STRING = "phone_number";
    private static final String DATETIME_STRING = "datetime";
    private static final String PAYMENT_REFERENCE_STRING = "payment_reference";
    private static final String PAYMENT_METHOD_STRING = "payment_method";




    public interface PayGatePaymentAPIListener {
         void onTransactionInitializationHas_Success(boolean success_state);
         void onTransactionCompleteHas_Success(boolean success_state);
         void onTransactionCompleteHas_Cancelled();
        void onTransactionCompleteHas_OnGoing();
        void onTransactionCompleteHas_Expired();
         void onErrorOccurred();
    }

    private PayGatePaymentAPIListener mListener;



    private String mTx_Reference;
    private int mStatus;

    private static PayGatePaymentAPI mPayGatePaymentAPI;
    private  VolleyRequestQueueSingleton mVolleyRequestQueueSingleton;

    //By default no transaction on going
    private boolean mTransactionOnGoing = false;
   public static boolean mCheckingTransactionComplete = true;

    public void resetDataParameters(){
        mTx_Reference = null;
        mStatus = -1;
        mListener = null;
        mTransactionOnGoing = false;
        mCheckingTransactionComplete = true;
    }

    public PayGatePaymentAPI(VolleyRequestQueueSingleton volley) {
        this.mVolleyRequestQueueSingleton = volley;


        //Load the last reference data here
        String ref = mCurrentUser.getLast_transaction_reference();
        if(!ref.equals("@none")){
            mTx_Reference = ref;

            mCheckingTransactionComplete = false;
        }
    }


    public static synchronized PayGatePaymentAPI getInstance(VolleyRequestQueueSingleton volley){
        if(mPayGatePaymentAPI == null){
            mPayGatePaymentAPI = new PayGatePaymentAPI(volley);
        }

        return mPayGatePaymentAPI;
    }


    public void initializeTransaction(int price){

        JSONObject params = new JSONObject();

        try {

            params.put("auth_token", PAYMENT_PAYGATE_TOGO_KEY);
            params.put(PHONE_NUMBER_STRING, mCurrentUser.getTel());
            params.put("amount", price);  //Tranfering 200 Fcfa as test
            params.put("description", "Test avec xampy num");
            params.put("identifier", mCurrentUser.getTel() + "_" + System.currentTimeMillis());

            Log.i("PAYMENT REQ", params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                PAYGATE_PAYMENT_URL,
                params,
                new Response.Listener<JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Update the payment initialization status
                        //Initialization finished

                        try {
                            mTx_Reference = response.getString(TX_REFERENCE_STRING);
                            mStatus = response.getInt(STATUS_STRING);

                            //On success response is composed by ^tx_reference and @status
                            //Call Payment listener for checking status

                            Log.i("PAYMENT INFO", "ref + " + mTx_Reference + " status " + mStatus);

                            //Transaction is saved with success on paygate
                            if(mStatus == 0){
                                //Save the reference yo database
                                mCurrentUser.setLast_transaction_reference(mTx_Reference);
                                AppDataBaseManager.DB_USER_TABLE.updateUserLastTransaction(mCurrentUser);
                                Log.d("PAY TRANS REF", mCurrentUser.getLast_transaction_reference());


                                //Update success state
                                mTransactionOnGoing = true; //Under missed variable
                                mCheckingTransactionComplete = false;
                                if(mListener != null)
                                    mListener.onTransactionInitializationHas_Success(true);
                            }

                        } catch (JSONException e) {
                            if(mListener != null)
                                mListener.onErrorOccurred();
                            e.printStackTrace();
                        }
                    }
                },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       //Update success state
                       if(mListener != null)
                           mListener.onTransactionInitializationHas_Success(false);
                   }
               }
        );

        this.mVolleyRequestQueueSingleton.getRequestQueue().add(jsonObjectRequest);

    }


    public void getTransactionConfirmation(){

        //We are not  checking the state of a transaction
        //Then we can ask a new confirmation
        if(!mCheckingTransactionComplete) {
            JSONObject params = new JSONObject();

            try {
                params.put("auth_token", PAYMENT_PAYGATE_TOGO_KEY);
                params.put(TX_REFERENCE_STRING, mTx_Reference);

                Log.i("PAYMENT REQ CHECK", params.toString());
            } catch (JSONException e) {
                e.printStackTrace();

            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    PAYGATE_PAYMENT_CHECK_URL,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Update the payment initialization status
                            //Initialization finished

                            try {
                                String _mTx_Reference = response.getString(TX_REFERENCE_STRING);
                                String mIdentifier = response.getString(IDENTIFIER_STRING);
                                String mPay_Reference = response.getString(PAYMENT_REFERENCE_STRING);
                                int _mStatus = response.getInt(STATUS_STRING);
                                String mDate_Time = response.getString(DATETIME_STRING);
                                String mPay_Method = response.getString(PAYMENT_METHOD_STRING);

                                //On success response is composed by ^tx_reference and @status
                                //Call Payment listener for checking status
                                Log.i("PAYMENT REQ CHECK", "Staus => " + _mStatus);
                                String status_string = null;
                                if (_mStatus == 0) {
                                    //re init the last transaction value in data base
                                    //to default
                                    mCurrentUser.setLast_transaction_reference("@none");
                                    AppDataBaseManager.DB_USER_TABLE.updateUserLastTransaction(mCurrentUser);

                                    mTx_Reference = "";

                                    //Success payment confirmation success
                                    //We ve finished checking transaction
                                    mCheckingTransactionComplete = true;
                                    //Update success state
                                    if (mListener != null)
                                        mListener.onTransactionCompleteHas_Success(true);

                                    status_string = "sucsess";

                                } else if (_mStatus == 2) {
                                    //Success payment on going
                                    //Update success state
                                    if (mListener != null)
                                        mListener.onTransactionCompleteHas_OnGoing();

                                    status_string = "on going";

                                } else if (_mStatus == 4) {
                                    //Success payment confirmation expired
                                    //Update success state
                                    if (mListener != null)
                                        mListener.onTransactionCompleteHas_Expired();

                                    status_string = "expired";

                                } else if (_mStatus == 6) {
                                    //Success payment confirmation cancelled
                                    //Update success state
                                    if (mListener != null)
                                        mListener.onTransactionCompleteHas_Cancelled();

                                    status_string = "cancelled";
                                }

                                Log.d("PAYMENT REQ CHECK", "Status " + _mStatus + " " + status_string);



                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (mListener != null)
                                    mListener.onErrorOccurred();

                                //Error occurred when tracking the transaction state
                                mCheckingTransactionComplete = false;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Update success state
                            if (mListener != null)
                                mListener.onTransactionCompleteHas_Success(false);

                            //Error occurred when tracking the transaction state
                            mCheckingTransactionComplete = false;
                        }
                    }
            );

            this.mVolleyRequestQueueSingleton.getRequestQueue().add(jsonObjectRequest);
        }
    }

    public void setListener(PayGatePaymentAPIListener mListener) {
        this.mListener = mListener;
    }

    public void setTransactionOnGoingState(boolean mTransactionOnGoing) {
        this.mTransactionOnGoing = mTransactionOnGoing;
    }

    public String getmTx_Reference() {
        return mTx_Reference;
    }

    public boolean get_isTransactionOnGoingState(){
        return this.mTransactionOnGoing;
    }

}
