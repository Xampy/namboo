package com.xampy.namboo.ui.fonds.paymentData.buyCreditData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xampy.namboo.R;
import com.xampy.namboo.ui.fonds.PaymentFragment;
import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingAdapter;
import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingDummyContent;

import java.util.List;

public class BuyCreditOptionsAdapter extends RecyclerView.Adapter<BuyCreditOptionsAdapter.ViewHolder> {

    private final List<BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem> mValues;
    private  PaymentFragment.OnPaymentFragmentInteractionListener mListener;
    LayoutInflater mLayoutInflater;

    public BuyCreditOptionsAdapter(
            List<BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem> items,
            PaymentFragment.OnPaymentFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }


    @NonNull
    @Override
    public BuyCreditOptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_credit_options_item, parent, false);
        return new BuyCreditOptionsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final BuyCreditOptionsAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPrice.setText(mValues.get(position).mPrice);
        holder.mCreditValue.setText(mValues.get(position).mCreditValue);
        //holder.mIcon.setImageDrawable();;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onBuyCreditOptionsChoose(holder.mItem.mCreditValue, holder.mItem.mPrice);
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
        public final TextView mPrice;
        public final TextView mCreditValue;
        public final ImageView mIcon;
        public BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCreditValue = (TextView) view.findViewById(R.id.payment_buy_credit_option_item_credit_value);
            mIcon = (ImageView) view.findViewById(R.id.payment_buy_credit_option_item_image);
            mPrice = (TextView) view.findViewById(R.id.payment_buy_credit_option_item_price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCreditValue.getText() + "'";
        }
    }
}
