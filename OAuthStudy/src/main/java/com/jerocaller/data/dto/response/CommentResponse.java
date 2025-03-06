package com.jerocaller.data.dto.response;

import com.jerocaller.data.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    
    private Integer id;
    private String comment;
    private String writer;
    private String writerRole;
    
    public static CommentResponse toDto(Comment entity) {
        return CommentResponse.builder()
            .id(entity.getId())
            .comment(entity.getComment())
            .writer(entity.getMember().getUsername())
            .writerRole(entity.getMember().getRole())
            .build();
    }
    
}
