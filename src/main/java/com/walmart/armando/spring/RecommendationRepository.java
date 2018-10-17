package com.walmart.armando.spring;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.stereotype.Repository;

@Repository
public class RecommendationRepository {

	Collection<Product> findRecommendedProductByName(String name){
		HashSet<Product> products = new HashSet<Product>();

		Product product = new Product();
		product.setName(name);
		
		products.add(product);
		return products;
	}
}
