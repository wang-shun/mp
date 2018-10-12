package com.fiberhome.mapps.ydzf.entity;

import java.util.Date;

import com.fiberhome.mapps.ydzf.utils.DateUtil;

public class LawExpFoodCosmetics {
	private String declNo;
	
	private String varietiesNum;
	
	private String mnufctrRegName;
	
	private String importingCountry;
	
	private String inspectionAndQuarantine;
	
	private String healthStatus;
	
	private String otherMixed;
	
	private String packingTrans;
	
	private String sensoryTest;
	
	private String packingWhole;
	
	private String leakageAnomaly;
	
	private String labelCheck;
	
	private String isneedSmpl;
	
	private String remark;
	
	private String  receiverDocCode;
	
	private String receiverDocName;
	
	private String acceptApprTm;
	
	private Date operTime;
	
	private String inspectorCode;
	
	private Date confmTime;
	
	private String confmTimeStr;
	
	private String userId;
	
	private String inspectorName;

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public String getVarietiesNum() {
		return varietiesNum;
	}

	public void setVarietiesNum(String varietiesNum) {
		this.varietiesNum = varietiesNum;
	}

	public String getMnufctrRegName() {
		return mnufctrRegName;
	}

	public void setMnufctrRegName(String mnufctrRegName) {
		this.mnufctrRegName = mnufctrRegName;
	}

	public String getImportingCountry() {
		return importingCountry;
	}

	public void setImportingCountry(String importingCountry) {
		this.importingCountry = importingCountry;
	}

	public String getInspectionAndQuarantine() {
		return inspectionAndQuarantine;
	}

	public void setInspectionAndQuarantine(String inspectionAndQuarantine) {
		this.inspectionAndQuarantine = inspectionAndQuarantine;
	}

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public String getOtherMixed() {
		return otherMixed;
	}

	public void setOtherMixed(String otherMixed) {
		this.otherMixed = otherMixed;
	}

	public String getPackingTrans() {
		return packingTrans;
	}

	public void setPackingTrans(String packingTrans) {
		this.packingTrans = packingTrans;
	}

	public String getSensoryTest() {
		return sensoryTest;
	}

	public void setSensoryTest(String sensoryTest) {
		this.sensoryTest = sensoryTest;
	}

	public String getPackingWhole() {
		return packingWhole;
	}

	public void setPackingWhole(String packingWhole) {
		this.packingWhole = packingWhole;
	}

	public String getLeakageAnomaly() {
		return leakageAnomaly;
	}

	public void setLeakageAnomaly(String leakageAnomaly) {
		this.leakageAnomaly = leakageAnomaly;
	}

	public String getLabelCheck() {
		return labelCheck;
	}

	public void setLabelCheck(String labelCheck) {
		this.labelCheck = labelCheck;
	}

	public String getIsneedSmpl() {
		return isneedSmpl;
	}

	public void setIsneedSmpl(String isneedSmpl) {
		this.isneedSmpl = isneedSmpl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReceiverDocCode() {
		return receiverDocCode;
	}

	public void setReceiverDocCode(String receiverDocCode) {
		this.receiverDocCode = receiverDocCode;
	}

	public String getReceiverDocName() {
		return receiverDocName;
	}

	public void setReceiverDocName(String receiverDocName) {
		this.receiverDocName = receiverDocName;
	}

	public String getAcceptApprTm() {
		return acceptApprTm;
	}

	public void setAcceptApprTm(String acceptApprTm) {
		this.acceptApprTm = acceptApprTm;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getInspectorCode() {
		return inspectorCode;
	}

	public void setInspectorCode(String inspectorCode) {
		this.inspectorCode = inspectorCode;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getConfmTimeStr() {
		return confmTimeStr;
	}

	public void setConfmTimeStr(String confmTimeStr) {
		this.confmTimeStr = confmTimeStr;
	}

}
