package br.com.fatecrestapi.FoodFatec.controller;

import br.com.fatecrestapi.FoodFatec.entity.Customer;
import br.com.fatecrestapi.FoodFatec.repository.CustomerRepository;
import br.com.fatecrestapi.FoodFatec.service.CustomerService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> getInfoCustomers() {
        List<Customer> result = customerService.getInfoCustomers();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/findByCpf")
    public ResponseEntity<Object> getCustomerByCpf(@RequestBody Customer customer) {
        Optional<Customer> result = customerService.findByCpfCustomer(customer.getCpfCustomer());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/findByEmail/{email}")
    public ResponseEntity<Object> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> result = customerService.findByEmailCustomer(email);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/findByStatusTrue")
    public ResponseEntity<Object> getCustomerByStatusTrue() {
        List<Customer> result = customerService.findByStatusTrue();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/findByDateCreated")
    public ResponseEntity<Object> getCustomerByDateCreated(@RequestBody Customer customer) {
        List<Customer> result = customerService.findByDateCreate(customer.getDateCreatedCustomer());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> saveCustomer(@RequestBody Customer customer) {
        Customer result = customerService.saveCustomer(customer);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(value = "/delete/{idCustomer}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long idCustomer) {
        HashMap<String, Object> result = customerService.deleteCustomer(idCustomer);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/findCustomer/{idCustomer}")
    public ResponseEntity<Object> findCustomer(@PathVariable Long idCustomer) {
        Optional<Customer> result = customerService.findCustomerById(idCustomer);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer) {
        Customer result = customerService.updateCustomer(customer);
        return ResponseEntity.ok().body(result);
    }

}