package com.sai.nanodegree.capstone.backend;

import java.util.Date;

/**
 * Created by smajeti on 5/15/16.
 */
public class UserSongPlayHistoryBean {
    private Long id = -1L;
    private String email;
    private String categoryName;
    private String ragam;
    private String title;
    private Double palyedTime;
    private Date updatedDate;
    private Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Double getPalyedTime() {
        return palyedTime;
    }

    public void setPalyedTime(Double palyedTime) {
        this.palyedTime = palyedTime;
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
