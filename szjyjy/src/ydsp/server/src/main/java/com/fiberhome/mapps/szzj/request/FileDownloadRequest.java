package com.fiberhome.mapps.szzj.request;

import javax.validation.constraints.NotNull;


import com.rop.AbstractRopRequest;

public class FileDownloadRequest extends AbstractRopRequest {
	@NotNull
	private String directory;

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

}
