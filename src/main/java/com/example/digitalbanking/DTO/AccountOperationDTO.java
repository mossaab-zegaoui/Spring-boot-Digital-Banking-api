package com.example.digitalbanking.DTO;

import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.OperationType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

public class AccountOperationDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime date;
    private OperationType type;
    private String description;
}
