package com.xampy.namboo.ui.home.homeData.bottomRoomsData;

import com.xampy.namboo.ui.commonDataAdapters.commonRoomAdapter.CommonMaisonDummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeBottomChambreDummyContent {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<HomeBottomChambreDummyContent.HomeBottomChambreDummyItem> HOME_BOTTOM_CHAMBRE_DUMMY_ITEMS =
            new ArrayList<HomeBottomChambreDummyContent.HomeBottomChambreDummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, HomeBottomChambreDummyContent.HomeBottomChambreDummyItem> HOME_BOTTOM_CHAMBRE_DUMMY_ITEMS_MAP =
            new HashMap<String, HomeBottomChambreDummyContent.HomeBottomChambreDummyItem>();

    private static final int COUNT = 6;
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
                            String.valueOf(i), imagesUri[0], prices[0], "Lomé-TOGO"
                    )
            );
        }
    }

    private static void addItem(HomeBottomChambreDummyContent.HomeBottomChambreDummyItem item) {
        HOME_BOTTOM_CHAMBRE_DUMMY_ITEMS.add(item);
        HOME_BOTTOM_CHAMBRE_DUMMY_ITEMS_MAP.put(item.mId, item);
    }

    private static HomeBottomChambreDummyContent. HomeBottomChambreDummyItem createDummyItem(
            String id, String image, String price, String district_ville) {
        return new HomeBottomChambreDummyContent. HomeBottomChambreDummyItem(id, image, price, district_ville);
    }

    /**
     * A Category Scrolling dummy item representing a piece of content.
     */
    public static class HomeBottomChambreDummyItem {
        public final String mId;
        public final String mImageDownloadUrl;
        public final String mPrice;
        public final String mDistrictCountry;

        public HomeBottomChambreDummyItem(String id, String image_name, String price, String q_v) {
            this.mId = id;
            this.mImageDownloadUrl = image_name;
            this.mPrice = price;
            this.mDistrictCountry = q_v;
        }

        @Override
        public String toString() {
            return mImageDownloadUrl;
        }
    }
}
