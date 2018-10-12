package com.fiberhome.mapps.ydzf.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "law_im_foodcosmetics")
public class LawImFoodCosmetics {	
	/**
	 * 报检号
	 */
	@Id
	@Column(name = "decl_no")
	private String declNo;
	/**
	 * 报检单位
	 */
	@Column(name = "decl_reg_name")
	private String declRegName;
	/**
	 * 报检单位代码
	 */
	@Column(name = "decl_unit_code")
	private String declUnitCode;
	
	@Column(name = "consignee_cname")
	private String consigneeCname;
	
	@Column(name = "consignee_code")
	private String consigneeCode;
	
	@Column(name = "varieties_num")
	private String varietiesNum;
	
	@Column(name = "check_place")
	private String checkPlace;
	
	@Column(name = "weather")
	private String weather;
	
	@Column(name = "cont_no_str")
	private String contNoStr;
	
	@Column(name = "packing_trans")
	private String packingTrans;
	
	@Column(name = "iwood_packaging")
	private String iwoodPackaging;
	
	@Column(name = "wood_packaging_qualified")
	private String woodPackagingGualified;
	
	@Column(name = "health_status")
	private String healthStatus;
	
	@Column(name = "medical_vector")
	private String medicalVector;
	
	@Column(name = "poisonous_harmful")
	private String poisonousHarmful;
	
	@Column(name = "residue")
	private String residue;
	
	@Column(name = "other_mixed")
	private String otherMixed;
	
	@Column(name = "cargo_certificate")
	private String cargoCertificate;
	
	@Column(name = "other_notconform")
	private String otherNotconform;
	
	@Column(name = "sampling")
	private String sampling;
	
	@Column(name = "submission")
	private String submission;
	
	@Column(name = "submission_num")
	private String submissionNum;
	
	@Column(name = "packing_whole")
	private String packingWhole;
	
	@Column(name = "leakage_anomaly")
	private String leakageAnomaly;
	
	@Column(name = "label_check")
	private String labelCheck;
	
	@Column(name = "technical_processing")
	private String technicalProcessing;
	
	@Column(name = "isneed_smpl")
	private String isneedSmpl;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "receiver_doc_code")
	private String  receiverDocCode;
	
	@Column(name = "receiver_doc_name")
	private String receiverDocName;
	
	@Column(name = "accept_appr_tm")
	private String acceptApprTm;
	
	@Column(name = "oper_time")
	private Date operTime;
	
	@Column(name = "inspector_code")
	private String inspectorCode;
	
	@Column(name = "confm_time")
	private Date confmTime;
	
	@Column(name = "userid")
	private String userId;
	
	@Column(name = "inspector_name")
	private String inspectorName;
	
	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public String getDeclRegName() {
		return declRegName;
	}

	public void setDeclRegName(String declRegName) {
		this.declRegName = declRegName;
	}

	public String getDeclUnitCode() {
		return declUnitCode;
	}

	public void setDeclUnitCode(String declUnitCode) {
		this.declUnitCode = declUnitCode;
	}

	public String getConsigneeCname() {
		return consigneeCname;
	}

	public void setConsigneeCname(String consigneeCname) {
		this.consigneeCname = consigneeCname;
	}

	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}

	public String getVarietiesNum() {
		return varietiesNum;
	}

	public void setVarietiesNum(String varietiesNum) {
		this.varietiesNum = varietiesNum;
	}

	public String getCheckPlace() {
		return checkPlace;
	}

	public void setCheckPlace(String checkPlace) {
		this.checkPlace = checkPlace;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getContNoStr() {
		return contNoStr;
	}

	public void setContNoStr(String contNoStr) {
		this.contNoStr = contNoStr;
	}

	public String getPackingTrans() {
		return packingTrans;
	}

	public void setPackingTrans(String packingTrans) {
		this.packingTrans = packingTrans;
	}

	public String getIwoodPackaging() {
		return iwoodPackaging;
	}

	public void setIwoodPackaging(String iwoodPackaging) {
		this.iwoodPackaging = iwoodPackaging;
	}

	public String getWoodPackagingGualified() {
		return woodPackagingGualified;
	}

	public void setWoodPackagingGualified(String woodPackagingGualified) {
		this.woodPackagingGualified = woodPackagingGualified;
	}

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public String getMedicalVector() {
		return medicalVector;
	}

	public void setMedicalVector(String medicalVector) {
		this.medicalVector = medicalVector;
	}

	public String getPoisonousHarmful() {
		return poisonousHarmful;
	}

	public void setPoisonousHarmful(String poisonousHarmful) {
		this.poisonousHarmful = poisonousHarmful;
	}

	public String getResidue() {
		return residue;
	}

	public void setResidue(String residue) {
		this.residue = residue;
	}

	public String getOtherMixed() {
		return otherMixed;
	}

	public void setOtherMixed(String otherMixed) {
		this.otherMixed = otherMixed;
	}

	public String getCargoCertificate() {
		return cargoCertificate;
	}

	public void setCargoCertificate(String cargoCertificate) {
		this.cargoCertificate = cargoCertificate;
	}

	public String getOtherNotconform() {
		return otherNotconform;
	}

	public void setOtherNotconform(String otherNotconform) {
		this.otherNotconform = otherNotconform;
	}

	public String getSampling() {
		return sampling;
	}

	public void setSampling(String sampling) {
		this.sampling = sampling;
	}

	public String getSubmission() {
		return submission;
	}

	public void setSubmission(String submission) {
		this.submission = submission;
	}

	public String getSubmissionNum() {
		return submissionNum;
	}

	public void setSubmissionNum(String submissionNum) {
		this.submissionNum = submissionNum;
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

	public String getTechnicalProcessing() {
		return technicalProcessing;
	}

	public void setTechnicalProcessing(String technicalProcessing) {
		this.technicalProcessing = technicalProcessing;
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

}
