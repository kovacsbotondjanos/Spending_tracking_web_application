package com.example.monthlySpendingsBackend.models.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomUser> user = repository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return new CustomUserDetails(
                    userObj.getId(),
                    userObj.getUsername(),
                    userObj.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userObj.getRole()))
            );
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}