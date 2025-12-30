package com.mybus.busManagement.filter;

import com.mybus.busManagement.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        final String requestTokenHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwtToken = null;
        String role = null;
        Long userId = null;
        
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
                role = jwtUtil.getRoleFromToken(jwtToken);
                userId = jwtUtil.getUserIdFromToken(jwtToken);
            } catch (Exception e) {
                logger.warn("Unable to extract JWT Token information: " + e.getMessage());
            }
        }
        
        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtUtil.validateToken(jwtToken, username)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    
                    // Set user info in request attributes for easy access
                    request.setAttribute("username", username);
                    request.setAttribute("role", role);
                    request.setAttribute("userId", userId);
                } else {
                    logger.warn("JWT Token validation failed for user: " + username);
                }
            } catch (Exception e) {
                logger.error("Error validating JWT token: " + e.getMessage());
            }
        } else if (requestTokenHeader != null) {
            logger.warn("JWT Token present but username is null or authentication already exists");
        }
        chain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Don't filter auth endpoints
        return path.startsWith("/api/auth/");
    }
}

