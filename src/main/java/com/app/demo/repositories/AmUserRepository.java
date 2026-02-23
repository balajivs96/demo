package com.app.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.demo.entity.AmUser;

public interface AmUserRepository extends JpaRepository<AmUser, Long> {
	Optional<AmUser> findByUsername(String username);
	Optional<AmUser> findByEmail(String email);
}
