package com.fiberhome.mapps.ydzf.entity;

import java.util.Date;

import com.fiberhome.mapps.ydzf.utils.DateUtil;

public class LawFoodCosmetics {
	private String status;
	
	private String declNo;
	
	private String receiverDocName;
	
	private String inspectorName;
	
	private Date  acceptApprTm;
	
	private Date confmTime;

	private String confmTimeStr;
	
	private String mnufctrRegName;
	
	private String declRegName;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public String getReceiverDocName() {
		return receiverDocName;
	}

	public void setReceiverDocName(String receiverDocName) {
		this.receiverDocName = receiverDocName;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public Date getAcceptApprTm() {
		return acceptApprTm;
	}

	public void setAcceptApprTm(Date acceptApprTm) {
		this.acceptApprTm = acceptApprTm;
	}

	public Date getConfmTime() {
		return confmTime;
	}

	public void setConfmTime(Date confmTime) {
		this.confmTime = confmTime;
		if(confmTime!=null){
			setConfmTimeStr(DateUtil.sdf.format(confmTime));
		}
	}

	public String getConfmTimeStr() {
		return confmTimeStr;
	}

	public void setConfmTimeStr(String confmTimeStr) {
		this.confmTimeStr = confmTimeStr;
	}

	public String getMnufctrRegName() {
		return mnufctrRegName;
	}

	public void setMnufctrRegName(String mnufctrRegName) {
		this.mnufctrRegName = mnufctrRegName;
	}

	public String getDeclRegName() {
		return declRegName;
	}

	public void setDeclRegName(String declRegName) {
		this.declRegName = declRegName;
	}

	
}
