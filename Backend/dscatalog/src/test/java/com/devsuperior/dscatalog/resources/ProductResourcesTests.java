package com.devsuperior.dscatalog.resources;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ProductResource.class)
public class ProductResourcesTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProductService productService;
	
	private Long existingId;
	private Long dependentId;
	private Long nonExistingId;
	private ProductDTO productDTO;
	
	private PageImpl<ProductDTO> page;
	
	@BeforeEach
	void setUp()throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		productDTO = Factory.createProductDto();
		page = new PageImpl<>(List.of(productDTO));
		
		when(productService.findAllPaged(any())).thenReturn(page);
		
		when(productService.findById(existingId)).thenReturn(productDTO);
		when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(productService.update(eq(existingId), any())).thenReturn(productDTO); 
		when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(productService).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
		doThrow(DatabaseException.class).when(productService).delete(dependentId);
	}
	
	private Long eq(Long existingId2) {
		
		return null;
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		String expectedName = productDTO.getName();
		Double expectedPrice = productDTO.getPrice();
	
		ResultActions result = 
				mockMvc.perform(put("/product/{id}", existingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.price").value(expectedPrice));
		}
	
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
	
		ResultActions result = 
				mockMvc.perform(put("/product/{id}", existingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		}
	
	@Test
	public void findAllShouldReturnPage() throws Exception{
		
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON)
				);
		
		result.andExpect(status().isOk());
		
		
		
	}
	

}
