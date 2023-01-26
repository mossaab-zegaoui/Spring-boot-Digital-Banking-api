package com.example.digitalbanking.services;

import com.example.digitalbanking.DTO.*;
import com.example.digitalbanking.entities.*;
import com.example.digitalbanking.exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.exceptions.CustomerNotFoundException;
import com.example.digitalbanking.exceptions.InsufficientBalanceException;
import com.example.digitalbanking.mappers.AccountOperationDTOMapper;
import com.example.digitalbanking.mappers.BankAccountDTOMapper;
import com.example.digitalbanking.mappers.CustomerDTOMapper;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankServiceImpl implements BankService {
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final CustomerDTOMapper customerDTOMapper;
    private final BankAccountDTOMapper bankAccountDTOMapper;
    private final AccountOperationDTOMapper accountOperationDTOMapper;

    public BankServiceImpl(CustomerRepository customerRepository,
                           BankAccountRepository bankAccountRepository,
                           AccountOperationRepository accountOperationRepository,
                           CustomerDTOMapper customerDTOMapper,
                           BankAccountDTOMapper bankAccountDTOMapper,
                           AccountOperationDTOMapper accountOperationDTOMapper) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.customerDTOMapper = customerDTOMapper;
        this.bankAccountDTOMapper = bankAccountDTOMapper;
        this.accountOperationDTOMapper = accountOperationDTOMapper;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customers = customerRepository
                .findAll()
                .stream()
                .map(customerDTOMapper::toCustomerDTO)
                .collect(Collectors.toList());
        return customers;

    }

    @Override
    public CustomerDTO getCustomerDTO(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("customer " + customerId + " NOT FOUND"));
        return customerDTOMapper.toCustomerDTO(customer);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        Customer savedCustomer = customerDTOMapper.toCustomer(customerDTO);
        customerRepository.save(savedCustomer);
        return customerDTOMapper.toCustomerDTO(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {

        Customer savedCustomer = customerDTOMapper.toCustomer(customerDTO);
        customerRepository.save(savedCustomer);
        return customerDTOMapper.toCustomerDTO(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public BankAccountDTO getBankAccountDTO(String id) {
        BankAccount bankAccount = bankAccountRepository
                .findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank Account: " + id + " NOT FOUND"));
        if (bankAccount instanceof CurrentAccount)
            return bankAccountDTOMapper.toCurrentBankAccountDTO((CurrentAccount) bankAccount);
        else
            return bankAccountDTOMapper.toSavingBankAccountDTO((SavingAccount) bankAccount);

    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal overDraft) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("customer " + customerId + " NOT FOUND"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setBalance((balance.setScale(2, RoundingMode.HALF_UP)));
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCreatedAt(LocalDateTime.now());
        currentAccount.setCustomer(customer);
        bankAccountRepository.save(currentAccount);
        return bankAccountDTOMapper.toCurrentBankAccountDTO(currentAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal interestRate) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer: " + customerId + " NOT FOUND"));
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setBalance((balance.setScale(2, RoundingMode.HALF_UP)));
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCreatedAt(LocalDateTime.now());
        savingAccount.setCustomer(customer);
        bankAccountRepository.save(savingAccount);
        return bankAccountDTOMapper.toSavingBankAccountDTO(savingAccount);
    }

    @Override
    public BankAccount getBankAccount(String id) {
        BankAccount bankAccount = bankAccountRepository
                .findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("bank account: " + id + " NOT FOUND"));
        return bankAccount;
    }


    @Override
    public void withdraw(String accountId, BigDecimal amount, String description) {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount.getBalance().compareTo(amount) == -1)
            throw new InsufficientBalanceException("Balance insufficient ");
        else {
            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setType(OperationType.WITHDRAW);
            accountOperation.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
            accountOperation.setDate(LocalDateTime.now());
            accountOperation.setDescription(description);
            accountOperation.setBankAccount(bankAccount);
            accountOperationRepository.save(accountOperation);
//
            bankAccount.setBalance(bankAccount.getBalance().subtract(amount));
            bankAccountRepository.save(bankAccount);
        }
    }

    @Override
    public void deposit(String accountId, BigDecimal amount, String description) {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEPOSIT);
        accountOperation.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
        accountOperation.setDate(LocalDateTime.now());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
//
        bankAccount.setBalance(bankAccount.getBalance().add(amount));
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, BigDecimal amount) {
        deposit(accountIdSource, amount, "Transfer money to : " + accountIdDestination);
        withdraw(accountIdDestination, amount, "Transfer money from: " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> getAllBankAccounts() {

        List<BankAccountDTO> bankAccountDTOS = bankAccountRepository.findAll().stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount)
                return bankAccountDTOMapper.toCurrentBankAccountDTO((CurrentAccount) bankAccount);
            else
                return bankAccountDTOMapper.toSavingBankAccountDTO((SavingAccount) bankAccount);
        }).collect(Collectors.toList());

        return bankAccountDTOS;
    }

    @Override
    public AccountHistoryDTO getAccountOperationHistory(String accountId, int page, int size) {
        BankAccount bankAccount = getBankAccount(accountId);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS =
                accountOperations
                        .getContent()
                        .stream()
                        .map(accountOperationDTOMapper::toAccountOperationDTO)
                        .collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }
}
