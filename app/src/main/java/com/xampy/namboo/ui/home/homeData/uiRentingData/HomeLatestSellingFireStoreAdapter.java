package com.xampy.namboo.ui.home.homeData.uiRentingData;

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

public class HomeLatestSellingFireStoreAdapter extends FirestoreRecyclerAdapter<PostNambooFirestore, HomeLatestSellingFireStoreAdapter.PostViewHolder> {

    private final Context mContext;
    private final RequestManager glide;
    private final ChambresListener callback;
    private final String uid;
    private final PostChambreInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public interface ChambresListener {
        void onDataChanged();
    }

    public interface PostChambreInteractionListener {
        void onChambreClicked(PostNambooFirestore post);
    }


    public HomeLatestSellingFireStoreAdapter(
            @NonNull FirestoreRecyclerOptions<PostNambooFirestore> options,
            RequestManager glide,
            ChambresListener callback, PostChambreInteractionListener listener,
            String uid,
            Context context) {

        super(options);

        this.glide = glide;
        this.callback = callback;
        this.uid = uid;
        this.mListener = listener;

        this.mContext = context;
    }

    @Override
    protected void onBindViewHolder(
            @NonNull final HomeLatestSellingFireStoreAdapter.PostViewHolder holder,
            int position,
            @NonNull PostNambooFirestore post) {

        holder.mPostItem = post;
        String[] images = post.getImagesUrl().split(";", 6);

        boolean to_stop = false;
        for(String s: images){
            if (s.startsWith("http") && !to_stop) {
                to_stop = true;
                glide.load(Uri.parse(s)).into(holder.mImage);
            }
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask opening the onRoom fragment
                if(mListener != null)
                    mListener.onChambreClicked(holder.mPostItem);
            }
        });

    }


    @NonNull
    @Override
    public HomeLatestSellingFireStoreAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new HomeLatestSellingFireStoreAdapter.PostViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.fragment_home_selling_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public ImageView mImage;

        public PostNambooFirestore mPostItem;

        public PostViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.home_selling_item_image);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
