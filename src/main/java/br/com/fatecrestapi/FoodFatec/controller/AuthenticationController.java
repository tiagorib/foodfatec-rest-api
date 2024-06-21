package br.com.fatecrestapi.FoodFatec.controller;

import br.com.fatecrestapi.FoodFatec.dto.ResponseTokenDTO;
import br.com.fatecrestapi.FoodFatec.dto.ResponseUserDTO;
import br.com.fatecrestapi.FoodFatec.dto.UserLoginDTO;
import br.com.fatecrestapi.FoodFatec.exception.ResponseAuthentication;
import br.com.fatecrestapi.FoodFatec.service.AuthenticationService;
import br.com.fatecrestapi.FoodFatec.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login User")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO userLoginDTO) {
        ResponseTokenDTO token = authenticationService.login(userLoginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseAuthentication.response(token, HttpStatus.OK.value()));
    }

    /*@PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody SaveUserDTO saveUserDTO) {
        ResponseUserDTO result = authenticationService.saveUser(saveUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.response(result, HttpStatus.CREATED.value()));
    }*/

    /*@PutMapping("/updateUser")
    public ResponseEntity<Object> updateUser(@RequestBody SaveUserDTO saveUserDTO) {
        ResponseUserDTO result = authenticationService.saveUser(saveUserDTO);
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponse.response(result, HttpStatus.OK.value()));
    }*/

    @GetMapping("/validateToken")
    @Operation(summary = "Validate Token")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String token) {
        String responseToken = tokenService.validateToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseAuthentication.response(responseToken, HttpStatus.OK.value()));
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh Token")
    public ResponseEntity<Object> refreshToken(@RequestHeader("Authorization") String auth) {
        ResponseTokenDTO responseTokenDTO = tokenService.authRefreshToken(auth);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseAuthentication.response(responseTokenDTO, HttpStatus.OK.value()));
    }

    @GetMapping("/findUserByToken")
    @Operation(summary = "Find User By Token")
    public ResponseEntity<Object> findUserByToken(@RequestHeader("idUser") String idUser) throws JsonProcessingException {
        ResponseUserDTO result = authenticationService.findUserByToken(idUser);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseAuthentication.response(result, HttpStatus.OK.value()));
    }

    @GetMapping("/hello")
    @Operation(summary = "Hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.status(HttpStatus.OK).body("Hello");
    }

}
