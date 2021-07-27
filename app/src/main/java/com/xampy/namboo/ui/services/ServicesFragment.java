package com.xampy.namboo.ui.services;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public  interface onServiceFragmentInteractionListener {
        void onServiceTypeClicked(String title);
    }

    public ServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_services, container, false);

        (root.findViewById(R.id.services_user_plomberie)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open fragment
               mListener.onServiceTypeClicked("Plombier");
            }
        });

        (root.findViewById(R.id.services_user_charpentier)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open fragment
                mListener.onServiceTypeClicked("Charpentier");
            }
        });

        (root.findViewById(R.id.services_user_electricien)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open fragment
                mListener.onServiceTypeClicked("Electricien");
            }
        });

        (root.findViewById(R.id.services_user_peinture)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open fragment
                mListener.onServiceTypeClicked("Peintre");
            }
        });

        (root.findViewById(R.id.services_user_interior_deco)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open fragment
                mListener.onServiceTypeClicked(getResources().getString(R.string.deco_int));
            }
        });

        (root.findViewById(R.id.services_user_menusier)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open fragment
                mListener.onServiceTypeClicked("Menusier");
            }
        });

        return root;
    }


    private onServiceFragmentInteractionListener mListener = new onServiceFragmentInteractionListener() {
        @Override
        public void onServiceTypeClicked(String title) {
            ( (MainActivity)getActivity() ).openSearchServiceUserManyRoomDataFragment(title);
        }
    };
}
