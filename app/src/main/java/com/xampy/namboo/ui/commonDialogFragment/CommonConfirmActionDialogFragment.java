package com.xampy.namboo.ui.commonDialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.namboo.R;

public class CommonConfirmActionDialogFragment extends DialogFragment {


    public interface CommonConfirmActionDialogFragmentInteractionListener {
        void onYesClicked();
        void onNoClicked();
    }

    private CommonConfirmActionDialogFragmentInteractionListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Voulez vous exécuter cette action").setPositiveButton("oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    if(mListener != null)
                        mListener.onYesClicked();
                }
            }
        }).setNegativeButton("non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_NEGATIVE){
                    CommonConfirmActionDialogFragment.this.dismiss();
                    if(mListener != null)
                        mListener.onNoClicked();
                }
            }
        });
        return builder.create();
    }



    public void setListener(CommonConfirmActionDialogFragmentInteractionListener listener){
        mListener = listener;
    }
}
