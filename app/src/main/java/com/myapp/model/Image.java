package com.myapp.model;

public class Image {
    private String link;
//    private String thumbnailLink;
    private String title;
    private String contextLink;
    private String fileFormat;

    public Image() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

//    public String getThumbnailLink() {
//        return thumbnailLink;
//    }
//
//    public void setThumbnailLink(String thumbnailLink) {
//        this.thumbnailLink = thumbnailLink;
//    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getContextLink() {
        return contextLink;
    }

    public void setContextLink(String contextLink) {
        this.contextLink = contextLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
