package br.com.fatecrestapi.FoodFatec.service;

import br.com.fatecrestapi.FoodFatec.config.CustomerDetailsImpl;
import br.com.fatecrestapi.FoodFatec.dto.ResponseTokenDTO;
import br.com.fatecrestapi.FoodFatec.dto.UserLoginDTO;
import br.com.fatecrestapi.FoodFatec.exception.LoginServiceException;
import br.com.fatecrestapi.FoodFatec.exception.TokenException;
import br.com.fatecrestapi.FoodFatec.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    /*public ResponseUserDTO findUserByToken(String idUser) throws JsonProcessingException {
        try {
            if (idUser == null || idUser.isEmpty()) {
                throw new TokenException("Token inválido");
            }
            Optional<Customer> customer = userRepository.findById(UUID.fromString(idUser));
            if (user.isEmpty()) {
                throw new FindUserServiceException("Usuário não encontrado.");
            }
            return toResponseUserDTO(user.get());
        } catch (Exception ex) {
            throw new FindUserServiceException("Usuário não encontrado.");
        }
    }*/

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

    /*public ResponseUserDTO toResponseUserDTO(User user) {
        return new ResponseUserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getTelephone(), user.getDisabled(), user.getCreatedDate(), user.getUpdatedDate(), user.getRole());
    }*/
}
