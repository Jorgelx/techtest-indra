package com.indra.techtest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.indra.techtest.service.PriceService;
import com.indra.techtest.model.Price;
import com.indra.techtest.repository.PriceRepository;
import com.indra.techtest.dto.PriceDto;

/**
 * Test Application
 * 
 * @version 1.0
 * @author Jorge López Capdevila
 * 
 *         28/04/2023
 * 
 */

@SpringBootTest
@AutoConfigureMockMvc
class TechtestApplicationTests {

	@Autowired
	PriceRepository priceRepository;

	@Autowired
	PriceService priceService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	/**
	 * Prueba diferentes casos de uso en los que la respuesta del servidor sea 200
	 * OK y los datos concuerden con los esperados.
	 * 
	 * Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1(ZARA)
	 * Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
	 * Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
	 * Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
	 * Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)
	 * 
	 * @since 1.0
	 */

	@ParameterizedTest
	@CsvSource({ "2020-06-14 10:00, 35455, 1, 1, '2020-06-14T00:00:00', '2020-12-31T23:59:59', 35.5",
			"2020/06/14 16:00, 35455, 1, 2, '2020-06-14T15:00:00', '2020-06-14T18:30:00', 25.45",
			"2020-06-14-21:00, 35455, 1, 1, '2020-06-14T00:00:00', '2020-12-31T23:59:59', 35.5",
			"2020-06-15 10:00:00, 35455, 1, 3, '2020-06-15T00:00:00', '2020-06-15T11:00:00', 30.5",
			"2020/06/15-21:00:0, 35455, 1, 4, '2020-06-15T16:00:00', '2020-12-31T23:59:59', 38.95" })
	public void testApi(String date, long idProduct, long brandId, int priceList, String startDate, String endDate,
			double price) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/price/offer").param("date", date)
				.param("idProduct", String.valueOf(idProduct)).param("brandId", String.valueOf(brandId)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.productId").value(idProduct))
				.andExpect(jsonPath("$.brandId").value(brandId)).andExpect(jsonPath("$.priceList").value(priceList))
				.andExpect(jsonPath("$.startDate").value(startDate)).andExpect(jsonPath("$.endDate").value(endDate))
				.andExpect(jsonPath("$.price").value(price));
	}

