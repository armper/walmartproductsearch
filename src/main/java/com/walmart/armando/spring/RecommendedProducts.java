package com.walmart.armando.spring;

import java.util.ArrayList;
import java.util.List;

public class RecommendedProducts {
	private List<Product> items = new ArrayList<>();

	public List<Product> getItems() {
		return items;
	}

	public void setItems(List<Product> items) {
		this.items = items;
	}

}
