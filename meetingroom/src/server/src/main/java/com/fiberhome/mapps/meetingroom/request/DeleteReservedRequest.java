package com.fiberhome.mapps.meetingroom.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rop.AbstractRopRequest;

public class DeleteReservedRequest extends AbstractRopRequest
{
    @NotNull
    private String reservedId;
    /**
     * 操作类型，1：准备中取消会议，2：使用中结束会议，3：已结束/已取消删除会议
     */
    @NotNull
    @Pattern(regexp = "1|2|3", message = "格式不合法")
    private String operationType;

    public String getReservedId()
    {
        return reservedId;
    }

    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }

}
