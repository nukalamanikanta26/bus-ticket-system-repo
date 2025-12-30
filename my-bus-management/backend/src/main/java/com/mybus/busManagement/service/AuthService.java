package com.mybus.busManagement.service;

import com.mybus.busManagement.dto.AuthResponseDTO;
import com.mybus.busManagement.dto.LoginRequestDTO;
import com.mybus.busManagement.dto.SignupRequestDTO;
import com.mybus.busManagement.entity.User;
import com.mybus.busManagement.exception.BusinessException;
import com.mybus.busManagement.repository.UserRepository;
import com.mybus.busManagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    
    // Fixed admin credentials
    private static final String ADMIN_EMAIL = "admin@bus.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_NAME = "Admin User";
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // Signup - create new user
    public AuthResponseDTO signup(SignupRequestDTO signupRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        
        // Create new user
        User user = new User(
            signupRequest.getName(),
            signupRequest.getEmail(),
            signupRequest.getPassword(), // In production, hash this password
            signupRequest.getPhone()
        );
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(
            savedUser.getEmail(),
            savedUser.getRole(),
            savedUser.getId()
        );
        
        return new AuthResponseDTO(
            token,
            savedUser.getEmail(),
            savedUser.getName(),
            savedUser.getRole(),
            savedUser.getId()
        );
    }
    
    // Login
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        // Check for admin login
        if (ADMIN_EMAIL.equalsIgnoreCase(loginRequest.getEmail()) && 
            ADMIN_PASSWORD.equals(loginRequest.getPassword())) {
            
            String token = jwtUtil.generateToken(ADMIN_EMAIL, "ADMIN", 0L);
            return new AuthResponseDTO(
                token,
                ADMIN_EMAIL,
                ADMIN_NAME,
                "ADMIN",
                0L
            );
        }
        
        // Check for regular user login
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new BusinessException("Invalid email or password"));
        
        // Simple password check (in production, use password hashing)
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(
            user.getEmail(),
            user.getRole(),
            user.getId()
        );
        
        return new AuthResponseDTO(
            token,
            user.getEmail(),
            user.getName(),
            user.getRole(),
            user.getId()
        );
    }
    
    // Get user by email
    public User getUserByEmail(String email) {
        if (ADMIN_EMAIL.equalsIgnoreCase(email)) {
            // Return a virtual admin user
            User admin = new User();
            admin.setEmail(ADMIN_EMAIL);
            admin.setName(ADMIN_NAME);
            admin.setRole("ADMIN");
            admin.setId(0L);
            return admin;
        }
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("User not found"));
    }
}

