package com.jerocaller.requestbean;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * 사용자 요청 정보를 repository에 잘 전달할게끔 
 * 요청을 재가공한다. 
 */
public class ReprocessRequestOfPage {
	private final int PAGE_SIZE = 3;
	
	public Pageable getReprocessedPageRequest(PageRequestBean pRequest) {
		// 정렬 및 페이지 요청은 서비스에서 자체적으로 정하기보단
		// 컨트롤러 단에서 서비스로 요청하게끔 하여 조금 더 유연하고 동적인 요청을 가능하게끔
		// 하는 것이 좋지 않을까란 생각에 정렬 및 페이지 요청 코드는 컨트롤러 단에서 해보았다.
		Sort pageSort = Sort.by(
				Order.asc("userClassInfo.classNumber"), 
				Order.desc("averPurchase"),
				Order.desc("mileage")
		);

		// 페이지네이션 블록 개수가 유지되는지 보고자 한다면
		// 두 번째 인자를 3으로 설정.
		Pageable pageRequest = PageRequest.of(
				pRequest.getSelectedPage() - 1, 
				PAGE_SIZE,
				pageSort
		);
		return pageRequest;
	}
}
