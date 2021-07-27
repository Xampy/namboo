package com.xampy.namboo.ui.home.homeData.uiCategoryData;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xampy.namboo.R;
import com.xampy.namboo.ui.home.HomeFragment;
import com.xampy.namboo.ui.home.HomeFragment;

import java.util.List;


public class CategoryHorizontalScrollingAdapter extends RecyclerView.Adapter<CategoryHorizontalScrollingAdapter.ViewHolder>{

    private final List<CategoryHorizontalScrollingDummyContent.CategoryHorizontalScrollingDummyItem> mValues;
    private final Context mContext;
    private final HomeFragment.OnHomeFragmentInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public CategoryHorizontalScrollingAdapter(
            List<CategoryHorizontalScrollingDummyContent.CategoryHorizontalScrollingDummyItem> items,
            HomeFragment.OnHomeFragmentInteractionListener listener,
            Context context) {
        mValues = items;
        mListener = listener;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_custom_categories_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mText.setText(mValues.get(position).mText);

        holder.mIcon.setImageDrawable(
                mContext.getResources().getDrawable(
                        mContext.getResources().
                            getIdentifier(
                                    mValues.get(position).mIconName,
                                    "drawable",
                                    mContext.getPackageName()
                            )
                )
        );

        holder.mIcon.setBackground(
                mContext.getResources().getDrawable(
                        mContext.getResources().
                                getIdentifier(
                                        mValues.get(position).mIconBack,
                                        "drawable",
                                        mContext.getPackageName()
                                )
                )
        );

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onCategoriesItemClicked(holder.mItem);
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
        public final TextView mText;
        public final ImageView mIcon;
        public CategoryHorizontalScrollingDummyContent.CategoryHorizontalScrollingDummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mText = (TextView) view.findViewById(R.id.home_categories_text);
            mIcon = (ImageView) view.findViewById(R.id.home_categories_img);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mText.getText() + "'";
        }
    }
}
