package com.app.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.demo.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long> {
	Optional<Roles> findByName(String name);
}
