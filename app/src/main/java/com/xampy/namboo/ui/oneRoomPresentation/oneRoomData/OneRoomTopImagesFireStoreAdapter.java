package com.xampy.namboo.ui.oneRoomPresentation.oneRoomData;

import android.content.Context;
import android.net.Uri;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OneRoomTopImagesFireStoreAdapter extends
        FirestoreRecyclerAdapter<String, OneRoomTopImagesFireStoreAdapter. OneRoomImageViewHolder> {

    private final RequestManager glide;
    private final TerrainListener callback;
    LayoutInflater mLayoutInflater;

    public interface TerrainListener {
        void onDataChanged();

    }

   public interface PostTerrainInteractionListener {
        void onTerrainClicked(PostNambooFirestore post);
   }

   private PostTerrainInteractionListener mListener;


    public OneRoomTopImagesFireStoreAdapter(
            @NonNull FirestoreRecyclerOptions<String> options,
            RequestManager glide,
            TerrainListener callback) {

        super(options);

        this.glide = glide;
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(
            @NonNull final OneRoomTopImagesFireStoreAdapter.OneRoomImageViewHolder holder,
            int position,
            @NonNull String post) {

        boolean to_stop = false;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask opening the onRoom fragment
                //f(mListener != null)
                   // mListener.onTerrainClicked(holder.mPostItem);
            }
        });

    }


    @NonNull
    @Override
    public OneRoomTopImagesFireStoreAdapter.OneRoomImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new OneRoomTopImagesFireStoreAdapter.OneRoomImageViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.fragment_home_custom_bottom_room_data_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }


    public class OneRoomImageViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public ImageView mImage;

        public PostNambooFirestore mPostItem;

        public  OneRoomImageViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.common_maison_image_view);
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }

}
