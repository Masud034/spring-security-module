package com.example.springsecurity.setup;

import com.example.springsecurity.entities.AuthorityEntity;
import com.example.springsecurity.entities.RolesEntity;
import com.example.springsecurity.repositories.AuthorityEntityRepository;
import com.example.springsecurity.repositories.RolesEntityRepository;
import com.example.springsecurity.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class InitialRoleAuthoritySetup {

    private final AuthorityEntityRepository authorityRepository;

    private final RolesEntityRepository roleRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
        createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
    }

    private AuthorityEntity createAuthority (String name) {
        AuthorityEntity authority = authorityRepository.findByName(name);
        if(authority == null) {
            authority = new AuthorityEntity();
            authority.setName(name);
            authorityRepository.save(authority);
        }

        return authority;
    }


    private RolesEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        RolesEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RolesEntity();
            role.setName(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
