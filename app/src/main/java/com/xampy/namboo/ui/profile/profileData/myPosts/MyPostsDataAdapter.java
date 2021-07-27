package com.xampy.namboo.ui.profile.profileData.myPosts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xampy.namboo.R;
import com.xampy.namboo.ui.oneRoomPresentation.OneRoomFragment;

import java.util.List;

public class MyPostsDataAdapter extends RecyclerView.Adapter<MyPostsDataAdapter.ViewHolder> {

    private final List<MyPostsDataDummyContent.MyPostsDataDummyItem> mValues;
    private final OneRoomFragment.OnHomeOneRoomFragmentInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public MyPostsDataAdapter(
            List<MyPostsDataDummyContent.MyPostsDataDummyItem> items,
            OneRoomFragment.OnHomeOneRoomFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }


    @NonNull
    @Override
    public MyPostsDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile_my_posts_item, parent, false);
        return new MyPostsDataAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyPostsDataAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCategory.setText(mValues.get(position).mCategoryType);
        holder.mRoomType.setText(mValues.get(position).mRoomType);
        holder.mPosition.setText(mValues.get(position).mPosition);

        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRoomDataImageClicked(holder.mItem);
                }
            }
        });*/
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCategory;
        public final TextView mRoomType;
        public final TextView mPosition;

        public MyPostsDataDummyContent.MyPostsDataDummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCategory = (TextView) view.findViewById(R.id.my_posts_item_category);
            mRoomType = (TextView) view.findViewById(R.id.my_posts_item_room_type);
            mPosition = (TextView) view.findViewById(R.id.my_posts_item_position);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
