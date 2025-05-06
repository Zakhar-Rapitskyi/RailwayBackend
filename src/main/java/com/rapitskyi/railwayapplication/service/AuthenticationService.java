package com.rapitskyi.railwayapplication.service;

import com.rapitskyi.railwayapplication.dto.AuthenticationDTOs.JwtResponse;
import com.rapitskyi.railwayapplication.dto.AuthenticationDTOs.LoginRequest;
import com.rapitskyi.railwayapplication.dto.AuthenticationDTOs.RegisterRequest;
import com.rapitskyi.railwayapplication.dto.UserDTO;
import com.rapitskyi.railwayapplication.entity.User;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.ResourceAlreadyExistsException;
import com.rapitskyi.railwayapplication.repository.UserRepository;
import com.rapitskyi.railwayapplication.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(User.UserRole.user);
        
        User savedUser = userRepository.save(user);

        String jwt = jwtService.generateToken(savedUser);

        return new JwtResponse(jwt, UserDTO.fromEntity(savedUser));
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String jwt = jwtService.generateToken(user);

        return new JwtResponse(jwt, UserDTO.fromEntity(user));
    }
}