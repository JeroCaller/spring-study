package com.jerocaller.exception;

import com.jerocaller.common.CustomResponseCode;

public class FileNotFoundException extends CustomException {
	
	private static final long serialVersionUID = 4822375173773667798L;

	public FileNotFoundException() {
		super(CustomResponseCode.FILE_NOT_FOUND);
	}

}
