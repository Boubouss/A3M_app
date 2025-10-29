package com.A3M.user.security;

import com.A3M.user.enums.ERole;
import com.A3M.user.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        User user = new User();
        user.setId(1L);
        user.setEmail("0123456789");
        user.setPassword("Password123!");
        user.setUsername(annotation.username());
        user.setRole(ERole.valueOf("ROLE_" + annotation.roles()[0]));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                authorities
        );

        return new SecurityContextImpl(authentication);
    }
}