package com.jerocaller.responsebean;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Slf4j
public class PageResponseBean<T> {
	private Integer startPage;
	private Integer endPage;
	private Integer currentPage;
	private Integer blockNumToShow;
	
	/**
	 * 
	 * @param pageInfo
	 * @param blockNumToShow - 홀수 권장
	 */
	public PageResponseBean(Page<T> pageInfo, Integer blockNumToShow) {
		this.blockNumToShow = blockNumToShow;
		
		// 현재 선택된 페이지 블록이 페이지네이션에서 정 가운데에 오도록 한다. 
		Integer halfBlocks = (blockNumToShow / 2);
		
		currentPage = pageInfo.getNumber() + 1;
		
		Integer startDiff = currentPage - halfBlocks;
		
		// 기본 로직.
		// 예를 들어 halfBlock = 2 라 하고
		// currentPage = 4라고 한다면
		// startPage = Math.max(currentPage - 2, 1)
		// 라고 하면 startPage = 2가 된다. 
		// 만약 현재 선택된 페이지가 1에 가까워 왼쪽에 더 표시할 
		// 페이지 블록이 없을 경우를 대비해 max()를 사용하였으며, 
		// 두 번째 인자로 1번째 페이지를 지정하도록 하였다. 
		// endPage = Math.min(currentPage + 2, totalPage)
		// 라 하면 totalPage = 8일 때 endPage = 6이 된다. 
		// 이 역시도 현재 선택된 페이지가 totalPage에 가까울 때 
		// 더 이상 오른쪽에 표시할 페이지 블록이 없을 때 대신 끝 
		// 페이지가 표시되게끔 한 것이다. 
		// 위 예에 따르면 startPage = 2, endPage = 6, 
		// currentPage = 4가 되므로 화면에 표시되는 페이지 번호들은 
		// (이전) 2, 3, [4], 5, 6 (다음) 이 되어 총 5개의 페이지 블록이 보이면서도 
		// 현재 선택된 페이지 번호가 정 가운데에 놓이도록 할 수 있다. 
		
		Integer maxFormular = startDiff;
		Integer minFormular = currentPage + halfBlocks;
		
		// 페이지 블록 개수가 일정하게 표시되도록 하기 위한 로직. 
		// 아래 로직이 없다면 1 또는 끝 페이지에 가까울 때 
		// 정해진 블록 개수보다 더 적게 화면에 표시되는 버그가 나타난다. 
		if (startDiff <= 0) {
			minFormular = blockNumToShow;
		} else if (startDiff > (pageInfo.getTotalPages() / 2)) {
			maxFormular = pageInfo.getTotalPages() - blockNumToShow + 1;
		}
		
		// 페이지네이션에서 맨 왼쪽에 표시될 페이지 번호
		startPage = Math.max(maxFormular, 1);
		
		// 페이지네이션에서 맨 오른쪽에 표시될 페이지 번호.
		endPage = Math.min(minFormular, pageInfo.getTotalPages());
		
		log.info("startPage : " + startPage);
		log.info("endPage : " + endPage);
		log.info("currentPage: " + currentPage);
	}
}
