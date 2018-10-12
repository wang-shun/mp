package com.fiberhome.mapps.meeting.entity;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mt_signin_record")
public class MtSigninRecord
{
    @Id
    private String id;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "sequ_id")
    private String sequId;

    @Column(name = "person_id")
    private String personId;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "person_type")
    private String personType;
    /**
     * Y：是 N：否
     */
    private String signed;

    @Column(name = "sign_time")
    private Date   signTime;

    @Column(name = "serv_id")
    private String servId;

    private String signTimeStr;
    
    private String sequ;
	
    private String deptName;

    private String phone;
    
    public String getPersonType()
    {
        return personType;
    }

    public void setPersonType(String personType)
    {
        this.personType = personType;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getSignTimeStr()
    {
        return signTimeStr;
    }

    public void setSignTimeStr(String signTimeStr)
    {
        this.signTimeStr = signTimeStr;
    }

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
     * @return sequ_id
     */
    public String getSequId()
    {
        return sequId;
    }

    /**
     * @param sequId
     */
    public void setSequId(String sequId)
    {
        this.sequId = sequId;
    }

    /**
     * @return person_id
     */
    public String getPersonId()
    {
        return personId;
    }

    /**
     * @param personId
     */
    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    /**
     * @return person_name
     */
    public String getPersonName()
    {
        return personName;
    }

    /**
     * @param personName
     */
    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    /**
     * 获取Y：是 N：否
     *
     * @return signed - Y：是 N：否
     */
    public String getSigned()
    {
        return signed;
    }

    /**
     * 设置Y：是 N：否
     *
     * @param signed Y：是 N：否
     */
    public void setSigned(String signed)
    {
        this.signed = signed;
    }

    /**
     * @return sign_time
     */
    public Date getSignTime()
    {
        return signTime;
    }

    /**
     * @param signTime
     */
    public void setSignTime(Date signTime)
    {
        this.signTime = signTime;
    }

    /**
     * @return serv_id
     */
    public String getServId()
    {
        return servId;
    }

    /**
     * @param servId
     */
    public void setServId(String servId)
    {
        this.servId = servId;
    }

	public String getSequ() {
		return sequ;
	}

	public void setSequ(String sequ) {
		this.sequ = sequ;
	}
    
}