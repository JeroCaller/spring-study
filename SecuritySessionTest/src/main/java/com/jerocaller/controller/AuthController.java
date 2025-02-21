package com.jerocaller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.MembersRequest;
import com.jerocaller.dto.MembersResponse;
import com.jerocaller.dto.ResponseJson;
import com.jerocaller.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<ResponseJson> login(
        @RequestBody MembersRequest request
    ) {
        
        ResponseJson responseJson = null;
        
        MembersResponse response = null;
        
        try {
            response = authService.login(request);
        } catch (Exception e) {
            responseJson = ResponseJson.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
        }
        
        if (responseJson == null) {
            responseJson = ResponseJson.builder()
                .httpStatus(HttpStatus.OK)
                .message("로그인 성공")
                .data(response)
                .build();
        }

        return responseJson.toResponse();
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
