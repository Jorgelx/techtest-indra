package com.indra.techtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.indra.techtest.service.PriceService;



@SpringBootApplication
public class TechtestApplication implements CommandLineRunner {
	@Autowired
	private PriceService priceService;

	public static void main(String[] args) {
		SpringApplication.run(TechtestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		priceService.insertPriceTable();
	}
}