package com.example.digitalbanking.services;

import com.example.digitalbanking.DTO.*;
import com.example.digitalbanking.entities.BankAccount;

import java.math.BigDecimal;
import java.util.List;

public interface BankService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerDTO(Long customerId);

    void deleteCustomer(Long id);

    CurrentAccountDTO saveCurrentBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal overDraft);

    SavingAccountDTO saveSavingBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal interestRate);

    BankAccount getBankAccount(String id);

    BankAccountDTO getBankAccountDTO(String id);

    void withdraw(String accountId, BigDecimal amount, String description);

    void deposit(String accountId, BigDecimal amount, String description);

    void transfer(String accountIdSource, String accountIdDestination, BigDecimal amount);

    List<BankAccountDTO> getAllBankAccounts();

    AccountHistoryDTO getAccountOperationHistory(String accountId, int page, int size);
}
