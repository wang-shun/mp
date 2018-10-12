package com.fiberhome.mapps.vote.entity;

import java.util.List;

public class MobilearkBean
{
    private List<MobilearkDepartment> departmentInfos;
    private List<MobilearkUser>       userInfos;
    private int                       userSize;
    private List<MobilearkDocument>   documentList;
    private List<MobilearkFolder>     folderList;

    private String[]                  docUrls;

    public List<MobilearkFolder> getFolderList()
    {
        return folderList;
    }

    public void setFolderList(List<MobilearkFolder> folderList)
    {
        this.folderList = folderList;
    }

    public int getUserSize()
    {
        return userSize;
    }

    public void setUserSize(int userSize)
    {
        this.userSize = userSize;
    }

    public List<MobilearkDepartment> getDepartmentInfos()
    {
        return departmentInfos;
    }

    public void setDepartmentInfos(List<MobilearkDepartment> departmentInfos)
    {
        this.departmentInfos = departmentInfos;
    }

    public List<MobilearkUser> getUserInfos()
    {
        return userInfos;
    }

    public void setUserInfos(List<MobilearkUser> userInfos)
    {
        this.userInfos = userInfos;
    }

    public List<MobilearkDocument> getDocumentList()
    {
        return documentList;
    }

    public void setDocumentList(List<MobilearkDocument> documentList)
    {
        this.documentList = documentList;
    }

    public String[] getDocUrls()
    {
        return docUrls;
    }

    public void setDocUrls(String[] docUrls)
    {
        this.docUrls = docUrls;
    }

}
