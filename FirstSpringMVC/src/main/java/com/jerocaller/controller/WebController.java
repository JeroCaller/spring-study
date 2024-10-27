package com.jerocaller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jerocaller.model.ProductDto;
import com.jerocaller.model.UserDto;
import com.jerocaller.model.WebDao;

@Controller
public class WebController {
	
	@Autowired
	private WebDao dao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	/**
	 * RequestMapping(value = "이동할 페이지의 URI", method = 처리할 HTTP 메서드)
	 * 
	 * Controller 클래스에 @Controller 어노테이션을 붙이면 SSR 방식으로 응답 "페이지"를 
	 * 사용자에게 보여준다. 이 때 컨트롤러 내 메서드에서는 String 반환값을 가지며, 
	 * 반환값은 이동할 페이지의 URI을 입력한다. 
	 * 
	 * @return
	 */
	public String goToIndex() {
		// 포워딩 방식으로 index.html로 이동.
		// "forward:/index"의 축약형
		// 만약 redirect 방식으로 이동하고자 한다면 
		// "redirect:/index"와 같이 명시해야 한다. 
		return "index";
	}
	
	@PostMapping("/login")
	public String login(
			@RequestParam("id") String id, 
			@RequestParam("password") String password,
			Model model
		) 
	{
		UserDto selectedUser = dao.getOneUser(id, password);
		if (selectedUser == null) {
			return "loginFailed";
		}
		
		// 유저 정보를 싣고 View로 전달. 
		// Servlet에서의 request.setAttribute()와 동일.
		model.addAttribute("user", selectedUser);
		
		List<ProductDto> products = dao.getAllProducts();
		model.addAttribute("products", products);
		
		return "products";
	}
}
