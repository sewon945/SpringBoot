package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users , Long>{
   Optional<Users> findByEmail(String email);
   
   boolean existsByEmail(String email); // email값이 있는지 확인하는 것
   
}
