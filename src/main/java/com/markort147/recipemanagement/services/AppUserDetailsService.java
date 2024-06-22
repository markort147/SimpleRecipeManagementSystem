package com.markort147.recipemanagement.services;

import com.markort147.recipemanagement.models.AppUser;
import com.markort147.recipemanagement.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    @Autowired
    public AppUserDetailsService(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));

        return new AppUserDetails(user);
    }
}
