package com.example.not.firebaseimageupload;

/**
 * Created by NOT on 19/02/2020.
 */

public class Upload {
    private String name;
    private String imageUrl;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String mName, String mImageUrl) {
        if(mName.trim().equals("")) {
            mName = "No name";
        }

        this.name = mName;
        this.imageUrl = mImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
