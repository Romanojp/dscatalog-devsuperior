package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {
	public static Product createProduct() {
		Product product = new Product(1L, "Phone","GoodPhone", 800.0, "http://img.com/img.png" , Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(new Category(2L,"Eletronics"));
		return product;
	}
	
	public static ProductDTO createProductDto() {
		Product product = createProduct();
		
		return new ProductDTO(product, product.getCategories());
		
	}
	
}
