package com.sai.nanodegree.capstone.backend;

import java.util.Date;

/**
 * Created by smajeti on 5/9/16.
 */
public class UserBean {
    private Long id = -1L;
    private String email;
    private String displayName;
    private Boolean subscriptionPaid;
    private Date subscriptionPaidDate;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getSubscriptionPaid() {
        return subscriptionPaid;
    }

    public void setSubscriptionPaid(Boolean subscriptionPaid) {
        this.subscriptionPaid = subscriptionPaid;
    }

    public Date getSubscriptionPaidDate() {
        return subscriptionPaidDate;
    }

    public void setSubscriptionPaidDate(Date subscriptionPaidDate) {
        this.subscriptionPaidDate = subscriptionPaidDate;
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