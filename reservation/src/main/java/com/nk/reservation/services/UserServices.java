package com.nk.reservation.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nk.reservation.entity.ApplicationUser;
import com.nk.reservation.repository.UserRepository;

@Service
public class UserServices implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApplicationUser user = userRepository.findByUsername(username)
                                            .orElseThrow(() ->
                                             new UsernameNotFoundException("User not found")
                                             );
        Hibernate.initialize(user.getAuthorities());
        return user;
    }
    
}
