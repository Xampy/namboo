package com.xampy.namboo.ui.profile.profileData.myPosts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPostsDataDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<MyPostsDataDummyContent.MyPostsDataDummyItem> MY_POSTS_DATA_DUMMY_ITEMS =
            new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, MyPostsDataDummyContent.MyPostsDataDummyItem> MY_POSTS_DATA_DUMMY_ITEM_MAP =
            new HashMap<>();

    private static final int COUNT = 20;
    private static String[] categories = {"Location", " ", " ", " "};
    private static String[] room_type = {"Chambre salon", " ", " ", " "};
    private static String[] positions = {"Tokoin, Colombe", " ", " ", " "};

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(String.valueOf(i), categories[0], room_type[0], positions[0]));
        }
    }

    private static void addItem(MyPostsDataDummyContent.MyPostsDataDummyItem item) {
        MY_POSTS_DATA_DUMMY_ITEMS.add(item);
        MY_POSTS_DATA_DUMMY_ITEM_MAP.put(item.mId, item);
    }

    private static MyPostsDataDummyContent.MyPostsDataDummyItem createDummyItem(
            String id, String cat_type, String r_type, String position) {
        return new MyPostsDataDummyContent.MyPostsDataDummyItem(id, cat_type, r_type, position);
    }

    public static class MyPostsDataDummyItem {
        public final String mId;
        public final String mCategoryType;
        public final String mRoomType;
        public final String mPosition;

        public MyPostsDataDummyItem (String id, String cat_type, String r_type, String position) {
            this.mId = id;
            this.mCategoryType = cat_type;
            this.mRoomType = r_type;
            this.mPosition = position;
        }

        @Override
        public String toString() {
            return mCategoryType;
        }
    }
}
