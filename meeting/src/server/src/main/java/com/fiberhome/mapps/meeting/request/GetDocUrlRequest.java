package com.fiberhome.mapps.meeting.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class GetDocUrlRequest extends AbstractRopRequest{
	
	@NotNull
	@Size(max = 36, message = "文档标识最多为36个字符")
	private String documentId;
	
	//1：查看 3：下载（含查看）
	@Pattern(regexp = "1|0", message = "格式不合法")
    private String privilege;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	
}
