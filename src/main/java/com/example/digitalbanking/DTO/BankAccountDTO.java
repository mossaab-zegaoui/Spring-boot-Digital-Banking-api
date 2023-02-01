package com.example.digitalbanking.DTO;

import com.example.digitalbanking.entities.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public  class BankAccountDTO {
    private String type;
    private String id;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private String currency;
    private AccountStatus status;
    private CustomerDTO customerDTO;

}
