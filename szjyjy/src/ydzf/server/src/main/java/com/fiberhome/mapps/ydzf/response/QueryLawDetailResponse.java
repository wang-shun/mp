package com.fiberhome.mapps.ydzf.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.ydzf.entity.LawExpFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawFile;
import com.fiberhome.mapps.ydzf.entity.LawImFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawSampling;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryLawDetailResponse extends BaseResponse{
	@XmlElement(name = "law")
	private LawImFoodCosmetics lawFoodCosmetics;
	
	@XmlElement(name = "lawExp")
	private LawExpFoodCosmetics lawExpFoodCosmetics;
	
	@XmlElement(name = "lawSamplings")
	private List<LawSampling> LawSamplings;
	
	@XmlElement(name = "lawFiles")
	private List<LawFile> lawFiles;
	
	private String type;
	
	public LawImFoodCosmetics getLawFoodCosmetics() {
		return lawFoodCosmetics;
	}

	public void setLawFoodCosmetics(LawImFoodCosmetics lawFoodCosmetics) {
		this.lawFoodCosmetics = lawFoodCosmetics;
	}

	public List<LawSampling> getLawSamplings() {
		return LawSamplings;
	}

	public void setLawSamplings(List<LawSampling> lawSamplings) {
		LawSamplings = lawSamplings;
	}

	public LawExpFoodCosmetics getLawExpFoodCosmetics() {
		return lawExpFoodCosmetics;
	}

	public void setLawExpFoodCosmetics(LawExpFoodCosmetics lawExpFoodCosmetics) {
		this.lawExpFoodCosmetics = lawExpFoodCosmetics;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<LawFile> getLawFiles() {
		return lawFiles;
	}

	public void setLawFiles(List<LawFile> lawFiles) {
		this.lawFiles = lawFiles;
	}
	
}
