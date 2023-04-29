package com.indra.techtest.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class PriceDto {

	private Long productId;
	private Long brandId;
	private Long priceList;
	LocalDateTime startDate;
	LocalDateTime endDate;
	private Double price;

	public PriceDto() {
		super();
	}

	public PriceDto(Long productId, Long brandId, Long priceList, LocalDateTime startDate, LocalDateTime endDate,
			Double price) {
		super();
		this.productId = productId;
		this.brandId = brandId;
		this.priceList = priceList;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getPriceList() {
		return priceList;
	}

	public void setPriceList(Long priceList) {
		this.priceList = priceList;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "PriceDto [productId=" + productId + ", brandId=" + brandId + ", priceList=" + priceList + ", startDate="
				+ startDate + ", endDate=" + endDate + ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(brandId, endDate, price, priceList, productId, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceDto other = (PriceDto) obj;
		return Objects.equals(brandId, other.brandId) && Objects.equals(endDate, other.endDate)
				&& Objects.equals(price, other.price) && Objects.equals(priceList, other.priceList)
				&& Objects.equals(productId, other.productId) && Objects.equals(startDate, other.startDate);
	}

}
