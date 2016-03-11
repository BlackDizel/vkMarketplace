package org.byters.vkmarketplace.model.dataclasses;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;

public class CommentsBlob {
    private CommentsItemsBlob response;

    @Nullable
    public ArrayList<CommentInfo> getComments() {
        if (response == null) return null;
        return response.getItems();
    }

    public class CommentsItemsBlob {
        private ArrayList<CommentInfo> items;
        private ArrayList<CommentUserInfo> profiles;
        private ArrayList<CommentGroupInfo> groups;

        public ArrayList<CommentInfo> getItems() {
            if (items == null) return null;
            for (CommentInfo item : items) {
                int senderId = item.getFrom_id();
                CommentSenderInfo senderInfo;
                if (senderId < 0)
                    senderInfo = getItemFromGroups(senderId);
                else senderInfo = getItemFromUsers(senderId);
                if (senderInfo == null)
                    continue;
                item.setTitle(senderInfo.getTitle());
                item.setImageUrl(senderInfo.getPhoto_100());
            }

            return items;
        }

        private CommentSenderInfo getItemFromUsers(int senderId) {
            if (profiles == null) return null;
            for (CommentUserInfo user : profiles)
                if (user.getId() == senderId)
                    return user;
            return null;
        }

        private CommentSenderInfo getItemFromGroups(int senderId) {
            if (groups == null) return null;
            for (CommentGroupInfo group : groups)
                if (group.getId() == Math.abs(senderId))
                    return group;
            return null;
        }
    }

    public abstract class CommentSenderInfo {
        private String photo_100;
        private int id;

        public String getPhoto_100() {
            return photo_100;
        }

        /**
         * return positive number for groups
         *
         * @return
         */
        public int getId() {
            return id;
        }

        @Nullable
        public abstract String getTitle();
    }

    public class CommentUserInfo extends CommentSenderInfo {
        private String first_name;
        private String last_name;

        @Nullable
        @Override
        public String getTitle() {
            if (TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name))
                return null;
            return String.format("%s %s", first_name, last_name);
        }
    }

    public class CommentGroupInfo extends CommentSenderInfo {
        private String name;

        @Nullable
        @Override
        public String getTitle() {
            return name;
        }
    }

    public class CommentInfo {
        private String text;
        private int from_id;
        private String title;
        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFrom_id() {
            return from_id;
        }

        public String getText() {
            return text;
        }
    }
}
