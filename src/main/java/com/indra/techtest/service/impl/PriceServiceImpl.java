package com.indra.techtest.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.io.BufferedReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.indra.techtest.dto.PriceDto;
import com.indra.techtest.model.Price;
import com.indra.techtest.repository.PriceRepository;
import com.indra.techtest.service.PriceService;
import com.indra.techtest.util.PriceDeserializer;
import com.indra.techtest.util.FechaUtil;

/**
 * Servicio tabla PRICES
 * 
 * @author Jorge López Capdevila
 * @version 1.0
 * @since 28/04/2023
 */

@Service
public class PriceServiceImpl implements PriceService {

	@Autowired
	private PriceRepository priceRepository;
	private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);
	private static final String JSON_URL = "src/main/resources/json/prices.json";
	private FechaUtil fechaUtil = new FechaUtil();

	/**
	 * 
	 * Al inicio de la app almacena en memoria información referente al precio final
	 * y tarifa de un producto entre un rango de fechas determinadas.
	 * 
	 * 
	 * @since 1.0
	 */
	public void insertPriceTable() {
		List<Price> list = readPriceJson();
		priceRepository.saveAll(list);
		logger.info("*** Table PRICES provided");
		checkPricesAndPrint();
	}

	/**
	 * 
	 * Lee de un fichero JSON la información a cargar en la tabla temporal PRICE.
	 * 
	 * @return list Lista de precios
	 * @since 1.0
	 */
	public List<Price> readPriceJson() {
		String filePath = JSON_URL;
		List<Price> prices = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			Gson gson = new GsonBuilder().registerTypeAdapter(Price.class, new PriceDeserializer()).create();
			Type listType = new TypeToken<List<Price>>() {
			}.getType();
			prices = gson.fromJson(br, listType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prices;
	}

	/**
	 * 
	 * Imprime por consola los articulos almacenados en tabla de precios
	 * 
	 * @param
	 * @return void
	 * @since 1.0
	 */
	public void checkPricesAndPrint() {
		priceRepository.findAll().forEach(price -> logger.info(price.toString()));
	}

	/**
	 * 
	 * Formatea la fecha recibida para permitir al usuario mandar diferentes
	 * formatos, lo mas correcto seria recibir siempre un formato estandar.
	 * 
	 * 
	 * @param String date
	 * @return LocalDateTime localDateTime
	 * @since 1.0
	 */
	public LocalDateTime dateFormater(String date) {
		LocalDateTime localDateTime = fechaUtil.dateFormater(date);
		return localDateTime;
	}

	/**
	 * 
	 * Recupera el precio a aplicar.
	 * 
	 * @param LocalDateTime date
	 * @param long          idProduct
	 * @param long          brandId
	 * @return priceDto
	 * @since 1.0
	 */
	public PriceDto getPrice(LocalDateTime date, long idProduct, long brandId) {
		;
		List<Price> prices = priceRepository.findByBrandIdAndProductIdAndDate(brandId, idProduct, date);
		Price price = findHighestPriorityPrice(prices);
		PriceDto output = priceToPriceDto(price);
		return output;
	}

	/**
	 * 
	 * Selecciona el precio de mayor prioridad
	 * 
	 * @param list prices
	 * @return price
	 * @since 1.0
	 */

	public Price findHighestPriorityPrice(List<Price> prices) {
		Price highestPriorityPrice = null;
		for (Price price : prices) {
			if (highestPriorityPrice == null || price.getPriority() > highestPriorityPrice.getPriority()) {
				highestPriorityPrice = price;
			}
		}
		return highestPriorityPrice;
	}

	/**
	 * 
	 * Mapea un objeto Price a PriceDto
	 * 
	 * @param price
	 * @return priceDto
	 * @since 1.0
	 */
	@Override
	public PriceDto priceToPriceDto(Price price) {
		PriceDto priceDto = null;
		if (price != null) {
			priceDto = new PriceDto();
			priceDto.setProductId(price.getProductId());
			priceDto.setBrandId(price.getBrandId());
			priceDto.setPriceList(price.getPriceList());
			priceDto.setStartDate(price.getStartDate());
			priceDto.setEndDate(price.getEndDate());
			priceDto.setPrice(price.getPrice());
		}
		return priceDto;
	}
}
