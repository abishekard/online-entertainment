package com.abishek534.entertainment.image;

public class ImageInfo {
    private String imageTitle;
    private String like;
    private String views;
    private String date;
    private String imageId;
    private String imageUrl;

    public ImageInfo(String imageTitle, String like, String views, String date, String imageId, String imageUrl) {
        this.imageTitle = imageTitle;
        this.like = like;
        this.views = views;
        this.date = date;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }
    public ImageInfo(){}

    public String getImageTitle() {
        return imageTitle;
    }

    public String getLike() {
        return like;
    }

    public String getViews() {
        return views;
    }

    public String getDate() {
        return date;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
