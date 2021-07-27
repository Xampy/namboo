package com.xampy.namboo.ui.fonds;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.namboo.R;

public class ConfirmCreditBuyDialogFragment extends DialogFragment {


    private static final String ARG_VALUE_PARAM = "value";
    private static final String ARG_COUNT_PARAM = "count";

    private String mValue;
    private String mCount;



    public interface ConfirmCreditBuyDialogFragmentInteractionListener {
        void onContinueClicked(ConfirmCreditBuyDialogFragment fragment);
    }

    private ConfirmCreditBuyDialogFragmentInteractionListener mListener;




    public static ConfirmCreditBuyDialogFragment newInstance(String count, String value) {
        ConfirmCreditBuyDialogFragment fragment = new ConfirmCreditBuyDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COUNT_PARAM, count);
        args.putString(ARG_VALUE_PARAM, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mValue = getArguments().getString(ARG_VALUE_PARAM);
            mCount = getArguments().getString(ARG_COUNT_PARAM);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_payment_confirm_buy, null);

        ( (TextView) mView.findViewById(R.id.payment_confirm_buying_count_text_view)).setText(
                mCount
        );

        ( (TextView) mView.findViewById(R.id.payment_confirm_buying_value_text_view)).setText(
                mValue
        );

        ( (TextView) mView.findViewById(R.id.payment_confirm_buying_button_text_view))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Notify click on the continue button
                if(mListener != null)
                    mListener.onContinueClicked(ConfirmCreditBuyDialogFragment.this);

            }
        });



        builder.setView(mView);
        return builder.create();
    }


    public void setmListener(ConfirmCreditBuyDialogFragmentInteractionListener listener){
        mListener = listener;
    }
}
