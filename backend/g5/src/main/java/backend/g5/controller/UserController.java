package backend.g5.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.g5.dto.UserResponse;
import backend.g5.entity.User;
import backend.g5.service.AuthService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOptional = authService.getUserByToken(token);
        
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        User user = userOptional.get();
        response.put("success", true);
        response.put("user", new UserResponse(
            user.getUserID(),
            user.getFullname(),
            user.getEmail()
        ));
        
        return ResponseEntity.ok(response);
    }
}
