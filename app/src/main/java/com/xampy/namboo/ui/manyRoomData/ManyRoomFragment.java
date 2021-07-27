package com.xampy.namboo.ui.manyRoomData;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooPostHelper;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooUserHelper;
import com.xampy.namboo.ui.commonDataAdapters.commonRoomAdapter.CommonMaisonAdapter;
import com.xampy.namboo.ui.commonDataAdapters.commonRoomAdapter.CommonMaisonDummyContent;
import com.xampy.namboo.ui.home.homeData.bottomRoomsData.HomeBottomMaisonFireStoreAdapter;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomChambreFireStoreAdapter;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomMaisonFireStoreAdapter;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomTerrainFireStoreAdapter;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomUsersServiceTypeFireStoreAdapter;
import com.xampy.namboo.ui.search.searchData.SearchDataModelRoom;
import com.xampy.namboo.ui.search.searchData.SearchDataModelService;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.xampy.namboo.MainActivity;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooPostHelper;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooUserHelper;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomChambreFireStoreAdapter;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomMaisonFireStoreAdapter;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomTerrainFireStoreAdapter;
import com.xampy.namboo.ui.manyRoomData.manyRoomData.ManyRoomUsersServiceTypeFireStoreAdapter;
import com.xampy.namboo.ui.search.searchData.SearchDataModelRoom;
import com.xampy.namboo.ui.search.searchData.SearchDataModelService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManyRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  ManyRoomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FRAGMENT_TITLE = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SEARCH_SERVICE_PARAMS = "param3";
    private static final String ARG_SEARCH_ROOM_PARAMS = "param4";
    private static final String ARG_SERVICE_USERS_STRING = "param5";

    // TODO: Rename and change types of parameters
    private String mFragmentTitle;
    private String mParam2;
    private RecyclerView mCommonDataView;
    private Object mCommonAdapter;
    private SearchDataModelService mSearchServiceDataModel;
    private SearchDataModelRoom mSearchRoomDataModel;
    private String mServicesUser;

    public interface ManyRoomFragmentInteractionListener {
        void onItemClicked(PostNambooFirestore post);

        void onUserCallClicked(String call_number);

        void onUserVisitClicked(String coords);

        void onUserWhatsappClicked(String phoneNumber);
    }

    public ManyRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManyRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManyRoomFragment newInstance(String param1, String param2) {
        ManyRoomFragment fragment = new ManyRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TITLE, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ManyRoomFragment newSearchInstance(String param1, SearchDataModelService model) {
        ManyRoomFragment fragment = new ManyRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TITLE, param1);
        args.putSerializable(ARG_SEARCH_SERVICE_PARAMS, model);
        fragment.setArguments(args);
        return fragment;
    }



    public static ManyRoomFragment newSearchServiceUserInstance(String param1, String user_service) {
        ManyRoomFragment fragment = new ManyRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TITLE, param1);
        args.putString(ARG_SERVICE_USERS_STRING, user_service);
        fragment.setArguments(args);
        return fragment;
    }

    public static ManyRoomFragment newSearchRoomInstance(String param1, SearchDataModelRoom model) {
        ManyRoomFragment fragment = new ManyRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TITLE, param1);
        args.putSerializable(ARG_SEARCH_ROOM_PARAMS, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFragmentTitle = getArguments().getString(ARG_FRAGMENT_TITLE);
            mParam2 = getArguments().getString(ARG_PARAM2);

            try {
                mServicesUser = getArguments().getString(ARG_SERVICE_USERS_STRING);
            }catch (Exception e){
                //Nothing to do
            }
            try {
                mSearchServiceDataModel = (SearchDataModelService) getArguments().getSerializable(ARG_SEARCH_SERVICE_PARAMS);
            }catch (Exception e){
                //Nothing to do
            }

            try {
                mSearchRoomDataModel = (SearchDataModelRoom) getArguments().getSerializable(ARG_SEARCH_ROOM_PARAMS);
            }catch (Exception e){
                //Nothing to do
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_many_room, container, false);
        TextView title = root.findViewById(R.id.many_room_title);
        title.setText(mFragmentTitle);

        //Switch fragment title to know which kind
        //common data to displays


        if (mFragmentTitle == "Maison") {
            mCommonAdapter = new ManyRoomMaisonFireStoreAdapter(
                    generateOptionsForAdapter(
                            FirestoreNambooPostHelper.getSomeMaisons()
                    ),
                    Glide.with(this.getContext()),
                    new ManyRoomMaisonFireStoreAdapter.TerrainListener() {
                        @Override
                        public void onDataChanged() {
                            //
                        }
                    },
                    new ManyRoomMaisonFireStoreAdapter.ManyMaisonInteractionListener() {
                        @Override
                        public void onClicked(PostNambooFirestore post) {
                            //Open open fragment
                            mListener.onItemClicked(post);
                        }
                    },
                    "",
                    getContext()
            );
            mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
            mCommonDataView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
        } else if (mFragmentTitle == "Terrain") {
            mCommonAdapter = new ManyRoomTerrainFireStoreAdapter(
                    generateOptionsForAdapter(
                            FirestoreNambooPostHelper.getSomeTerrains()
                    ),
                    Glide.with(this.getContext()),
                    new ManyRoomTerrainFireStoreAdapter.TerrainListener() {
                        @Override
                        public void onDataChanged() {

                        }
                    },
                    new ManyRoomTerrainFireStoreAdapter.ManyTerrainInteractionListener() {
                        @Override
                        public void onClicked(PostNambooFirestore post) {
                            //
                            mListener.onItemClicked(post);
                        }
                    },
                    "",
                    getContext()
            );
            mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
            mCommonDataView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
        } else if (mFragmentTitle == "Chambre") {
            mCommonAdapter = new ManyRoomChambreFireStoreAdapter(
                    generateOptionsForAdapter(
                            FirestoreNambooPostHelper.getSomeChambres()
                    ),
                    Glide.with(this.getContext()),
                    new ManyRoomChambreFireStoreAdapter.ChambreListener() {
                        @Override
                        public void onDataChanged() {

                        }
                    },
                    new ManyRoomChambreFireStoreAdapter.ManyChambreInteractionListener() {
                        @Override
                        public void onClicked(PostNambooFirestore post) {
                            //
                            mListener.onItemClicked(post);
                        }
                    },
                    "",
                    getContext()
            );
            mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
            mCommonDataView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
        } else if (mFragmentTitle == "Location(s)") {
            mCommonAdapter = new ManyRoomChambreFireStoreAdapter(
                    generateOptionsForAdapter(
                            FirestoreNambooPostHelper.getAllRenting()
                    ),
                    Glide.with(this.getContext()),
                    new ManyRoomChambreFireStoreAdapter.ChambreListener() {
                        @Override
                        public void onDataChanged() {

                        }
                    },
                    new ManyRoomChambreFireStoreAdapter.ManyChambreInteractionListener() {
                        @Override
                        public void onClicked(PostNambooFirestore post) {
                            //
                            mListener.onItemClicked(post);
                        }
                    },
                    "",
                    getContext()
            );
            mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
            mCommonDataView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
        } else if (mFragmentTitle.equals("Vente(s)")) {
            mCommonAdapter = new ManyRoomChambreFireStoreAdapter(
                    generateOptionsForAdapter(
                            FirestoreNambooPostHelper.getAllSelling()
                    ),
                    Glide.with(this.getContext()),
                    new ManyRoomChambreFireStoreAdapter.ChambreListener() {
                        @Override
                        public void onDataChanged() {

                        }
                    },
                    new ManyRoomChambreFireStoreAdapter.ManyChambreInteractionListener() {
                        @Override
                        public void onClicked(PostNambooFirestore post) {
                            //
                            mListener.onItemClicked(post);
                        }
                    },
                    "",
                    getContext()
            );
            mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
            mCommonDataView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
        } else if (mFragmentTitle.equals("Proposants Services")) {

            if(mServicesUser != null) {

                FirestoreRecyclerOptions<UserNambooFirestore> d = generateUsersOptionsForAdapter(
                        FirestoreNambooUserHelper.getUsersByServices((mServicesUser))
                );


                //Get parameters here as bundle
                mCommonAdapter = new ManyRoomUsersServiceTypeFireStoreAdapter(
                        d,
                        Glide.with(this.getContext()),
                        new ManyRoomUsersServiceTypeFireStoreAdapter.UsersListener() {
                            @Override
                            public void onDataChanged() {
                                //Nothing to do
                            }
                        },
                        new ManyRoomUsersServiceTypeFireStoreAdapter.ManyUsersByServiceInteractionListener() {
                            @Override
                            public void onCallClicked(String call_number) {
                                //Provoke call here
                                mListener.onUserCallClicked(call_number);
                            }

                            @Override
                            public void onVisitClicked(String coords) {
                                mListener.onUserVisitClicked(coords);
                            }

                            @Override
                            public void onWhatsappMessagerClicked(String phoneNumber) {
                                mListener.onUserWhatsappClicked(phoneNumber);
                            }
                        },
                        "",
                        getContext()
                );
                mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
                mCommonDataView.setLayoutManager(new LinearLayoutManager(getContext()));
                mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
            }
        }else if (mFragmentTitle.equals("Résultats recherches")) {

            if(mSearchServiceDataModel != null) {

                FirestoreRecyclerOptions<UserNambooFirestore> d = generateUsersOptionsForAdapter(
                        FirestoreNambooUserHelper.getSearchUsersByServicesParams(
                                mSearchServiceDataModel.getmServiceType(),
                                mSearchServiceDataModel.getmServiceCity(),
                                mSearchServiceDataModel.getmServiceDistrict()
                        )
                );


                //Get parameters here as bundle
                mCommonAdapter = new ManyRoomUsersServiceTypeFireStoreAdapter(
                        d,
                        Glide.with(this.getContext()),
                        new ManyRoomUsersServiceTypeFireStoreAdapter.UsersListener() {
                            @Override
                            public void onDataChanged() {
                                //Nothing to do
                            }
                        },
                        new ManyRoomUsersServiceTypeFireStoreAdapter.ManyUsersByServiceInteractionListener() {
                            @Override
                            public void onCallClicked(String call_number) {
                                //Provoke call here
                                mListener.onUserCallClicked(call_number);
                            }

                            @Override
                            public void onVisitClicked(String coords) {
                                mListener.onUserVisitClicked(coords);
                            }

                            @Override
                            public void onWhatsappMessagerClicked(String phoneNumber) {
                                mListener.onUserWhatsappClicked(phoneNumber);
                            }
                        },
                        "",
                        getContext()
                );
                mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
                mCommonDataView.setLayoutManager(new LinearLayoutManager(getContext()));
                mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
            }
        }else if (mFragmentTitle.equals("Résultats recherches - Espaces")) {

            if(mSearchRoomDataModel != null) {
                Log.i("REQUEST SPCAES",  mSearchRoomDataModel.getmPostType()+
                        mSearchRoomDataModel.getmRoomType()+
                        mSearchRoomDataModel.getmPriceMin()+
                        mSearchRoomDataModel.getmPriceMax()+
                        mSearchRoomDataModel.getmCity()+
                        mSearchRoomDataModel.getmDistrict());

                FirestoreRecyclerOptions<PostNambooFirestore> d = generateOptionsForAdapter(
                        FirestoreNambooPostHelper.getPostByParameters(
                                mSearchRoomDataModel.getmPostType(),
                                mSearchRoomDataModel.getmRoomType(),
                                mSearchRoomDataModel.getmPriceMin(),
                                mSearchRoomDataModel.getmPriceMax(),
                                mSearchRoomDataModel.getmCity(),
                                mSearchRoomDataModel.getmDistrict()
                        )
                );


                //Get parameters here as bundle
                mCommonAdapter = new ManyRoomChambreFireStoreAdapter(
                        d,
                        Glide.with(this.getContext()),
                        new ManyRoomChambreFireStoreAdapter.ChambreListener() {
                            @Override
                            public void onDataChanged() {

                            }
                        },
                        new ManyRoomChambreFireStoreAdapter.ManyChambreInteractionListener() {
                            @Override
                            public void onClicked(PostNambooFirestore post) {
                                //
                                mListener.onItemClicked(post);
                            }
                        },
                        "",
                        getContext()
                );
                mCommonDataView = (RecyclerView) root.findViewById(R.id.common_room_data_list);
                mCommonDataView.setLayoutManager(new LinearLayoutManager(getContext()));
                mCommonDataView.setAdapter((RecyclerView.Adapter) mCommonAdapter);
            }
        }

        //root.findViewById(R.id.many_room_app_default_bar).setBackgroundColor(Color.parseColor("#2BB72F"));

        return root;
    }

    private FirestoreRecyclerOptions<PostNambooFirestore> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<PostNambooFirestore>()
                .setQuery(query, PostNambooFirestore.class)
                .setLifecycleOwner(this)
                .build();
    }

    private FirestoreRecyclerOptions<UserNambooFirestore> generateUsersOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<UserNambooFirestore>()
                .setQuery(query, UserNambooFirestore.class)
                .setLifecycleOwner(this)
                .build();
    }


    private ManyRoomFragmentInteractionListener mListener = new ManyRoomFragmentInteractionListener() {
        @Override
        public void onItemClicked(PostNambooFirestore post) {
            ((MainActivity) getActivity()).openOneRoomDataFragment(post);
        }

        @Override
        public void onUserCallClicked(String call_number) {
            ((MainActivity) getActivity()).callPhone(call_number);
        }

        @Override
        public void onUserVisitClicked(String coords) {
            ((MainActivity) getActivity()).openGoogleMapFromCoords(coords);
        }

        @Override
        public void onUserWhatsappClicked(String phoneNumber) {
            ((MainActivity) getActivity()).openWhatsappFromNumber(phoneNumber);
        }
    };
}
