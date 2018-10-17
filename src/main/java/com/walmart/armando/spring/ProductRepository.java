package com.walmart.armando.spring;

import java.util.HashSet;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

	public HashSet<Product> searchByName(String name) {

		HashSet<Product> products = new HashSet<Product>();

		Product product = new Product();
		product.setItemId(1234L);
		product.setName("hi");
		product.setSalePrice(123.33);
		product.setThumbnailImage(
				"https://i5.walmartimages.com/asr/20caa881-9f84-478b-8259-b9c3448e1007_1.0872b05362d97ff68033417572228b40.jpeg?odnHeight=100&odnWidth=100&odnBg=FFFFFF");

		products.add(product);

		product = new Product();
		product.setItemId(1235L);
		product.setSalePrice(444.34);
		product.setName("second second second");
		product.setLongDescription(
				"fkdlsjf fjkdls jfjfj fidosjofiew jfkdlsklds fjkldsj fjkdlsjfewi e wieow e owklfj dskl.");
		product.setThumbnailImage(
				"https://i5.walmartimages.com/asr/20caa881-9f84-478b-8259-b9c3448e1007_1.0872b05362d97ff68033417572228b40.jpeg?odnHeight=100&odnWidth=100&odnBg=FFFFFF");
		products.add(product);
		return products;
	}

}
