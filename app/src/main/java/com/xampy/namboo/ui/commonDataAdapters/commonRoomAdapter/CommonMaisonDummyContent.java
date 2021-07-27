package com.xampy.namboo.ui.commonDataAdapters.commonRoomAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingAdapter;
import com.xampy.namboo.ui.home.homeData.uiCategoryData.CategoryHorizontalScrollingDummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonMaisonDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<CommonMaisonDummyContent.CommonMaisonDummyItem> COMMON_MAISON_DUMMY_ITEMS =
            new ArrayList<CommonMaisonDummyContent.CommonMaisonDummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, CommonMaisonDummyContent.CommonMaisonDummyItem> COMMON_MAISON_DUMMY_ITEMS_MAP =
            new HashMap<String, CommonMaisonDummyContent.CommonMaisonDummyItem>();

    private static final int COUNT = 20;
    private static String[] imagesUri = {
            " ",
            "ic_big_home_white",
            "ic_home_white_24dp",
            "ic_layers_white_24dp",
            "ic_agence_pos"
    };
    private static String[] prices = {
            "10 000 Fcfa",
            "10 000 Fcfa",
            "10 000 Fcfa",
            "10 000 Fcfa",
    };

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(
                    createDummyItem(
                            String.valueOf(i), imagesUri[0], prices[0]
                    )
            );
        }
    }

    private static void addItem(CommonMaisonDummyContent.CommonMaisonDummyItem item) {
        COMMON_MAISON_DUMMY_ITEMS.add(item);
        COMMON_MAISON_DUMMY_ITEMS_MAP.put(item.mId, item);
    }

    private static CommonMaisonDummyContent.CommonMaisonDummyItem createDummyItem(
            String id, String image, String price) {
        return new CommonMaisonDummyContent.CommonMaisonDummyItem(id, image, price);
    }

    /**
     * A Category Scrolling dummy item representing a piece of content.
     */
    public static class CommonMaisonDummyItem {
        public final String mId;
        public final String mImageUri;
        public final String mPrice;

        public CommonMaisonDummyItem(String id, String image_name, String price) {
            this.mId = id;
            this.mImageUri = image_name;
            this.mPrice = price;
        }

        @Override
        public String toString() {
            return mImageUri;
        }
    }
}
