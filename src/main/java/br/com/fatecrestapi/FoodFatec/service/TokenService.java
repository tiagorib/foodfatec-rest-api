package br.com.fatecrestapi.FoodFatec.service;

import br.com.fatecrestapi.FoodFatec.config.CustomerDetailsImpl;
import br.com.fatecrestapi.FoodFatec.dto.ResponseTokenDTO;
import br.com.fatecrestapi.FoodFatec.entity.Customer;
import br.com.fatecrestapi.FoodFatec.exception.AuthorizationException;
import br.com.fatecrestapi.FoodFatec.exception.FindUserServiceException;
import br.com.fatecrestapi.FoodFatec.exception.TokenException;
import br.com.fatecrestapi.FoodFatec.repository.CustomerRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Optional;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.time.refresh}")
    private long timeRefreshToken;

    @Value("${api.security.token.time.access}")
    private long timeAccessToken;

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseTokenDTO authRefreshToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthorizationException("Authorization Header inválido.");
        }

        try {
            Customer customer = new Customer();
            customer.setEmailCustomer(getPartToken(authorization, "SUBJECT"));
            CustomerDetailsImpl customerDetails = new CustomerDetailsImpl(customer);
            String accessToken = generateAccessToken(customerDetails);
            return new ResponseTokenDTO(accessToken, authorization.replace("Bearer ", ""));
        } catch (Exception ex) {
            throw new TokenException("Erro ao autenticar refreshToken, procure o administrador.");
        }
    }

    public String generateAccessToken(CustomerDetailsImpl customerDetails) {
        try {
            Optional<Customer> userOptional = customerRepository.findByEmailCustomer(customerDetails.getUsername());
            if (!userOptional.isPresent()) {
                throw new FindUserServiceException("Usuário não encontrado.");
            }

            Customer customer = userOptional.get();
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("foodfatec-rest-api")
                    .withClaim("idUser", customer.getIdCustomer().toString())
                    .withClaim("emailUser", customer.getEmailCustomer())
                    .withClaim("type", "access")
                    .withSubject(customer.getRole().toString())
                    .withExpiresAt(genExpirationDate(timeAccessToken))
                    .sign(algorithm);
            return token;
        } catch (Exception exception) {
            throw new TokenException("Erro ao gerar access token, procure o administrador.");
        }
    }

    public String generateRefreshToken(CustomerDetailsImpl customerDetails) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("foodfatec-rest-api")
                    .withClaim("type", "refresh")
                    .withSubject(customerDetails.getUsername())
                    .withExpiresAt(genExpirationDate(timeRefreshToken))
                    .sign(algorithm);
            return token;
        } catch (Exception ex) {
            throw new TokenException("Erro ao gerar refresh token, procure o administrador.");
        }
    }

    public String getPartToken(String token, String part) {
        try {
            if(token.endsWith("Bearer "))
                throw new TokenException("Token vazio!");
            DecodedJWT decodedJWT = isTokenValid(token.substring(7));
            if (part.equals("PAYLOAD")) {
                String encodedPayload = decodedJWT.getPayload();
                byte[] payloadBytes = Base64.getDecoder().decode(encodedPayload);
                return new String(payloadBytes, "UTF-8");
            }
            if (part.equals("SUBJECT")) {
                return decodedJWT.getSubject();
            }
            throw new TokenException("Parte do token não reconhecida: " + part);
        } catch (Exception ex) {
            throw new TokenException("Erro ao validar validar token, procure o administrador.");
        }
    }

    public DecodedJWT isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("foodfatec-rest-api")
                    .build()
                    .verify(token);
        } catch (Exception ex) {
            throw new TokenException("Erro ao validar token, , procure o administrador.");
        }
    }

    public String getClaimFromToken(String token, String claimName) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("foodfatec-rest-api")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim(claimName).asString();
        } catch (JWTVerificationException exception) {
            throw new TokenException("Erro ao extrair a claim do token: " + exception.getMessage());
        }
    }

    public String validateToken(String token) {
        String responseToken = this.getPartToken(token, "PAYLOAD");
        if (responseToken == null || responseToken.isEmpty() || responseToken.contains("refresh")) {
            throw new TokenException("Token inválido");
        }
        return responseToken;
    }

    private Instant genExpirationDate(long time) {
        return LocalDateTime.now().plusHours(time).toInstant(ZoneOffset.of("-03:00"));
    }
}
