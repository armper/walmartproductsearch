package com.walmart.armando.spring;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

	private Dialog productPopup=new Dialog();

	private DetailsView detailsView;

	private Dialog recommendedPopup=new Dialog();

	public MainView(@Autowired ProductRepository productRepository, RecommendationRepository recommendationRepository, DetailsService detailsService) {

		getClass().getResourceAsStream("wmt_h_r_c.jpg");
		
		Image image = new Image("wmt_h_r_c.jpg", "Walmart Logo");
		image.setWidth("20em");
		add(image);
		
		/*
		 * The grid that will hold the recommended products based upon the selected
		 * product
		 */
		H4 recommendedItemsLabel = new H4("Recommended Items");
		recommendedItemsLabel.setVisible(false);

		recommendedProducts = new Grid<>();
		recommendedProducts.setVisible(false);

		recommendedProducts.setHeightByRows(true);
		recommendedProducts.setId("recommendedproductsgrid");
		// use Polymer for data
		recommendedProducts
				.addColumn(TemplateRenderer
						.<Product>of("<div><img width='45px' src = '[[item.thumbnailUrl]]'></img></div>")
						.withProperty("thumbnailUrl", Product::getThumbnailImage))
				.setHeader("Image").setFlexGrow(0).setWidth("10em");

		recommendedProducts.addColumn(Product::getName).setHeader("Name");
		recommendedProducts.addColumn(Product::getSalePrice).setHeader("Price").setFlexGrow(0);
		
		recommendedProducts.addSelectionListener(listener->{
			listener.getFirstSelectedItem().ifPresent(item->{
				productPopup.close();
				
				//set up the product details popup
				DetailsView recommendedDetailsView= new DetailsView(item);
				
				Div detailsContent=new Div();
				detailsContent.setSizeFull();
				
				Button closeButton=new Button("Close");

				detailsContent.add(closeButton, recommendedDetailsView);
				
				//set up the details popup as a notification
				recommendedPopup = new Dialog(detailsContent);

				closeButton.addClickListener(event -> recommendedPopup.close());

				recommendedPopup.open();
			});
		});

		/*
		 * The grid that will hold the products
		 */
		productGrid = new Grid<>();
		productGrid.setHeightByRows(true);
		productGrid.setId("productgrid");

		// use Polymer for data
		productGrid
				.addColumn(TemplateRenderer
						.<Product>of("<div><img src = '[[item.thumbnailUrl]]'></img></div>")
						.withProperty("thumbnailUrl", Product::getThumbnailImage))
				.setHeader("Image").setFlexGrow(0).setWidth("10em");

		productGrid.addColumn(Product::getName).setHeader("Name");
		productGrid.addColumn(Product::getSalePrice).setHeader("Price").setFlexGrow(0);
	
		productGrid.addSelectionListener(listener -> {
			listener.getAllSelectedItems().stream().forEach(selectedProduct -> {

				//close any possibly open  popups
				productPopup.close();	
				recommendedPopup.close();

				log.info("selected itemId: " + selectedProduct.getItemId());

				//find recommended products
				Collection<Product> findRecommendedProductByName = recommendationRepository
						.findRecommendedProductByItemId(selectedProduct.getItemId(), selectedProduct.getName());
				log.info("Found " + findRecommendedProductByName.size() + " recommended products: "
						+ findRecommendedProductByName.stream().map(Product::getName).collect(Collectors.joining("")));
				recommendedProducts.setItems(findRecommendedProductByName);

				//show recommended product grid if there is content
				if (findRecommendedProductByName.size() == 0) {
					recommendedItemsLabel.setVisible(false);
					recommendedProducts.setVisible(false);
				} else {
					recommendedItemsLabel.setVisible(true);
					recommendedProducts.setVisible(true);
				}
				
				//set up the product details popup
				detailsView= new DetailsView(selectedProduct);
				
				Div detailsContent=new Div();
				
				Button closeButton=new Button("Close");
				closeButton.addClickListener(event -> productPopup.close());

				detailsContent.add(closeButton, detailsView, recommendedItemsLabel, recommendedProducts);
				
				//set up the details popup as a notification
				productPopup = new Dialog(detailsContent);
				productPopup.open();
//
			});
		});

		/*
		 * Product Search Field and Button
		 */
		Div searchDiv = new Div();

		TextField searchTextField = new TextField("Product Search");

		button = new Button("Search", listener -> {
			productGrid.setItems(productRepository.searchByName(searchTextField.getValue()));
			
			//reset recommended products
			recommendedProducts.setItems(new ArrayList<Product>());
			recommendedItemsLabel.setVisible(false);
			recommendedProducts.setVisible(false);
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
		add(searchDiv, productGrid);

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
