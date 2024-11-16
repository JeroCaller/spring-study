package com.jerocaller.requestbean;

import lombok.Getter;
import lombok.Setter;

/**
 * 원래 여러 개의 요청 정보가 있을 것으로 기대하여 
 * 이렇게 form bean 객체를 정의하였지만 
 * 막상 만들고 나니 요청 정보가 하나뿐이지만 
 * 이걸 지우고 다시 컨트롤러 단에서 requestParam으로 
 * 받는 식으로 고치면 나중에 또 요청 정보 추가할 일이 
 * 생길까봐 이렇게 남긴다. 
 */
@Getter
@Setter
public class PageRequestBean {
	private Integer selectedPage = 1;
}
