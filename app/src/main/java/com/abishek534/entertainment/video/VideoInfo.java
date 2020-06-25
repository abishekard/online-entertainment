package com.abishek534.entertainment.video;

public class VideoInfo {
    private String videoTitle;
    private String videoDuration;
    private String videoUrl;
    private String videoDate;
    private String likes;
    private String views;
    private String thumbUrl;
    private String videoId;

    public VideoInfo(String videoTitle, String videoDuration, String videoUrl, String videoDate, String likes, String views, String thumbUrl,String videoId) {
        this.videoTitle = videoTitle;
        this.videoDuration = videoDuration;
        this.videoUrl = videoUrl;
        this.videoDate = videoDate;
        this.likes = likes;
        this.views = views;
        this.thumbUrl = thumbUrl;
        this.videoId = videoId;
    }
    public VideoInfo() {    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public String getLikes() {
        return likes;
    }

    public String getViews() {
        return views;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setVideoTitle(String mediaTitle) {
        this.videoTitle = mediaTitle;
    }

    public void setVideoDuration(String mediaDuration) {
        this.videoDuration = mediaDuration;
    }

    public void setVideoUrl(String mediaUrl) {
        this.videoUrl = mediaUrl;
    }

    public void setVideoDate(String mediaDate) {
        this.videoDate = mediaDate;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
