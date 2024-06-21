package br.com.fatecrestapi.FoodFatec.controller;

import br.com.fatecrestapi.FoodFatec.entity.Customer;
import br.com.fatecrestapi.FoodFatec.exception.ResponseGenericException;
import br.com.fatecrestapi.FoodFatec.repository.CustomerRepository;
import br.com.fatecrestapi.FoodFatec.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/customer")
@CrossOrigin(value = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/list")
    @Operation(summary = "List all customers")
    public ResponseEntity<Object> getInfoCustomers() {
        List<Customer> result = customerService.getInfoCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @GetMapping(value = "/findByCpf")
    @Operation(summary = "Method for find customer by CPF")
    public ResponseEntity<Object> getCustomerByCpf(@RequestBody Customer customer) {
        Optional<Customer> result = customerService.findByCpfCustomer(customer.getCpfCustomer());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @GetMapping(value = "/findByEmail/{email}")
    @Operation(summary = "Method for find customer by Email")
    public ResponseEntity<Object> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> result = customerService.findByEmailCustomer(email);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @GetMapping(value = "/findByStatusTrue")
    @Operation(summary = "Method for list customers with status true")
    public ResponseEntity<Object> getCustomerByStatusTrue() {
        List<Customer> result = customerService.findByStatusTrue();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @GetMapping(value = "/findByDateCreated")
    @Operation(summary = "Method for find customer by Date Created")
    public ResponseEntity<Object> getCustomerByDateCreated(@RequestBody Customer customer) {
        List<Customer> result = customerService.findByDateCreate(customer.getDateCreatedCustomer());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Method for registering customer")
    public ResponseEntity<Object> saveCustomer(@RequestBody Customer customer) {
        Customer result = customerService.saveCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGenericException.response(result));
    }

    @DeleteMapping(value = "/delete/{idCustomer}")
    @Operation(summary = "Method for delete customer")
    public ResponseEntity<Object> deleteCustomer(@PathVariable String idCustomer) {
        HashMap<String, Object> result = customerService.deleteCustomer(idCustomer);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @GetMapping(value = "/findCustomer/{idCustomer}")
    @Operation(summary = "Method for find customer by ID")
    public ResponseEntity<Object> findCustomer(@PathVariable String idCustomer) {
        Optional<Customer> result = customerService.findCustomerById(idCustomer);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @PutMapping(value = "/update")
    @Operation(summary = "Method for update customer")
    public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer) {
        Customer result = customerService.updateCustomer(customer);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

}