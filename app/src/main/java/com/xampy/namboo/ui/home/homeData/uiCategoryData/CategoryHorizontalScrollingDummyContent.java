package com.xampy.namboo.ui.home.homeData.uiCategoryData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryHorizontalScrollingDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<CategoryHorizontalScrollingDummyItem> CATEGORY_ITEMS =
            new ArrayList<CategoryHorizontalScrollingDummyItem >();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, CategoryHorizontalScrollingDummyItem > CATEGORY_ITEM_MAP =
            new HashMap<String, CategoryHorizontalScrollingDummyItem >();

    private static final int COUNT = 5;
    private static String[] texts = {"categories", "Maison", "Chambre", "Terrain", "Services"};
    private static String[] icons = {
            "ic_menu_white_24dp",
            "ic_big_home",
            "ic_home_white_24dp",
            "ic_agence_pos",
            "ic_layers_white_24dp"

    };
    private static String[] back = {
            "home_categories_categories_background",
            "home_categories_maison_background",
            "home_categories_chambre_background",
            "home_categories_agences_background",
            "home_categories_services_background"
    };

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(String.valueOf(i), icons[i], back[i], texts[i]));
        }
    }

    private static void addItem(CategoryHorizontalScrollingDummyItem  item) {
        CATEGORY_ITEMS.add(item);
        CATEGORY_ITEM_MAP.put(item.mId, item);
    }

    private static CategoryHorizontalScrollingDummyItem  createDummyItem(
            String id, String icon, String back_name, String text) {
        return new CategoryHorizontalScrollingDummyItem (id, icon, back_name, text);
    }

    /**
     * A Category Scrolling dummy item representing a piece of content.
     */
    public static class CategoryHorizontalScrollingDummyItem {
        public final String mId;
        public final String mIconName;
        public final String mIconBack;
        public final String mText;

        public CategoryHorizontalScrollingDummyItem (String id, String icon_name, String back_name, String text) {
            this.mId = id;
            this.mIconName = icon_name;
            this.mIconBack = back_name;
            this.mText= text;
        }

        @Override
        public String toString() {
            return mText;
        }
    }
}
