package com.fiberhome.mapps.sign.entity;

import javax.persistence.*;

@Table(name = "SN_SIGN_IMAGE")
public class SnSignImage {
    @Id
    @Column(name = "sign_image_id")
    private String signImageId;

    @Column(name = "sign_id")
    private String signId;

    private String image;

    /**
     * @return sign_image_id
     */
    public String getSignImageId() {
        return signImageId;
    }

    /**
     * @param signImageId
     */
    public void setSignImageId(String signImageId) {
        this.signImageId = signImageId;
    }

    /**
     * @return sign_id
     */
    public String getSignId() {
        return signId;
    }

    /**
     * @param signId
     */
    public void setSignId(String signId) {
        this.signId = signId;
    }

    /**
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }
}