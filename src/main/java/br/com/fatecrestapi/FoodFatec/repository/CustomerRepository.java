package br.com.fatecrestapi.FoodFatec.repository;

import br.com.fatecrestapi.FoodFatec.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}