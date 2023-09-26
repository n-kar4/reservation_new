package com.nk.reservation.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nk.reservation.dto.LoginResponseDTO;
import com.nk.reservation.entity.ApplicationUser;
import com.nk.reservation.entity.Role;
import com.nk.reservation.repository.RoleRepository;
import com.nk.reservation.repository.UserRepository;

@Service
@Transactional
public class AuthenticationServices implements IAuthenticationServices{
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenServices tokenServices;
    
    @Override
    public ApplicationUser registerUser(String username, String password) {

        String encodedPassword = passwordEncoder.encode(password);
        
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByAuthority("USER").get());

        return userRepository.save(new ApplicationUser(username, encodedPassword, roles));
    }

    @Override
    public LoginResponseDTO login(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenServices.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);

        } catch(Exception e){
            return new LoginResponseDTO(null, e.toString());
        }
    }



    
    // @Override
    // public LoginResponseDTO login(String username, String password) {

    //     // try {
    //         Authentication auth = authenticationManager.authenticate
    //             (new UsernamePasswordAuthenticationToken(username, password)
    //         );

    //         String token = tokenServices.generateJwt(auth);

    //         return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
    //     // } catch (Exception e) {
    //     //     // e.printStackTrace();
    //     //     return new LoginResponseDTO(null, e.toString());

    //     // }
    // }
    
}
