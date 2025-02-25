package com.jerocaller.data.dto.request;

import com.jerocaller.common.RoleNames;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class MemberRequest {
    
    @NotBlank
    @Size(min = 2, max = 20)
    private String username;
    
    @NotBlank
    private String password;
    
    @Builder.Default
    private String role = RoleNames.USER;
    
}
