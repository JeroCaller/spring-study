package com.jerocaller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jerocaller.model.UserDto;

@Controller
public class RestTestController {
	
	// View 사용 시의 예시. 
	@GetMapping("/withViewTest")
	public String useView(Model model) {
		UserDto dto = new UserDto();
		dto.setName("kimquel");
		dto.setJob("개발자");
		dto.setAge(30);
		
		model.addAttribute("data", dto);
		return "withView";
	}
	
	@GetMapping("/restTest")
	@ResponseBody
	public UserDto userJsonData() {
		UserDto dto = new UserDto();
		dto.setName("kimquel");
		dto.setJob("개발자");
		dto.setAge(30);
		
		return dto;
	}
	
	@GetMapping("/restTest2")
	@ResponseBody
	public String strJsonData() {
		return "hi";
	}
	
	@GetMapping("/restTest3")
	@ResponseBody
	public String strJsonData2() {
		return "{\"name\":\"jeongdb\",\"job\":\"요리사\",\"age\":29}";
	}
	
	// HTTP Header의 Content-type을 json으로 설정.
	@GetMapping(value = "/restTest4", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String strJsonData3() {
		return "{\"name\":\"jeongdb\",\"job\":\"요리사\",\"age\":29}";
	}
}
