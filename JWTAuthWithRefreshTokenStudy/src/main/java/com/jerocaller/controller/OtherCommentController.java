package com.jerocaller.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.business.OtherCommentService;
import com.jerocaller.common.RoleNames;
import com.jerocaller.data.dto.response.CommentResponse;
import com.jerocaller.data.dto.response.RestResponse;

import lombok.RequiredArgsConstructor;

/**
 * 다른 이들의 댓글 조회 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments/others")
public class OtherCommentController {
    
    private final OtherCommentService otherCommentService;
    
    @GetMapping("/users")
    public ResponseEntity<RestResponse> getAllUsers() {
        return commonLogic(RoleNames.USER);
    }
    
    @GetMapping("/staffs")
    public ResponseEntity<RestResponse> getAllStaffs() {
        return commonLogic(RoleNames.STAFF);
    }
    
    @GetMapping("/admins")
    public ResponseEntity<RestResponse> getAllAdmins() {
        return commonLogic(RoleNames.ADMIN);
    }
    
    private ResponseEntity<RestResponse> commonLogic(String role) {
        
        List<CommentResponse> comments = otherCommentService
            .getAllByRole(role);
            
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("조회 성공")
            .data(comments)
            .build()
            .toResponse();
    }
    
}
