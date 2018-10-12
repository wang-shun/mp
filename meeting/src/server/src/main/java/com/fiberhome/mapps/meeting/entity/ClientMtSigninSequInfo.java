package com.fiberhome.mapps.meeting.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mt_signin_sequ")
public class ClientMtSigninSequInfo extends MtSigninSequ
{

    private Long   totalNum;

    private Long   signedNum;

    private String sequStr;

    public String getSequStr()
    {
        return sequStr;
    }

    public void setSequStr(String sequStr)
    {
        this.sequStr = sequStr;
    }

    public Long getTotalNum()
    {
        return totalNum;
    }

    public void setTotalNum(Long totalNum)
    {
        this.totalNum = totalNum;
    }

    public Long getSignedNum()
    {
        return signedNum;
    }

    public void setSignedNum(Long signedNum)
    {
        this.signedNum = signedNum;
    }

}