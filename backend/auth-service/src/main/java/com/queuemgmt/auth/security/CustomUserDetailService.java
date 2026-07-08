package com.queuemgmt.auth.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.queuemgmt.auth.entity.User;
import com.queuemgmt.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/* Spring security does not know about our User entity, so we need to create a custom implementation of UserDetailsService
it helps in converting our User entity to Spring Security's UserDetails object
 */

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
       User user = userRepository.findByEmail(email)
       .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
       return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_"+ user.getRole().name())
            ));
    }
    
}
