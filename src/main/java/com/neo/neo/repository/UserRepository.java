package com.neo.neo.repository;

import com.neo.neo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByCpf(String cpf);


    Page<User> findAll(Pageable pageable);



}
