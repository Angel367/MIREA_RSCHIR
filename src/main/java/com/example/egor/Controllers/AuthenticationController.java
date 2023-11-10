package com.example.egor.Controllers;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import com.example.egor.Controllers.DTO.LoginDTO;
import com.example.egor.Entities.Session;
import com.example.egor.Entities.User;
import com.example.egor.Repositories.SessionRepository;
import com.example.egor.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Key jwtSecretKey;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserRepository userRepository,
                                    SessionRepository sessionRepository,
                                    PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            log.info(loginDTO.getEmail() + loginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            // Generate JWT token
            String token = generateJwtToken(authentication);

            // Store session in the database
            saveSession(loginDTO.getEmail(), token);

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {
            // Validate and parse the JWT token
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);

            // Retrieve the session from the database
            Session session = sessionRepository.getSessionBySessionToken(token);

            // Check if the token is expired
            if (claims.getBody().getExpiration().before(new Date())) {
                // Handle expired token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
            }

            // Get the user details from the session
            User user = userRepository.findById(session.getUser().getId())
                    .orElseThrow(EntityNotFoundException::new);

            return ResponseEntity.ok("email:" + user.getEmail() +
                    "\nusername: " + user.getUsername() +
                    "\nRole: " + user.getRole());
        } catch (JwtException | EntityNotFoundException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
    public User getUserByToken(String token) {
        token = token.substring(7); // Remove "Bearer " prefix

        try {
            // Validate and parse the JWT token
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);

            // Retrieve the session from the database
            Session session = sessionRepository.getSessionBySessionToken(token);

            // Check if the token is expired
            if (claims.getBody().getExpiration().before(new Date())) {
                // Handle expired token
                return null;
            }

            // Get the user details from the session
            User user = userRepository.findById(session.getUser().getId())
                    .orElseThrow(EntityNotFoundException::new);

            return user;
        } catch (JwtException | EntityNotFoundException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    private String generateJwtToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L)) // Token expires in 24 hours
                .signWith(jwtSecretKey)
                .compact();
    }

    private void saveSession(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1); // Session expires in 24 hours

        Session session = new Session();
        session.setUser(user);
        session.setSessionToken(token);
        session.setExpiresAt(Timestamp.valueOf(expiresAt));

        sessionRepository.save(session);
    }
}