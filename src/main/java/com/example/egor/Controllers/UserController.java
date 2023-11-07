package com.example.egor.Controllers;

import com.example.egor.Controllers.DTO.UserRegistrationDTO;
import com.example.egor.Entities.Role;
import com.example.egor.Entities.User;
import com.example.egor.Repositories.RoleRepository;
import com.example.egor.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (!emailPattern.matcher(registrationDTO.getEmail()).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));
        Role role = roleRepository.findByName(registrationDTO.getRole());
        if (role == null) {
            Role newRole = new Role();
            newRole.setName(registrationDTO.getRole());
            roleRepository.save(newRole);
            newUser.setRole(roleRepository.findByName(registrationDTO.getRole()));
        }
        else {
            newUser.setRole(role);
        }

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        newUser.setCreatedAt(timestamp);
        newUser.setUpdatedAt(timestamp);

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }
}