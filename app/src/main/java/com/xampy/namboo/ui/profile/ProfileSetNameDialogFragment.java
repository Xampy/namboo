package com.xampy.namboo.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.namboo.R;

public class ProfileSetNameDialogFragment extends DialogFragment {

    private EditText editTextName;

    /**
     * Listener on the save button
     * fo updating the username
     */
    public interface NambooProfileNameSetterDialogListener {
        public void onNambooProfileNameSetterSaveButtonClicked(DialogFragment fragment);
    }

    private NambooProfileNameSetterDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_profile_name_setter, null);

        this.editTextName = (EditText) v.findViewById(R.id.dialog_profile_setter_name_edit_text);

        builder.setView(v).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNambooProfileNameSetterSaveButtonClicked(ProfileSetNameDialogFragment.this);
            }
        });

        return builder.create();
    }

    /**
     * Get the text entered by the user
     * @return
     */
    public String getEditTextNameValue() {
        return String.valueOf(editTextName.getText());
    }

    public void setListener(NambooProfileNameSetterDialogListener listener){
        this.mListener = listener;
    }
}
