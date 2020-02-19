package com.example.not.firebaseimageupload;

/**
 * Created by NOT on 19/02/2020.
 */

public class Upload {
    private String mName;
    private String mImageUrl;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String mName, String mImageUrl) {
        if(mName.trim().equals("")) {
            mName = "No name";
        }

        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
