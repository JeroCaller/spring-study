package com.jerocaller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jerocaller.controller.formbeans.UserBean;

@Controller
public class RequestParamController {
	
	@GetMapping("/login")
	public String loginProcess(
			@RequestParam("id") String id, 
			@RequestParam("password") String password,
			Model model
	) {
		// key 이름과 value 이름이 꼭 같을 필요는 없음.
		// key, value 모두 "id"인 건 우연일 뿐.
		model.addAttribute("id", id);
		model.addAttribute("pw", password);
		return "/param/showParam";
	}
	
	@GetMapping("/userinfo/name/{userName}/job/{userJob}")
	public String extractUserInfo(
			@PathVariable String userName,
			@PathVariable String userJob, 
			Model model
	) {
		model.addAttribute("name", userName);
		model.addAttribute("job", userJob);
		return "/param/userInfo";
	}
	
	@GetMapping("/userinfo")
	public String goToUserPage() {
		return "/param/userMain";
	}
	
	@PostMapping("/userinfo")
	public String showUserInfo(UserBean bean, Model model) {
		model.addAttribute("userData", bean);
		return "/param/userInfoWithBean";
	}
}
