package com.moxi.hera.User.Service;

import com.moxi.hera.User.Model.CustomUserDetails;
import com.moxi.hera.User.Model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Créez et retournez une instance de UserDetails (par exemple, CustomUserDetails)
        return new CustomUserDetails(user.getEmail(), user.getPassword(),user);
    }
}