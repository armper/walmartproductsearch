package com.walmart.armando.spring;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
	private Integer totalResults;

	private List<Product> items = new ArrayList<>();

	public ProductList() {
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public List<Product> getItems() {
		return items;
	}

	public void setItems(List<Product> items) {
		this.items = items;
	}

}
