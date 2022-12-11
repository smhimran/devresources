package com.devreources.devresources.controllers.user;

import com.devreources.devresources.controllers.user.request.LoginRequest;
import com.devreources.devresources.controllers.user.request.SignupRequest;
import com.devreources.devresources.controllers.user.response.LoginFailedResponse;
import com.devreources.devresources.controllers.user.response.LoginSuccessfulResponse;
import com.devreources.devresources.controllers.user.response.SignupFailedResponse;
import com.devreources.devresources.controllers.user.response.SignupSuccessfulResponse;
import com.devreources.devresources.models.User;
import com.devreources.devresources.repositories.UserRepository;
import com.devreources.devresources.security.CustomUserDetailsService;
import com.devreources.devresources.security.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository repository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/me")
    public ResponseEntity<?> getUser(Principal principal) {
        if (principal != null) {
            Optional<User> user = repository.findByUsername(principal.getName());
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>() {{
                put("message", "You are not logged in!");
            }});
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> creatUser(@RequestBody SignupRequest request) {
        try {
            String currentPassword = request.getPassword();
            request.setPassword(new BCryptPasswordEncoder(16).encode(currentPassword));
            User user = new User(request.getName(), request.getUsername(), request.getEmail(), request.getPassword());
            repository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SignupSuccessfulResponse("User created successfully!", user));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SignupFailedResponse("User already exists!"));
        } catch (Exception e) {
            logger.error("Error creating request with username {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SignupFailedResponse("Signup failed!"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            logger.info("Bad credentials", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginFailedResponse("Authentication failed!"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginSuccessfulResponse("success", jwt));
    }
}
