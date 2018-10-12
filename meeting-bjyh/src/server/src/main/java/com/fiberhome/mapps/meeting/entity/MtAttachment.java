package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mt_attachment")
public class MtAttachment
{
    @Id
    private String id;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Double size;

    @Column(name = "upload_time")
    private Date   uploadTime;
    private String uploadTimeStr;
    /**
     * 1：查看 3：下载（含查看）
     */
    private Long   privilege;

    private String viewUrl;

    private String downloadUrl;

    /**
     * @return id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return meeting_id
     */
    public String getMeetingId()
    {
        return meetingId;
    }

    /**
     * @param meetingId
     */
    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

    /**
     * @return file_path
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * @param filePath
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * @return file_name
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * @return content_type
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * @param contentType
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public Double getSize()
    {
        return size;
    }

    public void setSize(Double size)
    {
        this.size = size;
    }

    /**
     * 获取1：查看 3：下载（含查看）
     *
     * @return privilege - 1：查看 3：下载（含查看）
     */
    public Long getPrivilege()
    {
        return privilege;
    }

    /**
     * 设置1：查看 3：下载（含查看）
     *
     * @param privilege 1：查看 3：下载（含查看）
     */
    public void setPrivilege(Long privilege)
    {
        this.privilege = privilege;
    }

    public String getViewUrl()
    {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl)
    {
        this.viewUrl = viewUrl;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }

    public Date getUploadTime()
    {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime)
    {
        this.uploadTime = uploadTime;
    }

    public String getUploadTimeStr()
    {
        return uploadTimeStr;
    }

    public void setUploadTimeStr(String uploadTimeStr)
    {
        this.uploadTimeStr = uploadTimeStr;
    }

}