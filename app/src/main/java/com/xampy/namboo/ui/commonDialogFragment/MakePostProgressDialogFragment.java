package com.xampy.namboo.ui.commonDialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.xampy.namboo.R;
import com.xampy.namboo.ui.posts.MakePostFragment;

public class MakePostProgressDialogFragment extends DialogFragment {

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
