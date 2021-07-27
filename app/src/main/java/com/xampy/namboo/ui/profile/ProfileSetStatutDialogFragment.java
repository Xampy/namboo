package com.xampy.namboo.ui.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.namboo.R;

public class ProfileSetStatutDialogFragment extends DialogFragment{

    private Spinner editTextName;
    private Spinner mStatusChoosingSpinner;

    /**
     * Listener on the save button
     * fo updating the username
     */
    public interface NambooProfileStatutSetterDialogListener  {
        public void onNambooProfileStatusSetterSaveButtonClicked(DialogFragment fragment);
    }

    private NambooProfileStatutSetterDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_profile_status_setter, null);


        mStatusChoosingSpinner = (Spinner) v.findViewById(R.id.dialog_profile_setter_statut_spinner);
        ArrayAdapter<CharSequence> status_choose_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.profile_set_status_choice, android.R.layout.simple_spinner_item);
        status_choose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatusChoosingSpinner.setAdapter(status_choose_adapter);

        builder.setView(v).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNambooProfileStatusSetterSaveButtonClicked(ProfileSetStatutDialogFragment.this);
            }
        });

        return builder.create();
    }

    /**
     * Get the status choosed by the user
     * @return
     */
    public String getSpinnerChoiceValue() {
        return String.valueOf(mStatusChoosingSpinner.getSelectedItem());
    }

    public void setListener(NambooProfileStatutSetterDialogListener listener){
        this.mListener = listener;
    }
}
