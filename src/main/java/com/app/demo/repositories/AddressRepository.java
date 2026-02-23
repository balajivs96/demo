package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.demo.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
