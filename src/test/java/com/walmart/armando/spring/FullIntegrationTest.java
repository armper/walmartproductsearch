package com.walmart.armando.spring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.testbench.TestBenchTestCase;

public class FullIntegrationTest extends TestBenchTestCase {
	@Before
	public void setup() throws Exception {
		// Create a new browser instance
		setDriver(new ChromeDriver());
		// Open the application
		getDriver().get("http://localhost:8099/");
	}
	
	@Test
    public void clickButton() {
        // Find the first button (<vaadin-button>) on the page
        ButtonElement button = $(ButtonElement.class).first();

        // Click it
        button.click();

        GridElement grid = $(GridElement.class).id("productgrid");
        
        System.out.println("count: "+grid.getRowCount());
        
        assertThat(grid.getRowCount()).isEqualTo(10);
    }

	@After
	public void tearDown() throws Exception {
		// close the browser instance when all tests are done
		getDriver().quit();
	}

}
