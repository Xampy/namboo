package com.xampy.namboo.ui.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.namboo.R;

public class ProfileSetCityDialogFragment extends DialogFragment{

    private Spinner editTextName;
    private Spinner mStatusChoosingSpinner;
    private Spinner mCityChoosingSpinner;

    /**
     * Listener on the save button
     * fo updating the username
     */
    public interface NambooProfileCitySetterDialogListener  {
        public void onNambooProfileCitySetterSaveButtonClicked(DialogFragment fragment);
    }

    private NambooProfileCitySetterDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_profile_city_setter, null);


        mCityChoosingSpinner = (Spinner) v.findViewById(R.id.dialog_profile_setter_city_spinner);
        ArrayAdapter<CharSequence> status_choose_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.make_post_cities, android.R.layout.simple_spinner_item);
        status_choose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCityChoosingSpinner.setAdapter(status_choose_adapter);

        builder.setView(v).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNambooProfileCitySetterSaveButtonClicked(ProfileSetCityDialogFragment.this);
            }
        });

        return builder.create();
    }

    /**
     * Get the status choosed by the user
     * @return
     */
    public String getSpinnerChoiceValue() {
        return String.valueOf(mCityChoosingSpinner.getSelectedItem());
    }

    public void setListener(NambooProfileCitySetterDialogListener listener){
        this.mListener = listener;
    }
}
