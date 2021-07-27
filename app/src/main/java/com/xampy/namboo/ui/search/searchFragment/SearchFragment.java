package com.xampy.namboo.ui.search.searchFragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.ui.search.searchData.SearchDataModelRoom;
import com.xampy.namboo.ui.search.searchData.SearchDataModelService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mGotToEspaceTextView;
    private TextView mGoToServicesTextView;
    private LinearLayout mServicesSeacrhl;
    private boolean mSearchService;
    private RadioGroup mSearchServicesTypeServices;
    private Spinner mSearchCitySpinner;
    private Spinner mSearchDistrictSpinner;
    private RadioGroup mSearchRoomPostType;
    private RadioGroup mSearchRoomRoomType;
    private EditText mPriceMax;
    private EditText mPriceMin;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        final View root = inflater.inflate(R.layout.fragment_search, container, false);

        //[START Switching frame for search]
        mGotToEspaceTextView = (TextView) root.findViewById(R.id.serach_group_spaces);
        mGoToServicesTextView = (TextView) root.findViewById(R.id.serach_group_services);


        mSearchService = false;


        mGotToEspaceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide and show
                (root.findViewById(R.id.search_services)).setVisibility(View.GONE);
                (root.findViewById(R.id.search_room)).setVisibility(View.VISIBLE);

                mSearchService = false;
            }
        });
        mGoToServicesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide and show
                (root.findViewById(R.id.search_room)).setVisibility(View.GONE);
                (root.findViewById(R.id.search_services)).setVisibility(View.VISIBLE);

                mSearchService = true;
            }
        });
        //[END Switching frame for search]

        //[START Spinner setting]
        mSearchCitySpinner = (Spinner) root.findViewById(R.id.search_city_spinner);
        mSearchDistrictSpinner = (Spinner) root.findViewById(R.id.search_district_spinner);

        ArrayAdapter<CharSequence> district_choose_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.make_post_district, android.R.layout.simple_spinner_item);
        district_choose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSearchDistrictSpinner.setAdapter(district_choose_adapter);

        ArrayAdapter<CharSequence> city_choose_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.make_post_cities, android.R.layout.simple_spinner_item);
        city_choose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSearchCitySpinner.setAdapter(city_choose_adapter);
        //[END Spinner setting]
        
        ( (TextView) root.findViewById(R.id.launch_search_text_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //[START of service type searching]
                if(mSearchService){
                    //Need to search for a service

                    //Check if the data are filled

                    mSearchServicesTypeServices = (RadioGroup) root.findViewById(R.id.search_services_types_radio_group);
                    if(mSearchServicesTypeServices.getCheckedRadioButtonId() == -1){
                        //Pass an error message
                        Toast.makeText(getContext(), "Choisissez un type de Service", Toast.LENGTH_SHORT).show();
                        mSearchServicesTypeServices.requestFocus();
                    }else {
                        
                        if(mSearchCitySpinner.getSelectedItem().toString().length() > 0 &&
                            mSearchDistrictSpinner.getSelectedItem().toString().length() > 0){

                            TextView t = null;
                            switch ( mSearchServicesTypeServices.getCheckedRadioButtonId()){
                                case R.id.search_service_type_charpentier_radioButton:
                                    t =  root.findViewById(R.id.search_serice_type_charpentier_text_view);
                                    break;
                                case R.id.search_service_type_macon_radioButton:
                                    t =  root.findViewById(R.id.search_serice_type_macon_text_view);
                                    break;
                                case R.id.search_service_type_plombier_radioButton:
                                    t =  root.findViewById(R.id.search_serice_type_plombier_text_view);
                                    break;
                                case R.id.search_service_type_menusier_radioButton:
                                    t =  root.findViewById(R.id.search_serice_type_menusier_text_view);
                                    break;
                                case R.id.search_service_type_electricien_radioButton:
                                    t =  root.findViewById(R.id.search_serice_type_electricien_text_view);
                                    break;
                                case R.id.search_service_type_peintre_radioButton:
                                    t =  root.findViewById(R.id.search_serice_type_peintre_text_view);
                                    break;
                                case R.id.search_service_type_deco_radioButton:
                                    t =  root.findViewById(R.id.search_serice_type_deco_text_view);
                                    break;
                            }

                            //All data are correct
                            if(t != null) {
                                SearchDataModelService service = new SearchDataModelService(

                                        t.getText().toString(),

                                        mSearchCitySpinner.getSelectedItem().toString(),

                                        mSearchDistrictSpinner.getSelectedItem().toString()
                                );

                                //Open many fragment with
                                //Toast.makeText(getContext(), "Data are correct", Toast.LENGTH_SHORT).show();



                                ( (MainActivity) getActivity()).openSearchResultServiceManyRoomDataFragment(
                                        "Résultats recherches",
                                        service
                                );
                            }
                        }

                    }
                }//[END of service type searching]
                else {
                    mSearchRoomPostType = (RadioGroup) root.findViewById(R.id.search_room_post_types_radio_group);
                    mSearchRoomRoomType = (RadioGroup) root.findViewById(R.id.search_room_room_types_radio_group);
                    if (mSearchRoomPostType.getCheckedRadioButtonId() == -1
                            || mSearchRoomRoomType.getCheckedRadioButtonId() == -1){
                        //Show error
                        Toast.makeText(getContext(), "Choisissez un post et une espace", Toast.LENGTH_SHORT).show();
                        mSearchRoomRoomType.requestFocus();
                    }else {

                        //Post ype, room type and location are correct

                        if(mSearchCitySpinner.getSelectedItem().toString().length() > 0 &&
                                mSearchDistrictSpinner.getSelectedItem().toString().length() > 0) {

                            TextView t1 = null;
                            switch (mSearchRoomPostType.getCheckedRadioButtonId()) {
                                case R.id.search_room_post_type_location_radioButton:
                                    t1 = root.findViewById(R.id.search_room_post_type_location_text_view);
                                    break;
                                case R.id.search_room_post_type_vente_radioButton:
                                    t1 = root.findViewById(R.id.search_room_post_type_vente_text_view);
                                    break;
                            }


                            TextView t2 = null;
                            switch (mSearchRoomRoomType .getCheckedRadioButtonId()) {
                                case R.id.search_room_room_type_chambre_radioButton:
                                    t2 = root.findViewById(R.id.search_room_room_type_chambre_text_view);
                                    break;
                                case R.id.search_room_room_type_maison_radioButton:
                                    t2 = root.findViewById(R.id.search_room_room_type_maison_text_view);
                                    break;
                                case R.id.search_room_room_type_terrain_radioButton:
                                    t2 = root.findViewById(R.id.search_room_room_type_terrain_text_view);
                                    break;
                            }

                            //Prices fields
                            mPriceMin = (EditText) root.findViewById(R.id.search_room_room_type_price_min_edit_text);
                            mPriceMax = (EditText) root.findViewById(R.id.search_room_room_type_price_max_edit_text);
                            String p_min = (mPriceMin.getText().toString().length() > 0) ? mPriceMin.getText().toString() : "0";
                            String p_max = (mPriceMax.getText().toString().length() > 0) ?  mPriceMax.getText().toString() : "0";

                            if(t1 != null && t2 != null){
                                SearchDataModelRoom model = new SearchDataModelRoom(
                                        t1.getText().toString(),
                                        t2.getText().toString(),
                                        Integer.parseInt(p_min),
                                        Integer.parseInt(p_max),
                                        mSearchCitySpinner.getSelectedItem().toString(),

                                        mSearchDistrictSpinner.getSelectedItem().toString()
                                );

                                Log.i("RESUEST POST",
                                        t1.getText().toString() +
                                        t2.getText().toString() +
                                        Integer.parseInt(p_min) +
                                        Integer.parseInt(p_max) +
                                        mSearchCitySpinner.getSelectedItem().toString() +

                                        mSearchDistrictSpinner.getSelectedItem().toString());

                                //

                                ( (MainActivity)getActivity() ).openSearchResultRoomManyRoomDataFragment(
                                        "Résultats recherches - Espaces",
                                        model
                                );
                            }
                        }else {
                            Toast.makeText(getContext(), "Choisissez une ville et quartier", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        return root;
    }
}
