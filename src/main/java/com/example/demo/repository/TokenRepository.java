package com.example.demo.repository;


import com.example.demo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {

//    boolean findByUsername(String username);

    //    Optional<Token> findByUsername(String username);
    Token findByUsername(String username);
}
