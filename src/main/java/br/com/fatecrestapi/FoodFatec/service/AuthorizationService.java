package br.com.fatecrestapi.FoodFatec.service;


import br.com.fatecrestapi.FoodFatec.config.CustomerDetailsImpl;
import br.com.fatecrestapi.FoodFatec.entity.Customer;
import br.com.fatecrestapi.FoodFatec.exception.FindUserServiceException;
import br.com.fatecrestapi.FoodFatec.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws FindUserServiceException {
        Optional<Customer> customer = customerRepository.findByEmailCustomer(username);
        customer.orElseThrow(() -> new FindUserServiceException(username + " não encontrado."));
        return customer.map(CustomerDetailsImpl::new).get();
    }
}
