package com.app.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.demo.constants.AppConstants;
import com.app.demo.dto.UserDto;
import com.app.demo.entity.Address;
import com.app.demo.entity.AmUser;
import com.app.demo.entity.Roles;
import com.app.demo.handler.ApiLogicException;
import com.app.demo.repositories.AmUserRepository;
import com.app.demo.repositories.RoleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl {

	private AmUserRepository amUserRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;

	public List<AmUser> getUserById(Long id) {
		AmUser user = amUserRepository.findById(id)
				.orElseThrow(() -> new ApiLogicException(1001, AppConstants.NO_USERS_FOUND));

		return List.of(user);
	}

	public String createUser(UserDto userDto) {
		amUserRepository.findByUsername(userDto.getUsername()).ifPresent(u -> {
			throw new ApiLogicException(1000, AppConstants.USERNAME_ALREADY_EXISTS);
		});

		amUserRepository.findByEmail(userDto.getEmail()).ifPresent(u -> {
			throw new ApiLogicException(1000, AppConstants.EMAIL_ALREADY_EXISTS);
		});

		AmUser user = new AmUser();
		Address address = new Address();

		address.setStreet(userDto.getAddress().getStreet());
		address.setCity(userDto.getAddress().getCity());
		address.setState(userDto.getAddress().getState());
		address.setZipCode(userDto.getAddress().getZipCode());

		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setAddress(address);

		Roles defaultRole = roleRepository.findByName("CUSTOMER")
				.orElseThrow(() -> new ApiLogicException(1002, "Default role not found"));
		user.getRoles().add(defaultRole);

		amUserRepository.save(user);

		return AppConstants.USER_CREATED_SUCCESS;
	}

	public List<AmUser> getAllUsers() {
		return Optional.of(amUserRepository.findAll()).filter(list -> !list.isEmpty())
				.orElseThrow(() -> new ApiLogicException(1001, AppConstants.NO_USERS_FOUND));
	}
}
