package br.com.fatecrestapi.FoodFatec.service;

import br.com.fatecrestapi.FoodFatec.config.CustomerDetailsImpl;
import br.com.fatecrestapi.FoodFatec.dto.ResponseTokenDTO;
import br.com.fatecrestapi.FoodFatec.dto.ResponseUserDTO;
import br.com.fatecrestapi.FoodFatec.dto.UserLoginDTO;
import br.com.fatecrestapi.FoodFatec.entity.Customer;
import br.com.fatecrestapi.FoodFatec.exception.FindUserServiceException;
import br.com.fatecrestapi.FoodFatec.exception.LoginServiceException;
import br.com.fatecrestapi.FoodFatec.exception.TokenException;
import br.com.fatecrestapi.FoodFatec.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CustomerRepository customerRepository;

    public ResponseTokenDTO login(UserLoginDTO userLoginDTO) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            String accessToken = tokenService.generateAccessToken((CustomerDetailsImpl) auth.getPrincipal());
            String refreshToken = tokenService.generateRefreshToken((CustomerDetailsImpl) auth.getPrincipal());
            if (accessToken.isEmpty() || accessToken.isBlank()) {
                throw new TokenException("Erro ao gerar token!");
            }
            return new ResponseTokenDTO(accessToken, refreshToken);
        } catch (AuthenticationException ex) {
            System.out.println(ex.getMessage());
            throw new LoginServiceException("Login ou senha invalida!");
        } catch (ResponseStatusException ex) {
            throw new LoginServiceException("Falha ao logar, tente novamente mais tarde.");
        }
    }

    /*public ResponseUserDTO saveUser(SaveUserDTO saveUserDTO) {
        if (!validUser(saveUserDTO))
            throw new ValidUserException("Usuário inválido.");
        try {
            User user = new User();
            try {
                if (saveUserDTO.userId() != null) {
                    Optional<User> existUser = userRepository.findById(saveUserDTO.userId());
                    if (existUser.isPresent()) {
                        user = existUser.get();
                        user.setUpdatedDate(LocalDate.now());
                    } else {
                        throw new ValidUserException("ID do usuário inválido.");
                    }
                }
            } catch (Exception ex) {
                throw new ValidUserException("Usuário inválido.");
            }
            toUser(saveUserDTO, user);
            encryptPassword(user);
            return toResponseUserDTO(userRepository.saveAndFlush(user));
        } catch (Exception ex) {
            throw new InternalServerException("Erro ao salvar usuário, tente novamente.");
        }
    }*/

    public ResponseUserDTO findUserByToken(String idCustomer) throws JsonProcessingException {
        try {
            if (idCustomer == null || idCustomer.isEmpty()) {
                throw new TokenException("Token inválido");
            }
            Optional<Customer> customer = customerRepository.findById(String.valueOf(UUID.fromString(idCustomer)));
            if (customer.isEmpty()) {
                throw new FindUserServiceException("Usuário não encontrado.");
            }
            return toResponseUserDTO(customer.get());
        } catch (Exception ex) {
            throw new FindUserServiceException("Usuário não encontrado.");
        }
    }

    /*public Boolean validUser(SaveUserDTO saveUserDTO) {
        if (saveUserDTO.name().isBlank() || saveUserDTO.name() == null) {
            throw new ValidUserException("Informe um nome válido");
        }
        if (saveUserDTO.password().isBlank() || saveUserDTO.password() == null) {
            throw new ValidUserException("Informe uma senha valida");
        }
        if (saveUserDTO.email() == null || saveUserDTO.email().isBlank() || !saveUserDTO.email().matches("^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})*)$")) {
            throw new ValidUserException("Informe um email valido");
        }
        if (saveUserDTO.userId() == null) {
            if (userRepository.findByEmail(saveUserDTO.email()).isPresent())
                throw new ValidUserException("Email já cadastrado.");
            if (saveUserDTO.role() == Role.ADMIN)
                throw new ValidUserException("Permissão não autorizada.");
        }
        return true;
    }*/

    /*public void encryptPassword(User user) {
        try {
            BCryptPasswordEncoder encrypt = new BCryptPasswordEncoder();
            String encryptedPassword = encrypt.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        } catch (Exception ex) {
            throw new InternalServerException("Erro ao criptografar senha, tente novamente");
        }
    }*/

    /*public void toUser(SaveUserDTO dto, User user) {
        user.setUserId(dto.userId());
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setTelephone(dto.telephone());
        user.setRole(dto.role());
    }*/

    public ResponseUserDTO toResponseUserDTO(Customer customer) {
        return new ResponseUserDTO(customer.getIdCustomer(), customer.getFirstNameCustomer(), customer.getLastNameCustomer(), customer.getCpfCustomer(),
                customer.getBirthdateCustomer(), customer.getDateCreatedCustomer(), customer.getMonthlyIncomeCustomer(),
                customer.getStatusCustomer(), customer.getEmailCustomer(), customer.getRole());
    }
}
