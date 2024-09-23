package com.example.springsecurity.repositories;

import com.example.springsecurity.entities.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorityEntityRepository extends JpaRepository<AuthorityEntity, UUID> {
    AuthorityEntity findByName(String name);
}
