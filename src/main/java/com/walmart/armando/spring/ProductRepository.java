package com.walmart.armando.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class ProductRepository {
	private Logger log = LoggerFactory.getLogger(ProductRepository.class);

	public final RestTemplate restTemplate;
	private final String searchURI = "http://api.walmartlabs.com/v1/search";
	private String apiKey = "p4dbhqpqv8na44q75wuakrdx";

	public Collection<Product> searchByName(String query) {

		List<Product> products = new ArrayList<>();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(searchURI).queryParam("query", query)
				.queryParam("apiKey", apiKey);

		HttpEntity<ProductList> entity = new HttpEntity<>(headers);

		ResponseEntity<ProductList> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				ProductList.class);

		products.addAll(response.getBody().getItems());

		log.info("found "+response.getBody().getItems());
		log.info("getTotalResults "+response.getBody().getTotalResults());
		
		return products;
	}

	public ProductRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

}
