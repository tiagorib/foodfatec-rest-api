package br.com.fatecrestapi.FoodFatec.repository;

import br.com.fatecrestapi.FoodFatec.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}