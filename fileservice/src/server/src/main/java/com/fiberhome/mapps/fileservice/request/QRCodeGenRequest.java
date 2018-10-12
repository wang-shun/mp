package com.fiberhome.mapps.fileservice.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.rop.AbstractRopRequest;

public class QRCodeGenRequest extends AbstractRopRequest {
	@NotNull
	@Size(min=1, max=255)
	String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}
