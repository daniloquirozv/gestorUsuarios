package com.gestusers.gestorUsers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gestusers.gestorUsers.model.Role;
import com.gestusers.gestorUsers.model.User;

import feign.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query("Select u from User u join u.roles r where r.name = :rolename")
    List<User> findByRoleName(@Param("rolename") Role.RoleName roleName);
}
