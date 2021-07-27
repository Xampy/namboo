package com.xampy.namboo.ui.commonDialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.xampy.namboo.R;

public class CommonUploadingProgressDialogFragment extends DialogFragment {

    private ProgressBar mProgressBar;
    private TextView mProgressText;
    private View mView;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_make_post_progress, null);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.make_post_progress_bar);
        mProgressText = (TextView)  mView.findViewById(R.id.make_post_progress_text_view);


        builder.setView(mView);
        return builder.create();
    }
}
