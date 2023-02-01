package com.example.digitalbanking.DTO;

import com.example.digitalbanking.entities.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountOperationDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime date;
    private OperationType type;
    private String description;
}
