package com.sai.nanodegree.capstone.backend;

import java.util.Date;

/**
 * Created by smajeti on 5/15/16.
 */
public class SyncDetailsBean {
    private Long id = -1L;
    private String userEmail;
    private Date syncFromServerTime;
    private Date syncToServerTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getSyncFromServerTime() {
        return syncFromServerTime;
    }

    public void setSyncFromServerTime(Date syncFromServerTime) {
        this.syncFromServerTime = syncFromServerTime;
    }

    public Date getSyncToServerTime() {
        return syncToServerTime;
    }

    public void setSyncToServerTime(Date syncToServerTime) {
        this.syncToServerTime = syncToServerTime;
    }
}
