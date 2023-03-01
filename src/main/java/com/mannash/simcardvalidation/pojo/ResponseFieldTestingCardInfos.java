package com.mannash.simcardvalidation.pojo;

import com.mannash.simcardvalidation.pojo.ResponseFieldTestingCardPojo;
import com.mannash.simcardvalidation.pojo.ResponseGenericPojo;

import java.util.List;

public class ResponseFieldTestingCardInfos extends ResponseGenericPojo {

	@Override
	public String toString() {
		return "ResponseFieldTestingCardInfos [responseFieldTestingCardPojos=" + responseFieldTestingCardPojos + "]";
	}

	private List<ResponseFieldTestingCardPojo> responseFieldTestingCardPojos;

	public List<ResponseFieldTestingCardPojo> getResponseFieldTestingCardPojos() {
		return responseFieldTestingCardPojos;
	}

	public void setResponseFieldTestingCardPojos(List<ResponseFieldTestingCardPojo> responseFieldTestingCardPojos) {
		this.responseFieldTestingCardPojos = responseFieldTestingCardPojos;
	}
}
