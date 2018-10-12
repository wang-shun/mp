package com.fiberhome.mapps.meeting.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.contact.pojo.MobilearkFolder;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.meeting.entity.MtDocument;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class GetPersonDocResponse extends BaseResponse
{

    @XmlElement(name = "documentList")
    private List<MtDocument>      documentInfo;
    @XmlElement(name = "folderList")
    private List<MobilearkFolder> folderList;

    public List<MobilearkFolder> getFolderList()
    {
        return folderList;
    }

    public void setFolderList(List<MobilearkFolder> folderList)
    {
        this.folderList = folderList;
    }

    public List<MtDocument> getDocumentInfo()
    {
        return documentInfo;
    }

    public void setDocumentInfo(List<MtDocument> documentInfo)
    {
        this.documentInfo = documentInfo;
    }

}
