package backend.g5.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import backend.g5.dto.LoginRequest;
import backend.g5.dto.RegisterRequest;
import backend.g5.dto.UserResponse;
import backend.g5.entity.User;
import backend.g5.repository.AuthRepository;

@Service
public class AuthService {
    
    @Autowired
    private AuthRepository authRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Simple in-memory session storage (for demo purposes)
    private Map<String, Integer> activeSessions = new HashMap<>();

    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        // Check if email already exists
        if (authRepository.existsByEmail(request.getEmail())) {
            response.put("success", false);
            response.put("message", "Email already registered");
            return response;
        }
        
        // Create new user with encrypted password
        User user = new User();
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = authRepository.save(user);
        
        response.put("success", true);
        response.put("message", "User registered successfully");
        response.put("user", new UserResponse(
            savedUser.getUserID(),
            savedUser.getFullname(),
            savedUser.getEmail()
        ));
        
        return response;
    }

    public Map<String, Object> authenticate(LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOptional = authRepository.findByEmail(request.getEmail());
        
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Invalid email or password");
            return response;
        }
        
        User user = userOptional.get();
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid email or password");
            return response;
        }
        
        // Generate session token
        String sessionToken = UUID.randomUUID().toString();
        activeSessions.put(sessionToken, user.getUserID());
        
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("token", sessionToken);
        response.put("user", new UserResponse(
            user.getUserID(),
            user.getFullname(),
            user.getEmail()
        ));
        
        return response;
    }

    public void invalidateSession(String token) {
        activeSessions.remove(token);
    }

    public Optional<User> getUserByToken(String token) {
        Integer userId = activeSessions.get(token);
        if (userId != null) {
            return authRepository.findById(userId);
        }
        return Optional.empty();
    }
}
