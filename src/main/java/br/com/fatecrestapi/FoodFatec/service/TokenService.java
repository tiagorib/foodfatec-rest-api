package br.com.fatec.DonationHaAuthentication.service;

import br.com.fatec.DonationHaAuthentication.dto.ResponseTokenDTO;
import br.com.fatec.DonationHaAuthentication.entity.User;
import br.com.fatec.DonationHaAuthentication.exception.AuthorizationException;
import br.com.fatec.DonationHaAuthentication.exception.TokenException;
import br.com.fatec.DonationHaAuthentication.exception.FindUserServiceException;
import br.com.fatec.DonationHaAuthentication.repository.UserRepository;
import br.com.fatec.DonationHaAuthentication.security.UserDetailsImpl;
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
    private UserRepository userRepository;

    public ResponseTokenDTO authRefreshToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthorizationException("Authorization Header inválido.");
        }

        try {
            User user = new User();
            user.setEmail(getPartToken(authorization, "SUBJECT"));
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            String accessToken = generateAccessToken(userDetails);
            return new ResponseTokenDTO(accessToken, authorization.replace("Bearer ", ""));
        } catch (Exception ex) {
            throw new TokenException("Erro ao autenticar refreshToken, procure o administrador.");
        }
    }

    public String generateAccessToken(UserDetailsImpl userDetails) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
            if (!userOptional.isPresent()) {
                throw new FindUserServiceException("Usuário não encontrado.");
            }

            User user = userOptional.get();
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-donation-api")
                    .withClaim("idUser", user.getUserId().toString())
                    .withClaim("emailUser", user.getEmail())
                    .withClaim("type", "access")
                    .withSubject(user.getRole().toString())
                    .withExpiresAt(genExpirationDate(timeAccessToken))
                    .sign(algorithm);
            return token;
        } catch (Exception exception) {
            throw new TokenException("Erro ao gerar access token, procure o administrador.");
        }
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-donation-api")
                    .withClaim("type", "refresh")
                    .withSubject(userDetails.getUsername())
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
                    .withIssuer("auth-donation-api")
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
                    .withIssuer("auth-donation-api")
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
