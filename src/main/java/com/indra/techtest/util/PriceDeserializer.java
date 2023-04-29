package com.indra.techtest.util;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.*;
import com.indra.techtest.model.Price;

public class PriceDeserializer implements JsonDeserializer<Price> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");

	@Override
	public Price deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Long brandId = jsonObject.get("BRAND_ID").getAsLong();
        LocalDateTime startDate = LocalDateTime.parse(jsonObject.get("START_DATE").getAsString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(jsonObject.get("END_DATE").getAsString(), formatter);
        Long priceList = jsonObject.get("PRICE_LIST").getAsLong();
        Long productId = jsonObject.get("PRODUCT_ID").getAsLong();
        Integer priority = jsonObject.get("PRIORITY").getAsInt();
        Double price = jsonObject.get("PRICE").getAsDouble();
        String currency = jsonObject.get("CURR").getAsString();
        return new Price(brandId, startDate, endDate, priceList, productId, priority, price, currency);
	}
}