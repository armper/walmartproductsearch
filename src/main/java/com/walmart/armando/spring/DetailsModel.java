package com.walmart.armando.spring;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface DetailsModel extends TemplateModel{

	String getDescription();
	
	void setDescription(String description);
}
