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
		
		Integer halfBlocks = (blockNumToShow / 2);
		
		currentPage = pageInfo.getNumber() + 1;
		
		Integer startDiff = currentPage - halfBlocks;
		Integer maxFormular = startDiff;
		Integer minFormular = currentPage + halfBlocks;
		
		// 페이지 블록 개수가 일정하게 표시되도록 하기 위한 로직. 
		if (startDiff <= 0) {
			minFormular = blockNumToShow;
		} else if (startDiff > (pageInfo.getTotalPages() / 2)) {
			maxFormular = pageInfo.getTotalPages() - blockNumToShow + 1;
		}
		
		startPage = Math.max(maxFormular, 1);
		endPage = Math.min(minFormular, pageInfo.getTotalPages());
		
		log.info("startPage : " + startPage);
		log.info("endPage : " + endPage);
		log.info("currentPage: " + currentPage);
	}
}
