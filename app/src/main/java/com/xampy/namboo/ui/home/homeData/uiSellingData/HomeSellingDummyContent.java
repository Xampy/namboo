package com.xampy.namboo.ui.home.homeData.uiSellingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeSellingDummyContent {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<HomeSellingDummyContent.HomeSellingDummyItem> HOME_SELLING_ITEMS =
            new ArrayList<HomeSellingDummyContent.HomeSellingDummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, HomeSellingDummyContent.HomeSellingDummyItem> HOME_SELLING_ITEM_MAP =
            new HashMap<>();

    private static final int COUNT = 4;
    private static String[] images = {" ", " ", " ", " "};

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(String.valueOf(i), images[i]));
        }
    }

    private static void addItem(HomeSellingDummyContent.HomeSellingDummyItem item) {
        HOME_SELLING_ITEMS.add(item);
        HOME_SELLING_ITEM_MAP.put(item.mId, item);
    }

    private static HomeSellingDummyContent.HomeSellingDummyItem createDummyItem(
            String id, String img_name) {
        return new HomeSellingDummyContent.HomeSellingDummyItem(id, img_name);
    }

    public static class HomeSellingDummyItem {
        public final String mId;
        public final String mImage;

        public HomeSellingDummyItem (String id, String img_name) {
            this.mId = id;
            this.mImage = img_name;
        }

        @Override
        public String toString() {
            return mImage;
        }
    }

}
