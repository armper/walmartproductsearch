package com.walmart.armando.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class RecommendationRepository {

	private Logger log = LoggerFactory.getLogger(ProductRepository.class);

	public final RestTemplate restTemplate;
	private final String searchURI = "http://api.walmartlabs.com/v1/nbp";
	private String apiKey = "p4dbhqpqv8na44q75wuakrdx";

	public Collection<Product> findRecommendedProductByItemId(Long itemId, String name) {

		List<Product> products = new ArrayList<>();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(searchURI)
					.queryParam("itemId", itemId)
					.queryParam("apiKey", apiKey);

			HttpEntity<RecommendedProducts> entity = new HttpEntity<>(headers);

			ResponseEntity<List<Product>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<List<Product>>() {
					});

			products.addAll(response.getBody());
		} catch (RestClientException e) {
			Product noProductFoundProduct=new Product();
			noProductFoundProduct.setItemId(999L);
			noProductFoundProduct.setName("None Found");
			noProductFoundProduct.setLongDescription("No recommended products found for item: "+name);
			noProductFoundProduct.setThumbnailImage("https://i5.walmartimages.com/asr/8ec661ff-cd21-4edf-8b5c-bc96870d1b92_1.28f2e1d512c3dd80cdc8c8a8e9f67d87.jpeg?odnHeight=100&odnWidth=100&odnBg=FFFFFF");
			products.add(noProductFoundProduct);
		}

		return products;
	}

	public RecommendationRepository(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

}
