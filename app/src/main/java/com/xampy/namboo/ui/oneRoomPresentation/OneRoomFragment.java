package com.xampy.namboo.ui.oneRoomPresentation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xampy.namboo.MainActivity;
import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.xampy.namboo.api.firestoreNamboo.FirestoreNambooUserHelper;
import com.xampy.namboo.tools.StringTools;
import com.xampy.namboo.ui.oneRoomPresentation.oneRoomData.OneRoomTopImagesFireStoreAdapter;
import com.xampy.namboo.ui.oneRoomPresentation.oneRoomData.RoomDataImagesAdapter;
import com.xampy.namboo.ui.oneRoomPresentation.oneRoomData.RoomDataImagesDummyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OneRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneRoomFragment extends Fragment {

    private OnHomeOneRoomFragmentInteractionListener mListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_POST = "POST";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RoomDataImagesAdapter mOneRoomImagesAdapter;
    private RecyclerView mOneRoomImagesView;

    private PostNambooFirestore mItemPost;
    private OneRoomTopImagesFireStoreAdapter mOneRoomImagesFirestoreAdapter;

    public OneRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OneRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneRoomFragment newInstance(
            Serializable post_serialized,
            String param1, String param2
    ) {
        OneRoomFragment fragment = new OneRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        //Post the post to serializable
        args.putSerializable(ARG_POST, post_serialized);


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

            //gET THE post here
            mItemPost = (PostNambooFirestore) getArguments().getSerializable(ARG_POST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_one_room, container, false);


        if(mItemPost != null) {

            //Erase all data in the adapter
            RoomDataImagesDummyContent.ROOM_IMAGES_ITEMS.clear();

            //Split download url at 6 maximum
            String[] images = mItemPost.getImagesUrl().split(";", 6);

            //Set the adapter containers
            for (int i = 0; i < 6; i++) {
                RoomDataImagesDummyContent.addItem(
                        RoomDataImagesDummyContent.createDummyItem(
                                String.valueOf(i), images[i]));
            }

            mOneRoomImagesAdapter = new RoomDataImagesAdapter(RoomDataImagesDummyContent.ROOM_IMAGES_ITEMS,  Glide.with(this.getContext()), mListener);
            mOneRoomImagesView = (RecyclerView) root.findViewById(R.id.one_room_images_list);
            mOneRoomImagesView.setLayoutManager(new GridLayoutManager(getContext(), 6));
            mOneRoomImagesView.setAdapter(mOneRoomImagesAdapter);


            //[START get the sender user]
            (
                    FirestoreNambooUserHelper.getUser(mItemPost.getmUserSenderUid())
            ).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    final UserNambooFirestore user = task.getResult().toObject(UserNambooFirestore.class);

                    //Set phone number and name here
                    ((TextView) root.findViewById(R.id.one_room_sender_number))
                            .setText(user.getPhoneNumber());

                    ((TextView) root.findViewById(R.id.one_room_sender_service_type))
                            .setText(user.getServiceType());

                    //Set listener on phone call button
                    ( (ImageView) root.findViewById(R.id.one_room_call_image_view)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ( (MainActivity)getActivity() ).callPhone(user.getPhoneNumber());
                        }
                    });

                    ( (ImageView) root.findViewById(R.id.one_room_whatsapp_image_view)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ( (MainActivity)getActivity() ).openWhatsappFromNumber(user.getPhoneNumber());
                        }
                    });

                    ( (ImageView) root.findViewById(R.id.one_room_go_image_view)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String pos = user.getPosition();
                            if(pos.length() > 2)
                                ( (MainActivity)getActivity() ).openGoogleMapFromCoords(pos);
                            else
                                Toast.makeText(getContext(), "Pas de position mise pour l'annonce", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            //[START get the sender user]

            //[START setup post information]
            ((TextView) root.findViewById(R.id.one_room_post_type_room_type))
                    .setText(new StringBuilder().append(mItemPost.getmRoomType()).append(" ").append(mItemPost.getmTypePost()).toString());


            //Data is Integer
            //We converted it to String and
            //set spaces to match local money writing
            //
            ((TextView) root.findViewById(R.id.one_room_price_text_view))
                    .setText(
                            String.format("%s Fcfa", this.matchLocalMoneyWriting(mItemPost.getmPrice()))
                    );



            ((TextView) root.findViewById(R.id.one_room_position_text_view))
                    .setText(new StringBuilder().append(mItemPost.getmDistrict()).append(", ").append(mItemPost.getmCity()).toString());

            ((TextView) root.findViewById(R.id.one_room_bedroom_text_view))
                    .setText(String.valueOf(mItemPost.getmChambre()));

            ((TextView) root.findViewById(R.id.one_room_principal_room_text_view))
                    .setText(String.valueOf(mItemPost.getmSalon()));

            ((TextView) root.findViewById(R.id.one_room_description_text_view))
                    .setText(mItemPost.getmDescription());

            ((TextView) root.findViewById(R.id.one_room_surface_text_view))
                    .setText(String.valueOf(mItemPost.getmSurface()));

            //[END setup post information]
        }


        return root;
    }


    /**
     * Set spaces in the value writing in order
     * to match the local money writing
     * Ex; 1000 => 1 000
     * @param value the integer value
     * @return a formatted string
     */
    private String matchLocalMoneyWriting(int value){
        return StringTools.matchLocalMoneyWriting(value);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeOneRoomFragmentInteractionListener) {
            mListener = (OnHomeOneRoomFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implements OnHomeOneRoomFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Listener on the home fragment
     */
    public interface OnHomeOneRoomFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRoomDataImageClicked(RoomDataImagesDummyContent.RoomDataImagesDummyItem item);
    }
}
