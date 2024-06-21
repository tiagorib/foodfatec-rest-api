package br.com.fatecrestapi.FoodFatec.repository;

import br.com.fatecrestapi.FoodFatec.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByCpfCustomer(String cpfCustomer);
    Optional<Customer> findByEmailCustomer(String emailCustomer);

    List<Customer> findByStatusCustomerIsTrueOrderByDateCreatedCustomerDesc();

    @Query(value = "SELECT c.* FROM customer c WHERE c.date_created_customer >= ?;", nativeQuery = true)
    List<Customer> findByDateCreated(@Param("dateCreatedCustomer") LocalDate dateCreatedCustomer);

}