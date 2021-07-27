package com.xampy.namboo.ui.home.homeData.uiRentingData;

import com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingDummyContent;
import com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingDummyItemInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeRentingDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<HomeRentingDummyContent.HomeRentingDummyItem> HOME_RENTING_ITEMS =
            new ArrayList<HomeRentingDummyContent.HomeRentingDummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, HomeRentingDummyContent.HomeRentingDummyItem> HOME_RENTING_ITEM_MAP =
            new HashMap<>();

    private static final int COUNT = 4;
    private static String[] images = {" ", " ", " ", " "};

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(String.valueOf(i), images[i]));
        }
    }

    private static void addItem(HomeRentingDummyContent.HomeRentingDummyItem item) {
        HOME_RENTING_ITEMS.add(item);
        HOME_RENTING_ITEM_MAP.put(item.mId, item);
    }

    private static HomeRentingDummyContent.HomeRentingDummyItem createDummyItem(
            String id, String img_name) {
        return new HomeRentingDummyContent.HomeRentingDummyItem(id, img_name);
    }

    public static class HomeRentingDummyItem {
        public final String mId;
        public final String mImage;

        public HomeRentingDummyItem (String id, String img_name) {
            this.mId = id;
            this.mImage = img_name;
        }

        @Override
        public String toString() {
            return mImage;
        }
    }
}
