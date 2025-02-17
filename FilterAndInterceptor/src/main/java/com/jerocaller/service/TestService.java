package com.jerocaller.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
	
	/**
	 * 일부러 지연시간을 부여하여 시간을 끈다. 
	 * 
	 * @param noIter
	 * @throws InterruptedException 
	 */
	public void delay(long sleepMillSeconds) throws InterruptedException {
		
		Thread.sleep(sleepMillSeconds);
		
	}

}
