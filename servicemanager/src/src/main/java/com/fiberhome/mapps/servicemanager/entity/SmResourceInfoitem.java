package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "SM_RESOURCE_INFOITEM")
public class SmResourceInfoitem {
    @Id
    private String id;

    @Column(name = "info_id")
    private String infoId;

    private String key;

    private String name;

    private String remarks;

    /**
     * ���ͣ�text �ı�,radio ��ѡ,checkbox ��ѡ
     */
    private String type;

    private String regex;

    /**
     * #radio����checkboxʱ��ѡ���Ϊ�գ� ����file:�ļ�ϵͳ,fdfs:FastDFS    
     */
    private String options;

    /**
     * �����������������sequ����ȡͬһ�����µ���С��sequ��������
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * �������ÿؼ���С
     */
    @Column(name = "control_size")
    private Long controlSize;

    @Column(name = "default_value")
    private String defaultValue;

    /**
     * ˳������
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
     * ��ȡ���ͣ�text �ı�,radio ��ѡ,checkbox ��ѡ
     *
     * @return type - ���ͣ�text �ı�,radio ��ѡ,checkbox ��ѡ
     */
    public String getType() {
        return type;
    }

    /**
     * �������ͣ�text �ı�,radio ��ѡ,checkbox ��ѡ
     *
     * @param type ���ͣ�text �ı�,radio ��ѡ,checkbox ��ѡ
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
     * ��ȡ#radio����checkboxʱ��ѡ���Ϊ�գ� ����file:�ļ�ϵͳ,fdfs:FastDFS    
     *
     * @return options - #radio����checkboxʱ��ѡ���Ϊ�գ� ����file:�ļ�ϵͳ,fdfs:FastDFS    
     */
    public String getOptions() {
        return options;
    }

    /**
     * ����#radio����checkboxʱ��ѡ���Ϊ�գ� ����file:�ļ�ϵͳ,fdfs:FastDFS    
     *
     * @param options #radio����checkboxʱ��ѡ���Ϊ�գ� ����file:�ļ�ϵͳ,fdfs:FastDFS    
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * ��ȡ�����������������sequ����ȡͬһ�����µ���С��sequ��������
     *
     * @return group_name - �����������������sequ����ȡͬһ�����µ���С��sequ��������
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * ���÷����������������sequ����ȡͬһ�����µ���С��sequ��������
     *
     * @param groupName �����������������sequ����ȡͬһ�����µ���С��sequ��������
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * ��ȡ�������ÿؼ���С
     *
     * @return control_size - �������ÿؼ���С
     */
    public Long getControlSize() {
        return controlSize;
    }

    /**
     * �����������ÿؼ���С
     *
     * @param controlSize �������ÿؼ���С
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
     * ��ȡ˳������
     *
     * @return sequ - ˳������
     */
    public Long getSequ() {
        return sequ;
    }

    /**
     * ����˳������
     *
     * @param sequ ˳������
     */
    public void setSequ(Long sequ) {
        this.sequ = sequ;
    }
}