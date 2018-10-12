package com.fiberhome.mapps.ydzf.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fiberhome.mapps.ydzf.entity.GoodsDetail;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.ydzf.entity.ContainerDetail;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class QueryGoodsDetailResponse extends BaseResponse {
	@XmlElement(name = "goodsDetails")
	private List<GoodsDetail> goodsDetails;
	@XmlElement(name = "containerDetails")
	private List<ContainerDetail> containerDetails;

	public List<GoodsDetail> getGoodsDetails() {
		return goodsDetails;
	}

	public void setGoodsDetails(List<GoodsDetail> goodsDetails) {
		this.goodsDetails = goodsDetails;
	}

	public List<ContainerDetail> getContainerDetails() {
		return containerDetails;
	}

	public void setContainerDetails(List<ContainerDetail> containerDetails) {
		this.containerDetails = containerDetails;
	}
}
