package com.xampy.namboo.ui.oneRoomPresentation.oneRoomData;

import com.xampy.namboo.ui.home.homeData.uiSellingData.HomeSellingDummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDataImagesDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<RoomDataImagesDummyContent.RoomDataImagesDummyItem> ROOM_IMAGES_ITEMS =
            new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, RoomDataImagesDummyContent.RoomDataImagesDummyItem> ROOM_IMAGES_ITEM_MAP =
            new HashMap<>();

    private static final int COUNT = 4;
    private static String[] images = {" ", " ", " ", " "};

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(String.valueOf(i), images[i]));
        }
    }

    public static void addItem(RoomDataImagesDummyContent.RoomDataImagesDummyItem item) {
        ROOM_IMAGES_ITEMS.add(item);
        ROOM_IMAGES_ITEM_MAP.put(item.mId, item);
    }

    public static RoomDataImagesDummyContent.RoomDataImagesDummyItem createDummyItem(
            String id, String img_name) {
        return new RoomDataImagesDummyContent.RoomDataImagesDummyItem(id, img_name);
    }

    public static class RoomDataImagesDummyItem {
        public final String mId;
        public final String mImage;

        public RoomDataImagesDummyItem (String id, String img_name) {
            this.mId = id;
            this.mImage = img_name;
        }

        @Override
        public String toString() {
            return mImage;
        }
    }
}
