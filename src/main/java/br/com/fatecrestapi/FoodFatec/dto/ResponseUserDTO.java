package br.com.fatecrestapi.FoodFatec.dto;

import br.com.fatecrestapi.FoodFatec.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResponseUserDTO(String idCustomer, String firstNameCustomer, String lastNameCustomer, String cpfCustomer,
                              LocalDate birthdateCustomer, LocalDate dateCreatedCustomer, BigDecimal monthlyIncomeCustomer,
                              Boolean statusCustomer, String emailCustomer, Role role) {
}