	/**
	 * Prueba caso de uso en el que la respuesta del servidor sea 400 Bad Request
	 * Not Found.
	 * 
	 * @since 1.0
	 */
	@ParameterizedTest
	@CsvSource({ "15-06-2020, 35455, 1", "33-06-2020 10:00, 35455, 1", "01-06-2020 10h 00m, 0, 1" })
	public void testController400(String date, long idProduct, long brandId) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/price/offer").param("date", date)
				.param("idProduct", String.valueOf(idProduct)).param("brandId", String.valueOf(brandId)))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Prueba caso de uso en el que la respuesta del servidor sea 404 Not Found Bad
	 * Request.
	 * 
	 * @since 1.0
	 */
	@ParameterizedTest
	@CsvSource({ "2050/06/15, 35455, 1", "2020/06/14 16:00, 1, 1", "2020-06-14-16:00:00, 35455, 100" })
	public void testController404(String date, long idProduct, long brandId) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/price/offer").param("date", date)
				.param("idProduct", String.valueOf(idProduct)).param("brandId", String.valueOf(brandId)))
				.andExpect(status().isNotFound());
	}

	/**
	 * Comprueba que la carga inicial de los datos en memoria corresponda con la
	 * información del JSON de la que se alimenta.
	 * 
	 * @since 1.0
	 */
	@Test
	void resultInitialInsertOk() {
		List<Price> pricesJson = priceService.readPriceJson();
		List<Price> pricesH2 = priceRepository.findAll();
		for (int i = 0; i < pricesJson.size(); i++) {
			Price priceJson = pricesJson.get(0);
			PriceDto priceJsonDto = priceService.priceToPriceDto(priceJson);
			Price priceH2 = pricesH2.get(0);
			PriceDto priceH2Dto = priceService.priceToPriceDto(priceH2);
			Assert.assertEquals(priceJsonDto, priceH2Dto);
		}
	}

	/**
	 * Comprueba que el método findHighestPriorityPrice() devuelve entre una lista
	 * de precios el de mayor prioridad.
	 * 
	 * @since 1.0
	 */
	@Test
	void checkPriorityChoise() {

		List<Price> priceList = new ArrayList<Price>();

		Price price1 = new Price();
		price1.setPriority(0);
		price1.setPrice(22.60);
		priceList.add(price1);

		Price price2 = new Price();
		price2.setPriority(1);
		price2.setPrice(33.20);
		priceList.add(price2);

		Price price3 = new Price();
		price3.setPriority(2);
		price3.setPrice(13.30);
		priceList.add(price3);

		Price priceHighestPriority = priceService.findHighestPriorityPrice(priceList);
		Double expectedPrice = 13.30;
		assertEquals(expectedPrice, priceHighestPriority.getPrice(), 0.001);
	}

	/**
	 * Comprueba que el método dateFormater no devuelve ninguna excepción no
	 * esperada con formatos no permitidos.
	 * 
	 * @since 1.0
	 */
	@Test
	void formatDateTestKo() {

		LocalDateTime resultOk = LocalDateTime.of(2023, 6, 1, 0, 0);
		LocalDateTime formatKo1 = priceService.dateFormater("20230601-000000");
		LocalDateTime formatKo2 = priceService.dateFormater("Jorge");
		LocalDateTime formatKo3 = priceService.dateFormater("2023-32-50");
		LocalDateTime formatKo4 = priceService.dateFormater("01-06-2022");
		LocalDateTime formatKo5 = priceService.dateFormater("01-06-2023-00.00.00");

		Assert.assertNotEquals(resultOk, formatKo1);
		Assert.assertNotEquals(resultOk, formatKo2);
		Assert.assertNotEquals(resultOk, formatKo3);
		Assert.assertNotEquals(resultOk, formatKo4);
		Assert.assertNotEquals(resultOk, formatKo5);
	}

	/**
	 * Comprueba que el método dateFormater transforma correctamente la misma fecha
	 * en los diferentes formatos permitidos.
	 * 
	 * @since 1.0
	 */
	@Test
	void formatDateTestOk() {
		// Fecha de referencia.
		LocalDateTime resultOk = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

		LocalDateTime formatOk1 = priceService.dateFormater("2020-06-14-16.00.00");
		LocalDateTime formatOk2 = priceService.dateFormater("2020-06-14-16.00.0");
		LocalDateTime formatOk3 = priceService.dateFormater("2020-06-14-16.00");
		LocalDateTime formatOk4 = priceService.dateFormater("2020-06-14-16:00:00");
		LocalDateTime formatOk5 = priceService.dateFormater("2020-06-14-16:00:0");
		LocalDateTime formatOk6 = priceService.dateFormater("2020-06-14-16:00");
		LocalDateTime formatOk7 = priceService.dateFormater("2020-06-14 16.00.00");
		LocalDateTime formatOk8 = priceService.dateFormater("2020-06-14 16.00.0");
		LocalDateTime formatOk9 = priceService.dateFormater("2020-06-14 16.00");
		LocalDateTime formatOk10 = priceService.dateFormater("2020-06-14 16:00:00");
		LocalDateTime formatOk11 = priceService.dateFormater("2020-06-14 16:00:0");
		LocalDateTime formatOk12 = priceService.dateFormater("2020-06-14 16:00");
		LocalDateTime formatOk13 = priceService.dateFormater("2020/06/14-16.00.00");
		LocalDateTime formatOk14 = priceService.dateFormater("2020/06/14-16.00.0");
		LocalDateTime formatOk15 = priceService.dateFormater("2020-06-14-16.00");
		LocalDateTime formatOk16 = priceService.dateFormater("2020/06/14-16:00:00");
		LocalDateTime formatOk17 = priceService.dateFormater("2020/06/14-16:00:0");
		LocalDateTime formatOk18 = priceService.dateFormater("2020/06/14-16:00");
		LocalDateTime formatOk19 = priceService.dateFormater("2020/06/14 16.00.00");
		LocalDateTime formatOk20 = priceService.dateFormater("2020/06/14 16.00.0");
		LocalDateTime formatOk21 = priceService.dateFormater("2020/06/14 16.00");
		LocalDateTime formatOk22 = priceService.dateFormater("2020/06/14 16:00:00");
		LocalDateTime formatOk23 = priceService.dateFormater("2020/06/14 16:00:0");
		LocalDateTime formatOk24 = priceService.dateFormater("2020/06/14 16:00");
		LocalDateTime formatOk25 = priceService.dateFormater("2020/06/14").withHour(16);
		LocalDateTime formatOk26 = priceService.dateFormater("2020-06-14").withHour(16);
		LocalDateTime formatOk27 = priceService.dateFormater("2020 06 14").withHour(16);

		Assert.assertEquals(resultOk, formatOk1);
		Assert.assertEquals(resultOk, formatOk2);
		Assert.assertEquals(resultOk, formatOk3);
		Assert.assertEquals(resultOk, formatOk4);
		Assert.assertEquals(resultOk, formatOk5);
		Assert.assertEquals(resultOk, formatOk6);
		Assert.assertEquals(resultOk, formatOk7);
		Assert.assertEquals(resultOk, formatOk8);
		Assert.assertEquals(resultOk, formatOk9);
		Assert.assertEquals(resultOk, formatOk10);
		Assert.assertEquals(resultOk, formatOk11);
		Assert.assertEquals(resultOk, formatOk12);
		Assert.assertEquals(resultOk, formatOk13);
		Assert.assertEquals(resultOk, formatOk14);
		Assert.assertEquals(resultOk, formatOk15);
		Assert.assertEquals(resultOk, formatOk16);
		Assert.assertEquals(resultOk, formatOk17);
		Assert.assertEquals(resultOk, formatOk18);
		Assert.assertEquals(resultOk, formatOk19);
		Assert.assertEquals(resultOk, formatOk20);
		Assert.assertEquals(resultOk, formatOk21);
		Assert.assertEquals(resultOk, formatOk22);
		Assert.assertEquals(resultOk, formatOk23);
		Assert.assertEquals(resultOk, formatOk24);
		Assert.assertEquals(resultOk, formatOk25);
		Assert.assertEquals(resultOk, formatOk26);
		Assert.assertEquals(resultOk, formatOk27);

	}

}
