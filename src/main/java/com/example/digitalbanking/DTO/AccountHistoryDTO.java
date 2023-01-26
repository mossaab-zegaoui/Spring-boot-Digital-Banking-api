package com.example.digitalbanking.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private BigDecimal balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOS;
}
