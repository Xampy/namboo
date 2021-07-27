package com.xampy.namboo.ui.oneRoomPresentation.oneRoomData;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.xampy.namboo.R;
import com.xampy.namboo.ui.home.HomeFragment;
import com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingAdapter;
import com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingDummyContent;
import com.xampy.namboo.ui.oneRoomPresentation.OneRoomFragment;

import java.util.List;

public class RoomDataImagesAdapter extends RecyclerView.Adapter<RoomDataImagesAdapter.ViewHolder>{

    private final List<RoomDataImagesDummyContent.RoomDataImagesDummyItem> mValues;
    private final OneRoomFragment.OnHomeOneRoomFragmentInteractionListener mListener;
    private final RequestManager mGlide;
    LayoutInflater mLayoutInflater;

    public RoomDataImagesAdapter(
            List<RoomDataImagesDummyContent.RoomDataImagesDummyItem> items,
            RequestManager glide,
            OneRoomFragment.OnHomeOneRoomFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.mGlide = glide;
    }


    @NonNull
    @Override
    public RoomDataImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_one_room_data_image_item, parent, false);
        return new RoomDataImagesAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RoomDataImagesAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem.mImage.startsWith("http")) {
            this.mGlide.load(Uri.parse(holder.mItem.mImage)).into(holder.mImage);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRoomDataImageClicked(holder.mItem);
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
        public RoomDataImagesDummyContent.RoomDataImagesDummyItem mItem;

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
