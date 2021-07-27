package com.xampy.namboo.ui.manyRoomData.manyRoomData;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ManyRoomUsersServiceTypeFireStoreAdapter
        extends FirestoreRecyclerAdapter<UserNambooFirestore, ManyRoomUsersServiceTypeFireStoreAdapter.UserViewHolder> {

    private final Context mContext;
    private final RequestManager glide;
    private final UsersListener callback;
    private final String uid;
    LayoutInflater mLayoutInflater;

    public interface UsersListener {
        void onDataChanged();

    }

   public interface ManyUsersByServiceInteractionListener {
        void onCallClicked(String call_number);
        void onVisitClicked(String coords);
       void onWhatsappMessagerClicked(String phoneNumber);
   }

   private ManyUsersByServiceInteractionListener mListener;


    public ManyRoomUsersServiceTypeFireStoreAdapter(
            @NonNull FirestoreRecyclerOptions<UserNambooFirestore> options,
            RequestManager glide,
            UsersListener callback,  ManyUsersByServiceInteractionListener listener,
            String uid,
            Context context) {

        super(options);

        this.glide = glide;
        this.callback = callback;
        this.uid = uid;

        this.mContext = context;

        this.mListener = listener;
    }

    @Override
    protected void onBindViewHolder(
            @NonNull final ManyRoomUsersServiceTypeFireStoreAdapter.UserViewHolder holder,
            int position,
            @NonNull UserNambooFirestore user) {

        holder.mUserItem = user;
        Log.i("Got service", user.getUsername());
        holder.mUserName.setText(user.getUsername());
        holder.mUserService.setText(user.getServiceType());
        //holder.mUserCall.setText(user.getPhoneNumber());

        holder.mUserLocation.setText(user.getCity() + "-" + user.getDistrict());

        String image = user.getUrlPicture();

        if (image.startsWith("http")) {
            glide.load(Uri.parse(image)).into(holder.mImage);
        }

        holder.mUserCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask opening the onRoom fragment
                if(mListener != null)
                   mListener.onCallClicked(holder.mUserItem.getPhoneNumber());
            }
        });


        //Check that the poster has allowed his position
        if(holder.mUserItem.getPosition().length() < 3){
            //Sure that it contains maps coords
            holder.mUserVisit.setVisibility(View.INVISIBLE);
        }

        holder.mUserVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the google map intent here
                if(mListener != null){
                    mListener.onVisitClicked(holder.mUserItem.getPosition());
                }
            }
        });

        holder.mUserWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onWhatsappMessagerClicked(holder.mUserItem.getPhoneNumber());
            }
        });

    }


    @NonNull
    @Override
    public ManyRoomUsersServiceTypeFireStoreAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ManyRoomUsersServiceTypeFireStoreAdapter.UserViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.fragment_many_room_user_services_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public ImageView mImage;
        public TextView mUserName;
        public ImageView mUserWhatsapp;
        public ImageView mUserCall;
        public ImageView mUserVisit;
        public TextView mUserLocation;
        public TextView mUserService;


        public UserNambooFirestore mUserItem;

        public UserViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.services_user_item_image_view);
            mUserName = (TextView) view.findViewById(R.id.services_user_item_name_text_view);
            mUserWhatsapp = (ImageView) view.findViewById(R.id.services_user_item_whatsapp_image_view);
            mUserCall = (ImageView) view.findViewById(R.id.services_user_item_call_image_view);
            mUserVisit = (ImageView) view.findViewById(R.id.services_user_item_go_image_view);
            mUserLocation = (TextView) view.findViewById(R.id.services_user_item_location_text_view);
            mUserService = (TextView) view.findViewById(R.id.services_user_item_service_text_view);

        }
        @Override
        public String toString() {
            return super.toString() + " '" + mUserName.getText() + "'";
        }
    }

}
