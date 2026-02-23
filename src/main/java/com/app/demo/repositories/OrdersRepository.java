package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.demo.entity.Order;

public interface OrdersRepository extends JpaRepository<Order, Long> {

}
