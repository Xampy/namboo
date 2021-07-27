package com.xampy.namboo.ui.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.namboo.R;
import com.xampy.namboo.MainActivity;

import static com.xampy.namboo.MainActivity.mCurrentUser;

public class ProfileSetPasswordDialogFragment extends DialogFragment{

    private Spinner editTextName;
    private Spinner mStatusChoosingSpinner;
    private EditText mTextOld;
    private EditText mTextNew;
    private  EditText mTextNewConfirm;

    /**
     * Listener on the save button
     * fo updating the username
     */
    public interface NambooProfilePasswordSetterDialogListener  {
        public void onNambooProfilePasswordSetterSaveButtonClicked(DialogFragment fragment);
        void ononNambooProfilePasswordSetterUpdated(final String new_pass);
    }

    private  NambooProfilePasswordSetterDialogListener  mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_profile_password_setter, null);


       mTextOld = (EditText) v.findViewById(R.id.dialog_profile_setter_pass_old_edit_text);
       mTextNew = (EditText) v.findViewById(R.id.dialog_profile_setter_pass_new__edit_text);
       mTextNewConfirm = (EditText) v.findViewById(R.id.dialog_profile_setter_pass_new_confirm__edit_text);

        if(MainActivity.mCurrentUser.getPassword().equals("@none")){
            mTextOld.setVisibility(View.GONE);
            ( v.findViewById(R.id.dialog_profile_setter_pass_old_title)).setVisibility(View.GONE);
        }

        builder.setView(v).setPositiveButton(R.string.save_text,null  /*new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNambooProfilePasswordSetterSaveButtonClicked(ProfileSetPasswordDialogFragment.this);
            }
        }*/).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProfileSetPasswordDialogFragment.this.dismiss();
            }
        });

        final AlertDialog _dialog = builder.create();
        _dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //Got the positive
                _dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String old = mTextOld.getText().toString();
                        String _new = mTextNew.getText().toString();
                        String _newConfirm = mTextNewConfirm.getText().toString();
                        Log.i("GOT PASSWORD", _new + " " + MainActivity.mCurrentUser.getPassword());

                        if(MainActivity.mCurrentUser.getPassword().equals("@none")){
                            if( _new.length() == 4 ) {
                                if(_newConfirm.length() > 0) {
                                    if (_new.equals(_newConfirm)) {
                                        //Provoke listener
                                        //Call parent to update the password of a new time
                                        mListener.ononNambooProfilePasswordSetterUpdated(_newConfirm);
                                        dismiss();

                                    } else {
                                        //Error occured
                                        mTextNewConfirm.setError("confirmation incorrecte");
                                        mTextNewConfirm.requestFocus();

                                    }
                                }else {
                                    mTextNewConfirm.setError("confirmation incorrecte");
                                    mTextNewConfirm.requestFocus();
                                }
                            }else {
                                mTextNew.setError("Un PIN de 4 chiffres");
                                mTextNew.requestFocus();
                            }
                        }else {
                            if (MainActivity.mCurrentUser.getPassword().equals(old)) {
                                if (_new.length() == 4) {
                                    if (_newConfirm.length() > 0) {
                                        if (_new.equals(_newConfirm)) {
                                            //Provoke listener
                                            //Call parent to update the password of a new time
                                            mListener.ononNambooProfilePasswordSetterUpdated(_newConfirm);
                                            dismiss();

                                        } else {
                                            //Error occured
                                            mTextNewConfirm.setError("confirmation incorrecte");
                                            mTextNewConfirm.requestFocus();

                                        }
                                    } else {
                                        mTextNewConfirm.setError("confirmation incorrecte");
                                        mTextNewConfirm.requestFocus();
                                    }
                                } else {
                                    mTextNew.setError("Un PIN de 4 chiffres");
                                    mTextNew.requestFocus();
                                }
                            }else {
                                //Old password does'nt match
                                mTextOld.setError("Ancien mot de passe incorrect");
                                mTextOld.requestFocus();

                                mTextNew.setText("");
                                mTextNewConfirm.setText("");
                            }
                        }
                    }
                });
            }
        });
        return _dialog;
    }

    /**
     * Get the status choosed by the user
     * @return
     */
    public String getNewPasswordValue() {
        return mTextNew.getText().toString();
    }

    public void setListener(NambooProfilePasswordSetterDialogListener listener){
        this.mListener = listener;
    }
}
