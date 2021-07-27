package com.xampy.namboo.ui.posts.postsData.addRoomImagesList;

import android.net.Uri;

import com.xampy.namboo.ui.oneRoomPresentation.oneRoomData.RoomDataImagesDummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRoomImagesDataDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem> ADD_ROOM_IMAGES_DATA_DUMMY_ITEMS =
            new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem> ADD_ROOM_IMAGES_DATA_DUMMY_ITEMS_MAP =
            new HashMap<>();

    private static final int COUNT = 6;
    private static Uri[] images = {};

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(String.valueOf(i), null));
        }
    }

    private static void addItem(AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem item) {
        ADD_ROOM_IMAGES_DATA_DUMMY_ITEMS.add(item);
        ADD_ROOM_IMAGES_DATA_DUMMY_ITEMS_MAP.put(item.mId, item);
    }

    private static AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem createDummyItem(
            String id, Uri img_name) {
        return new AddRoomImagesDataDummyContent.AddRoomImagesDataDummyItem(id, img_name);
    }

    public static class AddRoomImagesDataDummyItem {
        public  String mId;
        public Uri mImageUri;

        public AddRoomImagesDataDummyItem (String id, Uri img_name) {
            this.mId = id;
            this.mImageUri = img_name;
        }

        @Override
        public String toString() {
            return mImageUri.toString();
        }
    }
}
