package com.app.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
	@JsonIgnore
	private long id;

	private String street;
	private String city;
	private String state;
	private String zipCode;
}
