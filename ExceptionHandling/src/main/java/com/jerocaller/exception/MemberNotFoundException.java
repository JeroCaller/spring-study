package com.jerocaller.exception;

import com.jerocaller.common.CustomResponseCode;

public class MemberNotFoundException extends CustomException {

	private static final long serialVersionUID = 3822901348668702169L;
	
	public MemberNotFoundException() {
		super(CustomResponseCode.MEMBER_NOT_FOUND);
	}

}
