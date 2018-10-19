package com.walmart.armando.spring;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@Tag("details-view")
@HtmlImport("details-view.html")
public class DetailsView extends PolymerTemplate<DetailsModel> {

	private static final long serialVersionUID = -8151572764707512046L;


	public DetailsView(Product product) {

		getModel().setDescription(product.getLongDescription());

	}

}
