package org.byters.vkmarketplace.model.dataclasses;

public class LikesBlob {
    private LikesInfo response;

    public boolean isLiked() {
        return response.getLiked() > 0;
    }

    public class LikesInfo {
        int liked;

        public int getLiked() {
            return liked;
        }
    }
}
