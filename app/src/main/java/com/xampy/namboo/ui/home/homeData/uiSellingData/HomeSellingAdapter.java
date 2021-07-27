package com.xampy.namboo.ui.home.homeData.uiSellingData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xampy.namboo.R;
import com.xampy.namboo.ui.home.HomeFragment;
import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingAdapter;
import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingDummyContent;
import com.xampy.namboo.ui.home.HomeFragment;

import java.util.List;

public class HomeSellingAdapter extends RecyclerView.Adapter<HomeSellingAdapter.ViewHolder>{

    private final List<HomeSellingDummyContent.HomeSellingDummyItem> mValues;
    private final HomeFragment.OnHomeFragmentInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public HomeSellingAdapter(
            List<HomeSellingDummyContent.HomeSellingDummyItem> items,
            HomeFragment.OnHomeFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }


    @NonNull
    @Override
    public HomeSellingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_selling_item, parent, false);
        return new HomeSellingAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final HomeSellingAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
       // holder.mImage.setText(mValues.get(position).mText);
        //holder.mIcon.setImageDrawable();;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onRoomDataClicked(holder.mItem);
                }
            }
        });
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
        public final ImageView mImage;
        public HomeSellingDummyContent.HomeSellingDummyItem mItem;

        public ViewHolder(View view) {
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
