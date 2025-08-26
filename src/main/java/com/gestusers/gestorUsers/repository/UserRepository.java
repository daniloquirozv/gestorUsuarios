package com.gestusers.gestorUsers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestusers.gestorUsers.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    

}
