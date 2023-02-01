package com.example.digitalbanking.services;

import com.example.digitalbanking.DTO.AccountHistoryDTO;
import com.example.digitalbanking.DTO.AccountOperationDTO;
import com.example.digitalbanking.DTO.BankAccountDTO;
import com.example.digitalbanking.DTO.CurrentAccountDTO;
import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.DTO.SavingAccountDTO;
import com.example.digitalbanking.entities.BankAccount;
import java.math.BigDecimal;
import java.util.List;

public interface BankService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerById(Long customerId);

    void deleteCustomer(Long id);

    CurrentAccountDTO saveCurrentBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal overDraft);

    SavingAccountDTO saveSavingBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal interestRate);

    BankAccount getBankAccountById(String id);

    BankAccountDTO getBankAccountDTO(String id);

    void withdraw(String accountId, BigDecimal amount, String description);

    void deposit(String accountId, BigDecimal amount, String description);

    void transfer(String accountIdSource, String accountIdDestination, BigDecimal amount);

    List<BankAccountDTO> getAllBankAccounts();

    List<AccountOperationDTO> getAccountOperationsByBankAccountId(String accountId);

    AccountHistoryDTO getAccountOperationHistory(String accountId, int page, int size);
}
