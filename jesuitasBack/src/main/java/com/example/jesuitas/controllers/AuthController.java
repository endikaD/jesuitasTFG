package com.example.jesuitas.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.jesuitas.models.ERole;
import com.example.jesuitas.repository.RoleRepository;
import com.example.jesuitas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jesuitas.models.Role;
import com.example.jesuitas.models.User;
import com.example.jesuitas.payload.request.LoginRequest;
import com.example.jesuitas.payload.request.SignupRequest;
import com.example.jesuitas.payload.response.JwtResponse;
import com.example.jesuitas.payload.response.MessageResponse;
import com.example.jesuitas.security.jwt.JwtUtils;
import com.example.jesuitas.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Nombre de usuario en uso"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email en uso"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findRoleByName(ERole.ROLE_ADMIN)
          .orElseThrow(() -> new RuntimeException("Error: Role no encontrado"));
      roles.add(userRole);
//      Role userRole = roleRepository.findRoleByName(ERole.ROLE_MODERATOR)
//              .orElseGet(() -> {
//                Role defaultRole = new Role(ERole.ROLE_USER);
//                roleRepository.save(defaultRole);
//                return defaultRole;
//              });
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findRoleByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role no encontrado"));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findRoleByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role no encontrado"));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findRoleByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role no encontrado"));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Usuario registrado!"));
  }
}
