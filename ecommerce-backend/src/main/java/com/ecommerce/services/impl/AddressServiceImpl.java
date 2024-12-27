package com.ecommerce.services.impl;

import org.springframework.stereotype.Service;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.dto.Response;
import com.ecommerce.entities.Address;
import com.ecommerce.entities.User;
import com.ecommerce.repositories.AddressRepository;
import com.ecommerce.services.AddressService;
import com.ecommerce.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepo;
	private final UserService userService;

	/**
	 * Saves or updates the address of the currently logged-in user.
	 * 
	 * @param addressDto Data transfer object containing address details.
	 * @return A Response object indicating success or failure.
	 */
	@Override
	public Response saveAndUpdateAddress(AddressDto addressDto) {

		// Get the currently logged-in user
		User user = userService.getLoginUser();

		// Retrieve the user's existing address, or create a new one if none exists
		Address address = user.getAddress();

		if (address == null) {
			address = new Address();
			address.setUser(user);
		}

		// Update the address fields if provided in the DTO
		if (addressDto.getStreet() != null)
			address.setStreet(addressDto.getStreet());
		if (addressDto.getCity() != null)
			address.setCity(addressDto.getCity());
		if (addressDto.getState() != null)
			address.setState(addressDto.getState());
		if (addressDto.getZipCode() != null)
			address.setZipCode(addressDto.getZipCode());
		if (addressDto.getCountry() != null)
			address.setCountry(addressDto.getCountry());

		// Save the address to the repository
		addressRepo.save(address);

		// Build the response message based on whether the address was created or
		// updated
		String message = (user.getAddress() == null) ? 
				"Address successfully created" : 
				"Address successfully updated";
		
		return Response.builder().status(200).message(message).build();
	}
}
