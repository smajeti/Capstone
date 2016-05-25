package com.sai.nanodegree.capstone.backend;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;
    private String videoRelPath;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public String getVideoRelPath() {
        return videoRelPath;
    }

    public void setVideoRelPath(String videoRelPath) {
        this.videoRelPath = videoRelPath;
    }
}