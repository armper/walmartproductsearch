package com.walmart.armando.spring;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.stereotype.Repository;

import com.github.javafaker.Faker;

@Repository
public class RecommendationRepository {

	Collection<Product> findRecommendedProductByName(String name) {

		Faker faker = new Faker();

		HashSet<Product> products = new HashSet<Product>();

		for (int i = 0; i < 10; i++) {
			Product product = new Product();
			product.setItemId((long) faker.number().numberBetween(1000, 2000));
			product.setThumbnailImage(faker.internet().image());
			product.setLongDescription(faker.chuckNorris().fact());
			product.setName(faker.commerce().productName());
			product.setSalePrice(faker.number().randomDouble(2, 1, 1000));

			products.add(product);
		}

		return products;
	}
}
