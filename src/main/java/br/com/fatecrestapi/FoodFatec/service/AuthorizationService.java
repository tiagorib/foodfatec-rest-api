package br.com.fatec.DonationHaAuthentication.service;

import br.com.fatec.DonationHaAuthentication.entity.User;
import br.com.fatec.DonationHaAuthentication.exception.FindUserServiceException;
import br.com.fatec.DonationHaAuthentication.repository.UserRepository;
import br.com.fatec.DonationHaAuthentication.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws FindUserServiceException {
        Optional<User> user = userRepository.findByEmail(username);
        user.orElseThrow(() -> new FindUserServiceException(username + " não encontrado."));
        return user.map(UserDetailsImpl::new).get();
    }
}
