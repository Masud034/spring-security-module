package com.example.springsecurity.repositories;

import com.example.springsecurity.entities.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RolesEntityRepository extends JpaRepository<RolesEntity, UUID> {
    RolesEntity findByName(String name);
}
