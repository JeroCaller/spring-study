package com.jerocaller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @FeignClient
 * 
 * name 또는 value : 필수. 그런데 아무렇게나 지어도 되는 것으로 파악됨. 지정하지 않으면 에러 뜸.
 * url : 외부 API의 domain URL
 * 
 * 인터페이스 내부에서는 Controller에서 사용하던 @GetMapping 등의 어노테이션을 그대로 사용할 수 있다. 
 */
@FeignClient(name = "myFeign" , url = "https://jsonplaceholder.typicode.com")
public interface MyFeignInterface {
	
	@GetMapping("/posts/{id}")
	Object getOnePost(@PathVariable("id") Integer id);
	
	@GetMapping("/comments/{id}")
	Object getOneComment(@PathVariable("id") Integer id);
}
