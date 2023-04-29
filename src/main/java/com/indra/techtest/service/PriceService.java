package com.indra.techtest.service;

import java.util.List;

import java.time.LocalDateTime;

import com.indra.techtest.dto.PriceDto;
import com.indra.techtest.model.Price;

public interface PriceService {

	public void insertPriceTable();

	public List<Price> readPriceJson();

	public void checkPricesAndPrint();

	public LocalDateTime dateFormater(String date);

	public PriceDto getPrice(LocalDateTime date, long idProduct, long brandId);

	public Price findHighestPriorityPrice(List<Price> prices);

	public PriceDto priceToPriceDto(Price price);
}
