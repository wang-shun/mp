package com.fiberhome.mapps.trip.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class HotelInfoRequest extends AbstractRopRequest {
	@NotNull
	private String hotelID;

	public String getHotelID() {
		return hotelID;
	}

	public void setHotelID(String hotelID) {
		this.hotelID = hotelID;
	}

}
