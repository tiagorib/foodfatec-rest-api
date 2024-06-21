package br.com.fatecrestapi.FoodFatec.service;

import br.com.fatecrestapi.FoodFatec.entity.Customer;
import br.com.fatecrestapi.FoodFatec.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getInfoCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        if (validateCustomer(customer)) {
            encryptPassword(customer);
            return customerRepository.saveAndFlush(customer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A renda mensal do cliente é " +
                            "obrigatória e deve ser maior ou igual a 0 (zero)!");
        }
    }

    public HashMap<String, Object> deleteCustomer(String idCustomer) {
        Optional<Customer> customer =
                Optional.ofNullable(customerRepository.findById(idCustomer).
                        orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Cliente não encontrado!")));


        customerRepository.delete(customer.get());
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "Cliente: " + customer.get().getFirstNameCustomer() +
                " " + customer.get().getLastNameCustomer() +  " excluído com sucesso!");
        return result;
    }

    public Optional<Customer> findCustomerById(String idCustomer) {
        return Optional.ofNullable(customerRepository.findById(idCustomer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado!")));
    }

    public Optional<Customer> findByCpfCustomer(String cpfCustomer) {
        return Optional.ofNullable(customerRepository.findByCpfCustomer(cpfCustomer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhum cliente encontrado com o CPF: " + cpfCustomer)));
    }

    public Optional<Customer> findByEmailCustomer(String emailCustomer) {
        return Optional.ofNullable(customerRepository.findByEmailCustomer(emailCustomer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhum cliente encontrado com esse Email!")));
    }

    public List<Customer> findByStatusTrue() {
        return customerRepository.findByStatusCustomerIsTrueOrderByDateCreatedCustomerDesc();
    }

    public List<Customer> findByDateCreate(LocalDate dateCreatedCustomer) {
        return customerRepository.findByDateCreated(dateCreatedCustomer);
    }

    public Customer updateCustomer(Customer customer) {
        if (validateCustomer(customer)) {
            if (findCustomerById(customer.getIdCustomer()) != null) {
                encryptPassword(customer);
                return customerRepository.saveAndFlush(customer);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A renda salarial deve ser maior ou igual 0!");
        }
    }

    public Boolean validateCustomer(Customer customer) {
        if (customer.getMonthlyIncomeCustomer() != null &&
                customer.getMonthlyIncomeCustomer().compareTo(BigDecimal.valueOf(0)) >= 0 &&
                !customer.getPasswordCustomer().equals("") &&
                customer.getPasswordCustomer() != null) {
            return true;
        } else {
            return false;
        }
    }


    public void encryptPassword(Customer customer){
        BCryptPasswordEncoder encrypt = new BCryptPasswordEncoder();
        String encryptedPassword = null;
        if (customer.getIdCustomer() == null) {
            encryptedPassword = encrypt.encode(customer.getPasswordCustomer());
            customer.setPasswordCustomer(encryptedPassword);
        } else {
            if (!customerRepository.findById(customer.getIdCustomer()).get().getPasswordCustomer()
                    .equals(customer.getPasswordCustomer())) {
                encryptedPassword = encrypt.encode(customer.getPasswordCustomer());
                customer.setPasswordCustomer(encryptedPassword);
            }
        }

    }



}
