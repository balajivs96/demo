package com.app.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	@JsonIgnore
	private long id;
	private String username;
	private String email;
	private String password;
	private AddressDto address;
//	private Set<Long> roleIds;
}
