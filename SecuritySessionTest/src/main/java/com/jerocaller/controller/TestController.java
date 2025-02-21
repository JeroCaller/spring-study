package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.MembersResponse;
import com.jerocaller.dto.ResponseJson;
import com.jerocaller.service.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    
    private final TestService testService;
    
    @GetMapping("/something")
    public ResponseEntity<ResponseJson> doSomething() {
        
        ResponseJson responseJson = null;
        MembersResponse response = null;
        
        try {
            response = (MembersResponse) testService
                .doSomethingWithOnlyAuthed();
        } catch (Exception e) {
            responseJson = ResponseJson.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
        }
        
        if (responseJson == null) {
            responseJson = ResponseJson.builder()
                .httpStatus(HttpStatus.OK)
                .message("인증 여부 확인 성공 후 작업 실행됨.")
                .data(response)
                .build();
        }

        return responseJson.toResponse();
    }
}
