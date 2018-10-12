
/**
 * @file  TripOrder.java
 * @brief 简单描述 福利管理对象VO
 *
 * - 版权: Copyright(C) 烽火星空通信发展有限有限公司(Starrysky)
 * - 功能: 本文件归属于【WelfarePackage】子系统【welfarepkg】模块
 *     -# 功能描述示例 福利管理对象VO
 * - 修订历史:
 * - --版本-----月/日/年---修订者-----------------------------------------
 * - v0.9.1.0    2016-08-04   wuj
 *     -# 修改 文件注释规范
 * - v0.9.0.0    2016-08-04   wuj
 *     -# 创建
 *
 * @bug TD-xx: 缺陷示例
 */
 
package com.fiberhome.mapps.trip.entity;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author  wuj
 */
@Table(name = "trip_order")
public class TripOrder implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    ///columns START
    /**
      *对应字段：ORDER_ID，字段含义：orderId
      */
    @Id
    @Column(name = "order_id")
    private String orderId;
    /**
      *对应字段：OUT_ORDER_ID，字段含义：outOrderId
      */
    @Column(name = "out_order_id")
    private String outOrderId;
    /**
      *对应字段：OUT_USER_ID，字段含义：outUserId
      */
    @Column(name = "out_user_id")
    private String outUserId;
    /**
      *对应字段：USER_ID，字段含义：userId
      */
    @Column(name = "user_id")
    private String userId;
    /**
      *对应字段：USER_NAME，字段含义：userName
      */
    @Column(name = "user_name")
    private String userName;
    /**
      *对应字段：BEGIN_DATE，字段含义：beginDate
      */
    @Column(name = "begin_date")
    private String beginDate;
    /**
      *对应字段：END_DATE，字段含义：endDate
      */
    @Column(name = "end_date")
    private String endDate;
    /**
      *对应字段：LINK_TEL，字段含义：linkTel
      */
    @Column(name = "link_tel")
    private String linkTel;
    /**
      *对应字段：HOTEL_NAME，字段含义：hotelName
      */
    @Column(name = "hotel_name")
    private String hotelName;
    /**
      *对应字段：HOTEL_ADDRESS，字段含义：hotelAddress
      */
    @Column(name = "hotel_address")
    private String hotelAddress;
    /**
      *对应字段：HOTEL_LOCATION，字段含义：hotelLocation
      */
    @Column(name = "hotel_location")
    private String hotelLocation;
    /**
      *对应字段：ROOM_TYPE，字段含义：roomType
      */
    @Column(name = "room_type")
    private String roomType;
    /**
      *对应字段：ROOM_NUMBER，字段含义：roomNumber
      */
    @Column(name = "room_number")
    private java.lang.Integer roomNumber;
    /**
      *对应字段：HAS_BREAKFAST，字段含义：hasBreakfast
      */
    @Column(name = "has_breakfast")
    private String hasBreakfast;
    /**
      *对应字段：PRICE，字段含义：price
      */
    @Column(name = "price")
    private BigDecimal price;
    /**
      *对应字段：PAY_TYPE，字段含义：payType
      */
    @Column(name = "pay_type")
    private String payType;
    /**
      *对应字段：ORDER_STATUS，字段含义：orderStatus
      */
    @Column(name = "order_status")
    private String orderStatus;
    /**
      *对应字段：CREATE_DATE，字段含义：createDate
      */
    @Column(name = "create_date")
    private Date createDate;

    /**
      *对应字段：UPDATE_DATE，字段含义：updateDate
      */
    @Column(name = "update_date")
    private Date updateDate;

    /**
      *对应字段：CANCAL_DATE，字段含义：cancalDate
      */
    @Column(name = "cancal_date")
    private Date cancalDate;

    /**
      *对应字段：ORG_ID，字段含义：orgId
      */
    @Column(name = "org_id")
    private String orgId;
    /**
      *对应字段：ORG_NAME，字段含义：orgName
      */
    @Column(name = "org_name")
    private String orgName;
    
    @Column(name = "hotel_tel")
    private String hotelTel;
    
    @Column(name = "hotel_img")
    private String hotelImg;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "hotel_id")
    private String hotelId;
    
    ///columns END
    
    public String getHotelTel() {
		return hotelTel;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public void setHotelTel(String hotelTel) {
		this.hotelTel = hotelTel;
	}

	public String getHotelImg() {
		return hotelImg;
	}

	public void setHotelImg(String hotelImg) {
		this.hotelImg = hotelImg;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
     
    public String getOrderId()
    {
        return this.orderId;
    }
    public void setOutOrderId(String outOrderId)
    {
        this.outOrderId = outOrderId;
    }
     
    public String getOutOrderId()
    {
        return this.outOrderId;
    }
    public void setOutUserId(String outUserId)
    {
        this.outUserId = outUserId;
    }
     
    public String getOutUserId()
    {
        return this.outUserId;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
     
    public String getUserId()
    {
        return this.userId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
     
    public String getUserName()
    {
        return this.userName;
    }
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }
     
    public String getBeginDate()
    {
        return this.beginDate;
    }
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
     
    public String getEndDate()
    {
        return this.endDate;
    }
    public void setLinkTel(String linkTel)
    {
        this.linkTel = linkTel;
    }
     
    public String getLinkTel()
    {
        return this.linkTel;
    }
    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }
     
    public String getHotelName()
    {
        return this.hotelName;
    }
    public void setHotelAddress(String hotelAddress)
    {
        this.hotelAddress = hotelAddress;
    }
     
    public String getHotelAddress()
    {
        return this.hotelAddress;
    }
    public void setHotelLocation(String hotelLocation)
    {
        this.hotelLocation = hotelLocation;
    }
     
    public String getHotelLocation()
    {
        return this.hotelLocation;
    }
    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }
     
    public String getRoomType()
    {
        return this.roomType;
    }
    public void setRoomNumber(java.lang.Integer roomNumber)
    {
        this.roomNumber = roomNumber;
    }
     
    public java.lang.Integer getRoomNumber()
    {
        return this.roomNumber;
    }
    public void setHasBreakfast(String hasBreakfast)
    {
        this.hasBreakfast = hasBreakfast;
    }
     
    public String getHasBreakfast()
    {
        return this.hasBreakfast;
    }

    public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setPayType(String payType)
    {
        this.payType = payType;
    }
     
    public String getPayType()
    {
        return this.payType;
    }
    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }
     
    public String getOrderStatus()
    {
        return this.orderStatus;
    }

    /**
      * @brief 功能:设置 createDate时间
      * @param createDate createDate时间
      */
    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }
     
    public Date getCreateDate()
    {
        return this.createDate;
    }

    /**
      * @brief 功能:设置 updateDate时间
      * @param updateDate updateDate时间
      */
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }
     
    public Date getUpdateDate()
    {
        return this.updateDate;
    }

    /**
      * @brief 功能:设置 cancalDate时间
      * @param cancalDate cancalDate时间
      */
    public void setCancalDate(Date cancalDate)
    {
        this.cancalDate = cancalDate;
    }
     
    public Date getCancalDate()
    {
        return this.cancalDate;
    }
     
    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }
     
    public String getOrgId()
    {
        return this.orgId;
    }
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }
     
    public String getOrgName()
    {
        return this.orgName;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }

 
}

