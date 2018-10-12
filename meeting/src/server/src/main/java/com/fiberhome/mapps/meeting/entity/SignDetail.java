package com.fiberhome.mapps.meeting.entity;

import java.util.List;
import java.util.Map;

public class SignDetail
{

    private String              personId;

    private String              personName;

    private String              sequ;

    private String              signTimeStr;

    private String              deptName;

    private String              phone;

    private Map<String, String> signInfo;

    private List<String>        signTimes;

    private String              personType;

    public String getPersonType()
    {
        return personType;
    }

    public void setPersonType(String personType)
    {
        this.personType = personType;
    }

    public Map<String, String> getSignInfo()
    {
        return signInfo;
    }

    public void setSignInfo(Map<String, String> signInfo)
    {
        this.signInfo = signInfo;
    }

    public String getSequ()
    {
        return sequ;
    }

    public void setSequ(String sequ)
    {
        this.sequ = sequ;
    }

    public String getSignTimeStr()
    {
        return signTimeStr;
    }

    public void setSignTimeStr(String signTimeStr)
    {
        this.signTimeStr = signTimeStr;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public String getPersonName()
    {
        return personName;
    }

    public void setPersonName(String personName)
    {
        this.personName = personName;
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

    public List<String> getSignTimes()
    {
        return signTimes;
    }

    public void setSignTimes(List<String> signTimes)
    {
        this.signTimes = signTimes;
    }

}
