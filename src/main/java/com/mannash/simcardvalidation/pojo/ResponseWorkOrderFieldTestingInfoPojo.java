package com.mannash.simcardvalidation.pojo;

import com.mannash.simcardvalidation.pojo.ResponseWorkOrderFieldTestingPojo;

import java.util.List;

public class ResponseWorkOrderFieldTestingInfoPojo {

	private List<com.mannash.simcardvalidation.pojo.ResponseWorkOrderFieldTestingPojo> responseWorkOrderFieldTestingPojo;

	public List<com.mannash.simcardvalidation.pojo.ResponseWorkOrderFieldTestingPojo> getResponseWorkOrderFieldTestingPojo() {
		return responseWorkOrderFieldTestingPojo;
	}

	public void setResponseWorkOrderFieldTestingPojo(List<ResponseWorkOrderFieldTestingPojo> responseWorkOrderFieldTestingPojo) {
		this.responseWorkOrderFieldTestingPojo = responseWorkOrderFieldTestingPojo;
	}

	@Override
	public String toString() {
		return responseWorkOrderFieldTestingPojo.toString();
	}
	
}
