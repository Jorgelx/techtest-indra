package com.indra.techtest.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Util para manejar diferentes formatos de fechas.
 * 
 * @author Jorge López Capdevila
 * @version 1.0
 * @since 28/04/2023
 */

public class FechaUtil {

	/**
	 * Transforma un string a un objeto LocalDateTime
	 * 
	 * @param string date
	 * @return localDateTime
	 * @since 1.0
	 */

	public LocalDateTime dateFormater(String date) {
		List<DateTimeFormatter> dateTimeFormatters = Arrays.asList(
				DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.s"), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm"),

				DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:s"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"),
				
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.s"), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm"),
	
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:s"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),

				DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"), 
				DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:s"),
				DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
				
				DateTimeFormatter.ofPattern("yyyy/MM/dd HH.mm.ss"), 
				DateTimeFormatter.ofPattern("yyyy/MM/dd HH.mm.s"),
				DateTimeFormatter.ofPattern("yyyy/MM/dd HH.mm"),

				DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss"), 
				DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:s"),
				DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm"),
				
				DateTimeFormatter.ofPattern("yyyy/MM/dd-HH.mm.ss"), 
				DateTimeFormatter.ofPattern("yyyy/MM/dd-HH.mm.s"),
				DateTimeFormatter.ofPattern("yyyy/MM/dd-HH.mm")

		);
		LocalDateTime localDateTime = null;
		for (DateTimeFormatter formatter : dateTimeFormatters) {
			try {
				localDateTime = LocalDateTime.parse(date, formatter);
				break;
			} catch (DateTimeParseException e) {
				// Se ignora la excepción y se vuelve a probar con el siguiente formato.
			}
		}
		if (localDateTime == null)
			localDateTime = dateFormaterWithoutTime(date);
		return localDateTime;
	}

	/**
	 * Transforma un string que representa un dia en concreto a un objeto
	 * LocalDateTime
	 * 
	 * @param string date
	 * @return localDateTime
	 * @since 1.0
	 */

	public LocalDateTime dateFormaterWithoutTime(String date) {
		List<DateTimeFormatter> dateFormatters = Arrays.asList(DateTimeFormatter.ofPattern("yyyy/MM/dd"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("yyyy MM dd"));
		LocalDate localDate = null;
		LocalDateTime localDateTime = null;
		for (DateTimeFormatter formatter : dateFormatters) {
			try {
				localDate = LocalDate.parse(date, formatter);
				break;
			} catch (DateTimeParseException e) {
				// Se ignora la excepción y se vuelve a probar con el siguiente formato.
			}
		}
		if (localDate != null)
			localDateTime = localDate.atStartOfDay();
		return localDateTime;
	}

}
