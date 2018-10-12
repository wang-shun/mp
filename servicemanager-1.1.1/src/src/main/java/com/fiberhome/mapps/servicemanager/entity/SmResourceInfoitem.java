package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "sm_resource_infoitem")
public class SmResourceInfoitem {
    @Id
    private String id;

    @Column(name = "info_id")
    private String infoId;

    private String key;

    private String name;

    private String remarks;

    /**
     * 类型，text 文本,radio 单选,checkbox 多选
     */
    private String type;

    private String regex;

    /**
     * #radio或者checkbox时的选项，可为空， 例子file:文件系统,fdfs:FastDFS    
     */
    private String options;

    /**
     * 分组的名称排序依据sequ来，取同一分组下的最小的sequ进行排序
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 用于设置控件大小
     */
    @Column(name = "control_size")
    private Long controlSize;

    @Column(name = "default_value")
    private String defaultValue;

    /**
     * 顺序排列
     */
    private Long sequ;

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
     * @return info_id
     */
    public String getInfoId() {
        return infoId;
    }

    /**
     * @param infoId
     */
    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
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

    /**
     * @return remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取类型，text 文本,radio 单选,checkbox 多选
     *
     * @return type - 类型，text 文本,radio 单选,checkbox 多选
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型，text 文本,radio 单选,checkbox 多选
     *
     * @param type 类型，text 文本,radio 单选,checkbox 多选
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return regex
     */
    public String getRegex() {
        return regex;
    }

    /**
     * @param regex
     */
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * 获取#radio或者checkbox时的选项，可为空， 例子file:文件系统,fdfs:FastDFS    
     *
     * @return options - #radio或者checkbox时的选项，可为空， 例子file:文件系统,fdfs:FastDFS    
     */
    public String getOptions() {
        return options;
    }

    /**
     * 设置#radio或者checkbox时的选项，可为空， 例子file:文件系统,fdfs:FastDFS    
     *
     * @param options #radio或者checkbox时的选项，可为空， 例子file:文件系统,fdfs:FastDFS    
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * 获取分组的名称排序依据sequ来，取同一分组下的最小的sequ进行排序
     *
     * @return group_name - 分组的名称排序依据sequ来，取同一分组下的最小的sequ进行排序
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置分组的名称排序依据sequ来，取同一分组下的最小的sequ进行排序
     *
     * @param groupName 分组的名称排序依据sequ来，取同一分组下的最小的sequ进行排序
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 获取用于设置控件大小
     *
     * @return control_size - 用于设置控件大小
     */
    public Long getControlSize() {
        return controlSize;
    }

    /**
     * 设置用于设置控件大小
     *
     * @param controlSize 用于设置控件大小
     */
    public void setControlSize(Long controlSize) {
        this.controlSize = controlSize;
    }

    /**
     * @return default_value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 获取顺序排列
     *
     * @return sequ - 顺序排列
     */
    public Long getSequ() {
        return sequ;
    }

    /**
     * 设置顺序排列
     *
     * @param sequ 顺序排列
     */
    public void setSequ(Long sequ) {
        this.sequ = sequ;
    }
}