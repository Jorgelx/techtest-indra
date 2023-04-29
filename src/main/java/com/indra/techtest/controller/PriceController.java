package com.indra.techtest.controller;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indra.techtest.dto.PriceDto;
import com.indra.techtest.service.PriceService;

/**
 * Controlador REST PRICES
 * 
 * @author Jorge López Capdevila
 * @version 1.0
 * @since 28/04/2023
 */

@RestController
@RequestMapping("/price")
public class PriceController {

	@Autowired
	PriceService priceService;

	/**
	 * 
	 * Llamada al servicio Endpoint: /price/offer
	 * 
	 * @param string date required
	 * @param long   idProduct required
	 * @param long   brandId required
	 * @return responseEntity priceDto
	 * @since 1.0
	 */

	@GetMapping(path = "/offer")
	public ResponseEntity<?> get(@RequestParam(required = true) String date,
			@RequestParam(required = true) long idProduct, @RequestParam(required = true) long brandId) {
		LocalDateTime formatDate = null;
		formatDate = priceService.dateFormater(date);
		if (formatDate == null) {
			return new ResponseEntity<String>("Invalid date format: " + date, HttpStatus.BAD_REQUEST);
		}
		PriceDto result = priceService.getPrice(formatDate, idProduct, brandId);
		if (result == null) {
			return new ResponseEntity<String>("Price not found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<PriceDto>(result, HttpStatus.OK);
		}
	}
}
