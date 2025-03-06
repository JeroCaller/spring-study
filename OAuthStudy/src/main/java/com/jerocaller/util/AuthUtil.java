package com.jerocaller.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jerocaller.common.RoleNames;

@Component
public class AuthUtil {
    
    public UserDetails getCurrentUserInfo() {
        
        Authentication auth = SecurityContextHolder
            .getContext()
            .getAuthentication();
        
        return (UserDetails) auth.getPrincipal();
    }
    
    public boolean isAnonymous() {
        
        Authentication auth = SecurityContextHolder
            .getContext()
            .getAuthentication();
        
        return processIsAnonymous(auth);
    }
    
    public boolean isAnonymous(Authentication auth) {
        return processIsAnonymous(auth);
    }
    
    private boolean processIsAnonymous(Authentication auth) {
        
        if (auth == null || !auth.isAuthenticated()) {
            return true;
        }
        
        for (GrantedAuthority authz : auth.getAuthorities()) {
            if (authz.getAuthority().contains(RoleNames.ANONYMOUS)) {
                return true;
            }
        }
        return false; 
    }
    
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
}
