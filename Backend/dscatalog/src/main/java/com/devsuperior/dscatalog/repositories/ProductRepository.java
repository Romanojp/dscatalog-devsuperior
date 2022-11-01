package com.devsuperior.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN  obj.categories cats "
			+ "WHERE (COALESCE(:category) IS NULL OR :category IN cats) AND "
			+ "(:name = '' OR LOWER(obj.name) LIKE LOWER (CONCAT('%', :name,'%')))")
	Page<Product> find(List<Category> category,String name, Pageable pageable);

}
