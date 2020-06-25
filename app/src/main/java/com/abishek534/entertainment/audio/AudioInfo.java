package com.abishek534.entertainment.audio;

public class AudioInfo {
    private String audioTitle;
    private String audioDuration;
    private String audioUrl;
    private String audioDate;
    private String likes;
    private String views;
    private String thumbUrl;
    private String audioId;


    public AudioInfo(String audioTitle, String audioDuration, String audioUrl, String audioDate, String likes, String views, String thumbUrl, String audioId) {
        this.audioTitle = audioTitle;
        this.audioDuration = audioDuration;
        this.audioUrl = audioUrl;
        this.audioDate = audioDate;
        this.likes = likes;
        this.views = views;
        this.thumbUrl = thumbUrl;
        this.audioId = audioId;
    }
    public AudioInfo(){}


    public String getAudioTitle() {
        return audioTitle;
    }

    public String getAudioDuration() {
        return audioDuration;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getAudioDate() {
        return audioDate;
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

    public String getAudioId() {
        return audioId;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public void setAudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public void setAudioDate(String audioDate) {
        this.audioDate = audioDate;
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

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }
}
