package com.jerocaller.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.jerocaller.data.dto.request.CommentRequest;
import com.jerocaller.data.dto.response.CommentResponse;
import com.jerocaller.data.entity.Comment;
import com.jerocaller.data.entity.Member;
import com.jerocaller.data.repository.CommentRepository;
import com.jerocaller.util.AuthUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final AuthUtil authUtil;
    private final UserDetailsService userDetailsService;
    
    public List<CommentResponse> getMyCommentsAll() {
        
        Member me = getCurrentMember();
        
        List<Comment> comments = commentRepository.findByMember(me);
        
        return comments.stream()
            .map(CommentResponse :: toDto)
            .collect(Collectors.toList());
    }
    
    public CommentResponse getMyOne(int id) {
        
        String username = authUtil.getAuth().getName();
        Comment comment = commentRepository.findByIdAndUsername(id, username)
            .orElseThrow(() -> new EntityNotFoundException("해당 댓글은 존재하지 않습니다."));
        
        return CommentResponse.toDto(comment);
    }
    
    @Transactional
    public CommentResponse create(CommentRequest commentRequest) {
        
        Member me = getCurrentMember();
        
        Comment newComment = toEntityForInserting(commentRequest, me);
        Comment savedComment = commentRepository.save(newComment);
        
        return CommentResponse.toDto(savedComment);
    }
    
    @Transactional
    public CommentResponse update(CommentRequest commentRequest) {
        
        Member me = getCurrentMember();
        if (!commentRepository.existsByIdAndMember(commentRequest.getId(), me)) {
            throw new EntityNotFoundException("해당 Id 및 현재 유저 정보와 일치하는 댓글 정보 없음.");
        }
        
        Comment comment = toEntityForUpdating(commentRequest, me);
        Comment updatedComment = commentRepository.save(comment);
        
        return CommentResponse.toDto(updatedComment);
    }
    
    @Transactional
    public void deleteOne(int id) {
        
        Member me = getCurrentMember();
        if (!commentRepository.existsByIdAndMember(id, me)) {
            throw new EntityNotFoundException("해당 Id 및 현재 유저 정보와 일치하는 댓글 정보 없음.");
        }
        
        commentRepository.deleteById(id);
        
    }
    
    private Comment toEntityForInserting(
        CommentRequest commentRequest, 
        Member member
    ) {
        return Comment.builder()
            .comment(commentRequest.getComment())
            .member(member)
            .build();
    }
    
    private Comment toEntityForUpdating(
        CommentRequest commentRequest, 
        Member member
    ) {
        return Comment.builder()
            .id(commentRequest.getId())
            .comment(commentRequest.getComment())
            .member(member)
            .build();
    }
    
    private Member getCurrentMember() {
        
        String username = authUtil.getAuth().getName();
        Member me = (Member) userDetailsService
            .loadUserByUsername(username);
        return me;
    }
    
}
