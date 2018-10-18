package com.walmart.armando.spring;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = -5078788760639570500L;

	private Logger log = LoggerFactory.getLogger(MainView.class);

	private Grid<Product> recommendedProducts;

	private Grid<Product> productGrid;

	private Button button;

	public MainView(@Autowired ProductRepository productRepository, RecommendationRepository recommendationRepository) {

		/*
		 * The grid that will hold the recommended products based upon the selected
		 * product
		 */
		recommendedProducts = new Grid<>();
		recommendedProducts.setId("recommendedproductsgrid");
		// use Polymer for data
		recommendedProducts
						.addColumn(TemplateRenderer
								.<Product>of("<div><img width='45px' src = '[[item.thumbnailUrl]]'></img></div>")
								.withProperty("thumbnailUrl", Product::getThumbnailImage))
						.setHeader("Image").setFlexGrow(0).setWidth("10em");

		recommendedProducts.addColumn(Product::getName).setHeader("Name");
		recommendedProducts.addColumn(Product::getSalePrice).setHeader("Price").setFlexGrow(0);
				// use Polymer for data 
//				productGrid
//						.addColumn(TemplateRenderer.<Product>of("<p style='white-space:normal' innerHTML$='[[item.description]]'>armando</p>")
//								.withProperty("description", Product::getLongDescription))
//						.setHeader("Description").setFlexGrow(1);
		recommendedProducts.addComponentColumn(product->{
					return new Html("<div style='white-space:normal'>"+product.getLongDescription()+"</div>");
				}).setHeader("Description").setFlexGrow(1);

		recommendedProducts.addSelectionListener(listener -> {
					listener.getAllSelectedItems().stream().forEach(selectedProduct -> {
						Collection<Product> findRecommendedProductByName = recommendationRepository
								.findRecommendedProductByItemId(selectedProduct.getItemId(), selectedProduct.getName());
						log.info("Found " + findRecommendedProductByName.size() + " recommended products: "
								+ findRecommendedProductByName.stream().map(Product::getName).collect(Collectors.joining("")));
						recommendedProducts.setItems(findRecommendedProductByName);
					});
				});

		/*
		 * The grid that will hold the products
		 */
		productGrid = new Grid<>();
		productGrid.setId("productgrid");
		
		// use Polymer for data
		productGrid
				.addColumn(TemplateRenderer
						.<Product>of("<div><img width='45px' src = '[[item.thumbnailUrl]]'></img></div>")
						.withProperty("thumbnailUrl", Product::getThumbnailImage))
				.setHeader("Image").setFlexGrow(0).setWidth("10em");

		productGrid.addColumn(Product::getName).setHeader("Name");
		productGrid.addColumn(Product::getSalePrice).setHeader("Price").setFlexGrow(0);
		// use Polymer for data 
		productGrid
				.addColumn(TemplateRenderer.<Product>of("<p style='white-space:normal' inner-H-T-M-L='[[item.description]]'></p>")
						.withProperty("description", Product::getLongDescription))
				.setHeader("Description").setFlexGrow(1);
//		productGrid.addComponentColumn(product->{
//			return new Html("<div style='white-space:normal'>"+product.getLongDescription()+"</div>");
//		}).setHeader("Description").setFlexGrow(1);

		productGrid.addSelectionListener(listener -> {
			listener.getAllSelectedItems().stream().forEach(selectedProduct -> {
				
				log.info("selected itemId: "+selectedProduct.getItemId());
				
				Collection<Product> findRecommendedProductByName = recommendationRepository
						.findRecommendedProductByItemId(selectedProduct.getItemId(), selectedProduct.getName());
				log.info("Found " + findRecommendedProductByName.size() + " recommended products: "
						+ findRecommendedProductByName.stream().map(Product::getName).collect(Collectors.joining("")));
				recommendedProducts.setItems(findRecommendedProductByName);
			});
		});

		/*
		 * Product Search Field and Button
		 */
		Div searchDiv = new Div();

		TextField searchTextField = new TextField("Product Search");

		button = new Button("Search", listener -> {
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

	public Grid<Product> getRecommendedProducts() {
		return recommendedProducts;
	}

	public void setRecommendedProducts(Grid<Product> recommendedProducts) {
		this.recommendedProducts = recommendedProducts;
	}

	public Grid<Product> getProductGrid() {
		return productGrid;
	}

	public void setProductGrid(Grid<Product> productGrid) {
		this.productGrid = productGrid;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

}
