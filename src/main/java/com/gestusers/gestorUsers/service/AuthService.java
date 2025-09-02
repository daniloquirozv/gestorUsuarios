package com.gestusers.gestorUsers.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gestusers.gestorUsers.dto.JwtResponse;
import com.gestusers.gestorUsers.dto.LoginRequest;
import com.gestusers.gestorUsers.dto.RegisterRequest;
import com.gestusers.gestorUsers.model.Role;
import com.gestusers.gestorUsers.model.User;
import com.gestusers.gestorUsers.repository.RoleRepository;
import com.gestusers.gestorUsers.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private RoleRepository roleRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse login (LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), 
            loginRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());

        User user = userRepository.findByUsername(loginRequest.getUsername())
                                    .orElseThrow(() -> new RuntimeException("User not found"));

        return new JwtResponse(jwt, "Bearer", user.getUsername(), user.getEmail(), roles);
    }

    public User register(RegisterRequest registerRequest){
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new RuntimeException("Username already exists!");
        } 
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email already exists!");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        }else{
            strRoles.forEach(role ->{
                switch (role) {
                    case "admin":
                    Role admindRole = roleRepository.findByName(Role.RoleName.ROLE_ADMIN)
                                                    .orElseThrow(() -> new RuntimeException("Role not found!"));
                        roles.add(admindRole);
                        break;
                    
                    case "guest":
                    Role guestRole = roleRepository.findByName(Role.RoleName.ROLE_GUEST)
                                                   .orElseThrow(() -> new RuntimeException("Role not found!"));
                        roles.add(guestRole);
                        break;
                
                    default:
                    Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                                                  .orElseThrow(() -> new RuntimeException("Role not found!"));
                        roles.add(userRole);
                        break;
                }
            });  
        }

        user.setRoles(roles);
        return userRepository.save(user);

    }

    


}
