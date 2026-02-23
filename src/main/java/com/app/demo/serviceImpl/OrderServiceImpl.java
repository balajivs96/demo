package com.app.demo.serviceImpl;

import org.springframework.stereotype.Service;

import com.app.demo.constants.AppConstants;
import com.app.demo.dto.OrderDto;
import com.app.demo.entity.AmUser;
import com.app.demo.entity.Order;
import com.app.demo.handler.ApiLogicException;
import com.app.demo.repositories.AmUserRepository;
import com.app.demo.repositories.OrdersRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl {

	private OrdersRepository ordersRepository;
	private AmUserRepository amUserRepository;

	public String placeOrder(Long userId, OrderDto orderDto) {
		AmUser user = amUserRepository.findById(userId)
				.orElseThrow(() -> new ApiLogicException(404, AppConstants.NO_USERS_FOUND));
		Order order = new Order();
		order.setProduct(orderDto.getProduct());
		order.setQuantity(orderDto.getQuantity());
		order.setPrice(orderDto.getPrice());
		order.setAmUser(user);
		ordersRepository.save(order);
		return "Order placed successfully";
	}

	public AmUser cancelOrder(Long userId, Long orderId) {
		AmUser user = amUserRepository.findById(userId)
				.orElseThrow(() -> new ApiLogicException(404, AppConstants.NO_USERS_FOUND));

		user.getOrders().removeIf(order -> order.getId() == orderId);

		return amUserRepository.save(user);
	}

}
