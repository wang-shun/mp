package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "mc_tag")
public class McTag {
    @Id
    private String id;

    private String tag;

    @Column(name = "system_id")
    private String systemId;

    private String name;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return system_id
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * @param systemId
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}