package com.sai.nanodegree.capstone.backend;

import java.util.Date;

/**
 * Created by smajeti on 5/13/16.
 */
public class SongDetailsBean {
    private Long id = -1L;
    private String aarohana;
    private String avarohana;
    private String categoryName;
    private String details;
    private Double duration;
    private String pictureUrl;
    private String videoUrl;
    private String ragam;
    private String title;
    private Date updatedDate;
    private Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAarohana() {
        return aarohana;
    }

    public void setAarohana(String aarohana) {
        this.aarohana = aarohana;
    }

    public String getAvarohana() {
        return avarohana;
    }

    public void setAvarohana(String avarohana) {
        this.avarohana = avarohana;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getRagam() {
        return ragam;
    }

    public void setRagam(String ragam) {
        this.ragam = ragam;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
