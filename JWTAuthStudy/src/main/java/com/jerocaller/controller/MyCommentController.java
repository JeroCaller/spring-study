package com.jerocaller.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.business.CommentService;
import com.jerocaller.data.dto.request.CommentRequest;
import com.jerocaller.data.dto.response.CommentResponse;
import com.jerocaller.data.dto.response.RestResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comments/my")
public class MyCommentController {
    
    private final CommentService commentService;
    
    @GetMapping
    public ResponseEntity<RestResponse> getMyCommentsAll() {
        
        List<CommentResponse> comments = commentService.getMyCommentsAll();
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("현재 사용자의 전체 댓글 조회 성공")
            .data(comments)
            .build()
            .toResponse();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse> getMyOneComment(
        @PathVariable("id") int id
    ) {
        
        CommentResponse comment = commentService.getMyOne(id);
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("댓글 한 건 조회 성공")
            .data(comment)
            .build()
            .toResponse();
    }
    
    @PostMapping
    public ResponseEntity<RestResponse> registerComment(
        @RequestBody CommentRequest commentRequest
    ) {
        
        CommentResponse result = commentService.create(commentRequest);
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.CREATED)
            .message("댓글 한 건 등록 성공")
            .data(result)
            .build()
            .toResponse();
    }
    
    @PutMapping
    public ResponseEntity<RestResponse> updateComment(
        @RequestBody CommentRequest commentRequest
    ) {
        
        CommentResponse result = commentService.update(commentRequest);
        
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("수정 성공")
            .data(result)
            .build()
            .toResponse();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse> deleteOne(
        @PathVariable("id") int id
    ) {
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("댓글 한 건 삭제 완료")
            .build()
            .toResponse();
    }
    
}
