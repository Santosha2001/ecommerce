package com.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.dto.Response;
import com.ecommerce.services.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

	private final AddressService addressService;

	/**
	 * This endpoint handles the POST request to save or update an address.
	 * 
	 * @param addressDto The AddressDto object containing the address details to be
	 *                   saved or updated.
	 * @return ResponseEntity<Response> A response entity containing the result of
	 *         the save or update operation.
	 */
	@PostMapping("/saveAddress")
	public ResponseEntity<Response> saveAndUpdateAddress(@RequestBody AddressDto addressDto) {
		
		return ResponseEntity.ok(addressService.saveAndUpdateAddress(addressDto));
	}
}
