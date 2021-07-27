package com.xampy.namboo.ui.home.homeData.bottomRoomsData;

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
import com.xampy.namboo.tools.StringTools;

public class HomeBottomMaisonFireStoreAdapter extends FirestoreRecyclerAdapter<PostNambooFirestore, HomeBottomMaisonFireStoreAdapter.PostViewHolder> {

    private final Context mContext;
    private final RequestManager glide;
    private final MaisonListener callback;
    private final String uid;
    private final PostMaisonInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public interface MaisonListener {
        void onDataChanged();
    }

    public interface PostMaisonInteractionListener {
        void onMaisonClicked(PostNambooFirestore post);
    }


    public HomeBottomMaisonFireStoreAdapter(
            @NonNull FirestoreRecyclerOptions<PostNambooFirestore> options,
            RequestManager glide,
            MaisonListener callback, PostMaisonInteractionListener listener,
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
            @NonNull final HomeBottomMaisonFireStoreAdapter.PostViewHolder holder,
            int position,
            @NonNull PostNambooFirestore post) {

        holder.mPostItem = post;

        holder.mPrice.setText(
                String.format("%s Fcfa",
                        StringTools.matchLocalMoneyWriting( post.getmPrice() )
                )
        );

        holder.mPostType.setText(post.getmTypePost());
        String[] images = post.getImagesUrl().split(";", 6);

        boolean to_stop = false;
        for(String s: images){
            if (s.startsWith("http") && !to_stop) {
                to_stop = true;
                glide.load(Uri.parse(s)).into(holder.mImage);
            }
        }


        holder.mDistrictCity.setText(new StringBuilder().append(post.getmDistrict()).append("-").append(post.getmCity()).toString());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask opening the onRoom fragment
                if(mListener != null)
                    mListener.onMaisonClicked(holder.mPostItem);
            }
        });

    }


    @NonNull
    @Override
    public HomeBottomMaisonFireStoreAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new HomeBottomMaisonFireStoreAdapter.PostViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.fragment_home_custom_bottom_room_data_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public ImageView mImage;
        public TextView mPrice;
        public TextView mDistrictCity;
        public TextView mPostType;

        public PostNambooFirestore mPostItem;

        public PostViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.common_maison_image_view);
            mPrice = (TextView) view.findViewById(R.id.common_maison_text_view_price);
            mDistrictCity = (TextView) view.findViewById(R.id.common_maison_district_city_text_view);
            mPostType = (TextView) view.findViewById(R.id.common_maison_post_type_text_view);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mPrice.getText() + "'";
        }
    }

}
