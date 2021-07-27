package com.xampy.namboo.ui.search.searchData;

import java.io.Serializable;

public class SearchDataModelRoom implements Serializable {

    private final String mPostType;
    private final String mRoomType;
    private final int mPriceMin;
    private final int mPriceMax;
    private final String mCity;
    private final String mDistrict;


    public SearchDataModelRoom (
            String post_type,
            String room_type,
            int price_min,
            int price_max,
            String city,
            String district
    ){
        this.mPostType = post_type;
        this.mRoomType = room_type;
        this.mPriceMin = price_min;
        this.mPriceMax = price_max;
        this.mCity = city;
        this.mDistrict = district;
    }


    public String getmRoomType() {return mRoomType;}
    public String getmDistrict() {return mDistrict;}
    public String getmCity() {return mCity;}
    public int getmPriceMax() {return mPriceMax;}
    public int getmPriceMin() {return mPriceMin;}
    public String getmPostType() {return mPostType;}
}
