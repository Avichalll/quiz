package com.quiz.quiz.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private final Userrepository userrepository;

    @Override
    @Transactional

    public UserDetails loadUserByUsername(String userContactNumber) throws UsernameNotFoundException {
        return userrepository.findByContactNumber(userContactNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
