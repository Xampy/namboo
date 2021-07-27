package com.xampy.namboo.ui.fonds.paymentData.buyCreditData;

import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingDummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyCreditOptionsDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem> BUY_CREDIT_OPTIONS_DUMMY_ITEMS =
            new ArrayList<BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem> BUY_CREDIT_OPTIONS_DUMMY_ITEM_MAP =
            new HashMap<String, BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem>();

    private static final int COUNT = 4;
    private static String[] prices = {"500 Fcfa", " 1 000 Fcfa", "2 500 Fcfa", "4 500 Fcfa"};
    private static String[] c_values = {"10 credits", "30 crédits", "90 crédits", "150 crédits"};
    private static String[] icons =
            {"ic_home_white_24dp"};

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(String.valueOf(i), icons[0], prices[i], c_values[i]));
        }
    }

    private static void addItem(BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem item) {
        BUY_CREDIT_OPTIONS_DUMMY_ITEMS.add(item);
        BUY_CREDIT_OPTIONS_DUMMY_ITEM_MAP.put(item.mId, item);
    }

    private static BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem createDummyItem(
            String id, String icon, String price, String credit_value) {
        return new BuyCreditOptionsDummyContent.BuyCreditOptionsDummyItem(id, icon, price, credit_value);
    }

    /**
     * A Category Scrolling dummy item representing a piece of content.
     */
    public static class BuyCreditOptionsDummyItem {
        public final String mId;
        public final String mIconName;
        public final String mPrice;
        public final String mCreditValue;

        public BuyCreditOptionsDummyItem (
                String id, String icon_name, String price, String credit_value) {
            this.mId = id;
            this.mIconName = icon_name;
            this.mPrice = price;
            this.mCreditValue = credit_value;
        }

        @Override
        public String toString() {
            return mPrice;
        }
    }
}
