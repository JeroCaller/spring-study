package com.jerocaller.service.interf;

import java.util.List;

public interface FileServiceInterface<Service> {
	
	/**
	 * 주어진 회원 Id를 토대로 해당 회원이 보유한 모든 파일들의 Id를 반환.
	 * 
	 * @param memberId
	 * @return
	 */
	List<Integer> getFilesIdByMember(int memberId);

}
