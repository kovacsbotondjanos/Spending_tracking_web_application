package com.example.monthlySpendingsBackend.dataBaseHandler.models.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public CustomUser registerUser(CustomUser user) {
        if(repository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already exists");
        }
        if(repository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public Optional<CustomUser> getUserById(Long id){
        return repository.findById(id);
    }

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