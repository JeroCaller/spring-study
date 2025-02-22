package com.jerocaller.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jerocaller.data.dto.response.CommentResponse;
import com.jerocaller.data.entity.Comment;
import com.jerocaller.data.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

/**
 * 다른 이들의 댓글에 대한 작업을 위한 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class OtherCommentService {
    
    private final CommentRepository commentRepository;
    
    public List<CommentResponse> getAllByRole(String role) {
        
        List<Comment> comments = commentRepository.findByRole(role);
        
        return comments.stream()
            .map(CommentResponse :: toDto)
            .collect(Collectors.toList());
    }
    
}
