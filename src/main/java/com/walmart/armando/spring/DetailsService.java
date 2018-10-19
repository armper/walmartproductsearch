package com.walmart.armando.spring;

import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.annotation.UIScope;

@Component
@UIScope
public class DetailsService {

	private Product product;
	
	private DetailsView detailsView;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public DetailsView getDetailsView() {
		return detailsView;
	}

	public void setDetailsView(DetailsView detailsView) {
		this.detailsView = detailsView;
	}
}
