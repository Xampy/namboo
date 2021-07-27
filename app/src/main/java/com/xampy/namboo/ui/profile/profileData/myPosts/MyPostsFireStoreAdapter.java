package com.xampy.namboo.ui.profile.profileData.myPosts;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.xampy.namboo.R;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyPostsFireStoreAdapter extends
        FirestoreRecyclerAdapter<PostNambooFirestore, MyPostsFireStoreAdapter.MyPostViewHolder> {

    private final RequestManager glide;
    private final MyPostListener callback;
    LayoutInflater mLayoutInflater;

    public interface MyPostListener {
        void onDataChanged();

        void onBoostMyPostClicked(final String uid, MyPostViewHolder holder);
        void onDeleteMyPost(String uid);

    }

   public interface PostTerrainInteractionListener {
        void onTerrainClicked(PostNambooFirestore post);
   }

   private PostTerrainInteractionListener mListener;


    public MyPostsFireStoreAdapter(
            @NonNull FirestoreRecyclerOptions<PostNambooFirestore> options,
            RequestManager glide,
            MyPostListener callback) {

        super(options);

        this.glide = glide;
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(
            @NonNull final MyPostsFireStoreAdapter.MyPostViewHolder holder,
            int position,
            @NonNull PostNambooFirestore post) {

        holder.mItem = post;

        String[] images = post.getImagesUrl().split(";", 6);

        boolean to_stop = false;
        for(String s: images){
            if(s != null)
                if (s.startsWith("http") && !to_stop) {
                    to_stop = true;
                    glide.load(Uri.parse(s)).into(holder.mImage);
                }
        }

        holder.mCategory.setText(post.getmTypePost());
        holder.mPosition.setText(
                new StringBuilder().append(post.getmDistrict()).append(" - ")
                        .append(post.getmCity()).toString());
        holder.mRoomType.setText(post.getmRoomType());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask opening the onRoom fragment
                //f(mListener != null)
                   // mListener.onTerrainClicked(holder.mPostItem);
            }
        });


        holder.mBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!holder.mItem.getmBoosted())
                    callback.onBoostMyPostClicked(holder.mItem.getmUid(), holder);
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDeleteMyPost(holder.mItem.getmUid());
            }
        });

    }


    @NonNull
    @Override
    public MyPostsFireStoreAdapter.MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new MyPostsFireStoreAdapter.MyPostViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.fragment_profile_my_posts_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }


    public class MyPostViewHolder extends RecyclerView.ViewHolder {
        public  View mView;

        public ImageView mImage;
        public final TextView mCategory;
        public final TextView mRoomType;
        public final TextView mPosition;
        public final TextView mDelete;
        public final TextView mBoost;

        public PostNambooFirestore mItem;

        public  MyPostViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.my_posts_item_image_view);
            mCategory = (TextView) view.findViewById(R.id.my_posts_item_category);
            mRoomType = (TextView) view.findViewById(R.id.my_posts_item_room_type);
            mPosition = (TextView) view.findViewById(R.id.my_posts_item_position);
            mDelete = (TextView) view.findViewById(R.id.my_posts_item_delete_text_view);
            mBoost = (TextView) view.findViewById(R.id.my_posts_item_boost_text_view);
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }

}
