package com.indra.techtest.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.indra.techtest.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {

	@Query("SELECT p FROM Price p WHERE p.brandId = :brandId AND p.productId = :productId AND :date BETWEEN p.startDate AND p.endDate")
	List<Price> findByBrandIdAndProductIdAndDate(@Param("brandId") Long brandId, @Param("productId") Long productId,
			@Param("date") LocalDateTime date);

}
