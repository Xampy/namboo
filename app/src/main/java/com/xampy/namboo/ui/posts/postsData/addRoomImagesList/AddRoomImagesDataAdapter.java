package com.xampy.namboo.ui.posts.postsData.addRoomImagesList;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xampy.namboo.R;
import com.xampy.namboo.ui.oneRoomPresentation.OneRoomFragment;
import com.xampy.namboo.ui.posts.MakePostFragment;
import com.xampy.namboo.ui.posts.MakePostFragment;

import java.util.List;

public class AddRoomImagesDataAdapter extends RecyclerView.Adapter<AddRoomImagesDataAdapter.ViewHolder> {

    private final List<AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem> mValues;
    private final MakePostFragment.OnMakePostFragmentInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public AddRoomImagesDataAdapter(
            List<AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem> items,
            MakePostFragment.OnMakePostFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }


    @NonNull
    @Override
    public AddRoomImagesDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_add_room_data_image_item, parent, false);
        return new AddRoomImagesDataAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final AddRoomImagesDataAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        // holder.mImage.setText(mValues.get(position).mText);
        //holder.mIcon.setImageDrawable();;

        if (holder.mItem.mImageUri != null) {
            holder.mImage.setImageURI(holder.mItem.mImageUri);
            Log.i("Choosing Before", "Click on item " + position + holder.mItem.mImageUri.toString());
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    /*if (holder.mItem.mImageUri != null)
                        Log.i("Choosing", "Click on item" + position + holder.mItem.mImageUri.toString());

                    mListener.onAddImageClicked(holder.mItem, holder.mImage, position);
                    //notifyItemChanged(position);*/
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
        public AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.make_post_images_one);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
