package com.xampy.namboo.ui.home.homeData.bottomRoomsData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xampy.namboo.R;

import java.util.List;

public class HomeBottomChambreAdapter extends RecyclerView.Adapter<HomeBottomChambreAdapter.ViewHolder>{
    private final List<HomeBottomChambreDummyContent.HomeBottomChambreDummyItem> mValues;
    private final Context mContext;
    //private final HomeFragment.OnHomeFragmentInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public HomeBottomChambreAdapter(
            List<HomeBottomChambreDummyContent.HomeBottomChambreDummyItem> items,
            int listener,
            Context context) {
        mValues = items;
        //mListener = listener;
        this.mContext = context;
    }


    @NonNull
    @Override
    public HomeBottomChambreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_common_maison_custom_item, parent, false);
        return new HomeBottomChambreAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeBottomChambreAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mText.setText(mValues.get(position).mText);

        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onCategoriesItemClicked(holder.mItem);
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
        public final ImageView mImage;
        public final TextView mPrice;
        public HomeBottomChambreDummyContent.HomeBottomChambreDummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.common_maison_image_view);
            mPrice = (TextView) view.findViewById(R.id.common_maison_text_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPrice.getText() + "'";
        }
    }
}
