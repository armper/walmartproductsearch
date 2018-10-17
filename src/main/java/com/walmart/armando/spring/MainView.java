package com.walmart.armando.spring;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = -5078788760639570500L;

	private Logger log = LoggerFactory.getLogger(MainView.class);

	public MainView(@Autowired ProductRepository productRepository, RecommendationRepository recommendationRepository) {

		/*
		 * The grid that will hold the recommended products based upon the selected
		 * product
		 */
		Grid<Product> recommendedProducts = new Grid<>();
		recommendedProducts.addColumn(Product::getName).setHeader("Product Name");

		/*
		 * The grid that will hold the products
		 */
		Grid<Product> productGrid = new Grid<>();

		productGrid.addColumn(TemplateRenderer.<Product>of("<div><img src = '[[item.thumbnailUrl]]'></img></div>")
				.withProperty("thumbnailUrl", Product::getThumbnailImage)).setHeader("Image").setFlexGrow(0).setWidth("10em");

		productGrid.addColumn(Product::getName).setHeader("Name");
		productGrid.addColumn(Product::getSalePrice).setHeader("Price").setFlexGrow(0);
		productGrid.addColumn(
				TemplateRenderer.<Product>of("<div style='white-space:normal'>[[item.description]]</div>")
						.withProperty("description", Product::getLongDescription))
				.setHeader("Description").setFlexGrow(1);
		
		productGrid.addSelectionListener(listener -> {
			listener.getAllSelectedItems().stream().forEach(selectedProduct -> {
				Collection<Product> findRecommendedProductByName = recommendationRepository
						.findRecommendedProductByName(selectedProduct.getName());
				log.info("Found " + findRecommendedProductByName.size() + " recommended products: "
						+ findRecommendedProductByName.stream().map(Product::getName).collect(Collectors.joining("")));
				recommendedProducts.setItems(findRecommendedProductByName);
			});
		});

		/*
		 * Product Search
		 */
		Div searchDiv = new Div();

		TextField searchTextField = new TextField("Product Search");

		Button button = new Button("Search", listener -> {
			productGrid.setItems(productRepository.searchByName(searchTextField.getValue()));

		});

		searchDiv.add(searchTextField, button);

		/*
		 * Search Results
		 */
		Div resultsDiv = new Div();

		resultsDiv.add(productGrid);

		/*
		 * add product search area and search results area to the layout
		 */
		add(searchDiv, productGrid, recommendedProducts);

	}

}
