package com.jerocaller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients  // Feign 사용을 위해 application 단에서 설정.
public class OpenFeignTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenFeignTestApplication.class, args);
	}

}
