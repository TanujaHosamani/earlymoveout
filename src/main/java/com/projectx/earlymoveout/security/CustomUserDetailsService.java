package com.projectx.earlymoveout.security;

import com.projectx.earlymoveout.entity.UserAccount;
import com.projectx.earlymoveout.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // ✅ Clean input (removes accidental spaces)
        String cleanUsername = username == null ? "" : username.trim();

        UserAccount user = userAccountRepository.findByUsernameIgnoreCase(cleanUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + cleanUsername));

        // ✅ IMPORTANT: add ROLE_ prefix for Spring Security
        String role = "ROLE_" + user.getRole();   // FACULTY -> ROLE_FACULTY

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
