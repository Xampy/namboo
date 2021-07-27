package com.xampy.namboo.ui.search.searchData;

import java.io.Serializable;

public class SearchDataModelService implements Serializable {

    private final String mServiceType;
    private final String mServiceCity;
    private final String mServiceDistrict;

    public SearchDataModelService (String service_type, String city, String district) {
        mServiceType = service_type;
        mServiceCity = city;
        mServiceDistrict = district;

    }

    public String getmServiceType() {return mServiceType;}
    public String getmServiceCity() {return mServiceCity;}
    public String getmServiceDistrict() {return mServiceDistrict;}
}
