package br.com.fatecrestapi.FoodFatec.repository;

import br.com.fatecrestapi.FoodFatec.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {


}