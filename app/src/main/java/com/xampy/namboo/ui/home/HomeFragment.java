package com.xampy.namboo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooPostHelper;
import com.xampy.namboo.ui.home.homeData.bottomRoomsData.HomeBottomChambreAdapter;
import com.xampy.namboo.ui.home.homeData.bottomRoomsData.HomeBottomChambreFireStoreAdapter;
import com.xampy.namboo.ui.home.homeData.bottomRoomsData.HomeBottomMaisonFireStoreAdapter;
import com.xampy.namboo.ui.home.homeData.bottomRoomsData.HomeBottomTerrainFireStoreAdapter;
import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingAdapter;
import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingDummyContent;
import com.xampy.namboo.ui.home.homeData.uiRentingData.HomeLatestSellingFireStoreAdapter;
import com.xampy.namboo.ui.home.homeData.uiRentingData.HomeRentingAdapter;
import com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingAdapter;
import com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingDummyContent;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import static com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingDummyContent.CATEGORY_ITEMS;
import static com.xampy.namboo.ui.home.homeData.uiRentingData.HomeRentingDummyContent.HOME_RENTING_ITEMS;
import static com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingDummyContent.HOME_SELLING_ITEMS;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView mCategoriesScrolling;
    private RecyclerView mHomeSellingView;
    private RecyclerView mHomeRentingView;
    private CategoryHorizontalScrollingAdapter mCategoriesScrollingAdapter;
    private HomeSellingAdapter mHomeSellingAdapter;
    private HomeRentingAdapter mHomeRentingAdapter;

    private FrameLayout mMakePostFrame;
    private FrameLayout mSeeAllRentingFrame;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeBottomChambreAdapter mHomeBottomChambreAdapter;
    private RecyclerView mHomeBottomChambreView;
    private HomeBottomChambreFireStoreAdapter mHomeBottomChambreFirestoreAdapter;
    private HomeBottomMaisonFireStoreAdapter mHomeBottomMaisonFirestoreAdapter;
    private RecyclerView mHomeBottomMaisonView;
    private HomeBottomTerrainFireStoreAdapter mHomeBottomTerrainFirestoreAdapter;
    private RecyclerView mHomeBottomTerrainView;
    private FrameLayout mSeeAllSellingFrame;
    private HomeLatestSellingFireStoreAdapter mHomeSellingFirestoreAdapter;
    private HomeLatestSellingFireStoreAdapter mHomeRentingFirestoreAdapter;

    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mCategoriesScrollingAdapter = new CategoryHorizontalScrollingAdapter(CATEGORY_ITEMS, mListener, getContext());
        mCategoriesScrolling = root.findViewById(R.id.home_categories_list);
        mCategoriesScrolling.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mCategoriesScrolling.setAdapter(mCategoriesScrollingAdapter);


        (root.findViewById(R.id.home_get_research)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSearchAdvancedClicked();
            }
        });







        /*mHomeSellingAdapter = new HomeSellingAdapter(HOME_SELLING_ITEMS, mListener);
        mHomeSellingView = root.findViewById(R.id.home_selling_list);
        mHomeSellingView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mHomeSellingView.setAdapter(mHomeSellingAdapter);

        mHomeRentingAdapter = new HomeRentingAdapter(HOME_RENTING_ITEMS, 4);
        mHomeRentingView = root.findViewById(R.id.home_renting_list);
        mHomeRentingView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mHomeRentingView.setAdapter(mHomeRentingAdapter);*/



        mHomeSellingFirestoreAdapter = new HomeLatestSellingFireStoreAdapter(
                generateOptionsForAdapter(
                        FirestoreNambooPostHelper.getLatestBoostedSelling()
                ),
                Glide.with(this.getContext()),
                new HomeLatestSellingFireStoreAdapter.ChambresListener() {
                    @Override
                    public void onDataChanged() {
                        //
                    }
                },
                new HomeLatestSellingFireStoreAdapter.PostChambreInteractionListener() {
                    @Override
                    public void onChambreClicked(PostNambooFirestore post) {
                        mListener.onChambreClickedToOneRoom(post);
                    }
                },
                "",
                getContext()
        );

        mHomeSellingView = root.findViewById(R.id.home_selling_list);
        mHomeSellingView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mHomeSellingView.setAdapter(mHomeSellingFirestoreAdapter);


        mHomeRentingFirestoreAdapter = new HomeLatestSellingFireStoreAdapter(
                generateOptionsForAdapter(
                        FirestoreNambooPostHelper.getLatestBoostedRenting()
                ),
                Glide.with(this.getContext()),
                new HomeLatestSellingFireStoreAdapter.ChambresListener() {
                    @Override
                    public void onDataChanged() {
                        //
                    }
                },
                new HomeLatestSellingFireStoreAdapter.PostChambreInteractionListener() {
                    @Override
                    public void onChambreClicked(PostNambooFirestore post) {
                        mListener.onChambreClickedToOneRoom(post);
                    }
                },
                "",
                getContext()
        );

        mHomeRentingView = root.findViewById(R.id.home_renting_list);
        mHomeRentingView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mHomeRentingView.setAdapter(mHomeRentingFirestoreAdapter);



        //###########################################################################################


        mMakePostFrame = root.findViewById(R.id.home_make_post);
        mMakePostFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener){
                    mListener.onMakePostClicked();
                }
            }
        });


        mSeeAllRentingFrame = root.findViewById(R.id.home_renting_header_frame);
        mSeeAllRentingFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the many room fragment
                //to show all renting post
                //mListener.onSeeAllRentingRoomClicked();

                mListener.onClickToAllRentingRoom();
            }
        });
        
        mSeeAllSellingFrame = root.findViewById(R.id.home_selling_header_frame);
        mSeeAllSellingFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickToAllSellingRoom();
            }
        });




        //[START See more on room, home and land]
        (root.findViewById(R.id.see_more_chambre)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickSeeMore("Chambre");
            }
        });

        (root.findViewById(R.id.see_more_maison)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickSeeMore("Maison");
            }
        });

        (root.findViewById(R.id.see_more_terrain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickSeeMore("Terrain");
            }
        });
        //[END See more on room, home and land]




        //Recently post at the bottom
        /*mHomeBottomChambreAdapter = new HomeBottomChambreAdapter(
                HomeBottomChambreDummyContent.HOME_BOTTOM_CHAMBRE_DUMMY_ITEMS, 0, getContext());*/


        mHomeBottomChambreFirestoreAdapter = new HomeBottomChambreFireStoreAdapter(
                generateOptionsForAdapter(
                        FirestoreNambooPostHelper.getFourChambresForHome()
                ),
                Glide.with(this.getContext()),
                new HomeBottomChambreFireStoreAdapter.ChambresListener() {
                    @Override
                    public void onDataChanged() {
                        //
                    }
                },
                new HomeBottomChambreFireStoreAdapter.PostChambreInteractionListener() {
                    @Override
                    public void onChambreClicked(PostNambooFirestore post) {
                        mListener.onChambreClickedToOneRoom(post);
                    }
                },
                "",
                getContext()
        );

        mHomeBottomChambreView = (RecyclerView) root.findViewById(R.id.home_chambre_post_recent_list);
        mHomeBottomChambreView.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mHomeBottomChambreView.setAdapter( mHomeBottomChambreFirestoreAdapter);




        mHomeBottomMaisonFirestoreAdapter = new HomeBottomMaisonFireStoreAdapter(
                generateOptionsForAdapter(
                        FirestoreNambooPostHelper.getSomeMaisonsForHome()
                ),
                Glide.with(this.getContext()),
                new HomeBottomMaisonFireStoreAdapter.MaisonListener() {
                    @Override
                    public void onDataChanged() {

                    }
                },
                new HomeBottomMaisonFireStoreAdapter.PostMaisonInteractionListener() {
                    @Override
                    public void onMaisonClicked(PostNambooFirestore post) {
                        mListener.onMaisonClickedToOneRoom(post);
                    }
                },
                "",
                getContext()
        );

        mHomeBottomMaisonView = (RecyclerView) root.findViewById(R.id.home_maison_post_recent_list);
        mHomeBottomMaisonView.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mHomeBottomMaisonView.setAdapter( mHomeBottomMaisonFirestoreAdapter);





        mHomeBottomTerrainFirestoreAdapter = new HomeBottomTerrainFireStoreAdapter(
                generateOptionsForAdapter(
                        FirestoreNambooPostHelper.getSomeTerrainsForHome()
                ),
                Glide.with(this.getContext()),
                new HomeBottomTerrainFireStoreAdapter.TerrainListener() {
                    @Override
                    public void onDataChanged() {
                        //Display an empty message
                    }
                },
                new HomeBottomTerrainFireStoreAdapter.PostTerrainInteractionListener() {
                    @Override
                    public void onTerrainClicked(PostNambooFirestore post) {
                        mListener.onTerrainClickedToOneRoom(post);
                    }
                },
                "",
                getContext()
        );

        mHomeBottomTerrainView = (RecyclerView) root.findViewById(R.id.home_terrain_post_recent_list);
        mHomeBottomTerrainView.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mHomeBottomTerrainView.setAdapter( mHomeBottomTerrainFirestoreAdapter);

        return root;
    }

    private FirestoreRecyclerOptions<PostNambooFirestore> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<PostNambooFirestore>()
                .setQuery(query, PostNambooFirestore.class)
                .setLifecycleOwner(this)
                .build();
    }

    /**
     * Listener on the home fragment
     */
    public interface OnHomeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchAdvancedClicked();
        void onRoomDataClicked(HomeSellingDummyContent.HomeSellingDummyItem mItem);
        void onSeeAllRentingRoomClicked();
        void onCategoriesItemClicked(CategoryHorizontalScrollingDummyContent.CategoryHorizontalScrollingDummyItem cItem);
        void onClickSeeMore(String destination);
        void onMakePostClicked();
        void onChambreClickedToOneRoom(PostNambooFirestore post);
        void onMaisonClickedToOneRoom(PostNambooFirestore post);
        void onTerrainClickedToOneRoom(PostNambooFirestore post);

        void onClickToAllRentingRoom();
        void onClickToAllSellingRoom();
    }

    private  OnHomeFragmentInteractionListener mListener = new OnHomeFragmentInteractionListener() {
        @Override
        public void onSearchAdvancedClicked() {
            ( (MainActivity)getActivity() ).openSearchFragment();
        }

        @Override
        public void onRoomDataClicked(HomeSellingDummyContent.HomeSellingDummyItem mItem) {
            ( (MainActivity)getActivity() ).openOneRoomDataFragment();
        }

        @Override
        public void onSeeAllRentingRoomClicked() {
            ( (MainActivity)getActivity() ).openManyRoomDataFragment("Locations");
        }

        @Override
        public void onCategoriesItemClicked(CategoryHorizontalScrollingDummyContent.CategoryHorizontalScrollingDummyItem cItem) {
            if(cItem.mText != "categories")
                ( (MainActivity)getActivity() ).openManyRoomDataFragment(cItem.mText);
        }

        @Override
        public void onClickSeeMore(String destination) {
            ( (MainActivity)getActivity() ).openManyRoomDataFragment(destination);
        }

        @Override
        public void onMakePostClicked() {
            ( (MainActivity)getActivity() ).openMakePostFragment();
        }

        @Override
        public void onChambreClickedToOneRoom(PostNambooFirestore post) {
            //Open the fragment with post information
            ( (MainActivity)getActivity() ).openOneRoomDataFragment(post);
        }

        @Override
        public void onMaisonClickedToOneRoom(PostNambooFirestore post) {
            //Open the fragment with post information
            ( (MainActivity)getActivity() ).openOneRoomDataFragment(post);
        }

        @Override
        public void onTerrainClickedToOneRoom(PostNambooFirestore post) {
            //Open the fragment with post information
            ( (MainActivity)getActivity() ).openOneRoomDataFragment(post);
        }

        @Override
        public void onClickToAllRentingRoom() {
            ( (MainActivity)getActivity() ).openManyRoomDataFragment("Location(s)");
        }

        @Override
        public void onClickToAllSellingRoom() {
            ( (MainActivity)getActivity() ).openManyRoomDataFragment("Vente(s)");
        }
    };
}
