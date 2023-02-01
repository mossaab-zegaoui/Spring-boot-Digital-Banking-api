package com.example.digitalbanking.services;

import com.example.digitalbanking.DTO.AccountHistoryDTO;
import com.example.digitalbanking.DTO.AccountOperationDTO;
import com.example.digitalbanking.DTO.BankAccountDTO;
import com.example.digitalbanking.DTO.CurrentAccountDTO;
import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.DTO.SavingAccountDTO;
import com.example.digitalbanking.entities.AccountOperation;
import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.CurrentAccount;
import com.example.digitalbanking.entities.Customer;
import com.example.digitalbanking.entities.OperationType;
import com.example.digitalbanking.entities.SavingAccount;
import com.example.digitalbanking.exceptions.NotFoundException;
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
    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException("customer " + customerId + " NOT FOUND"));
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
        Customer customer = customerDTOMapper.toCustomer(customerDTO);
        customerRepository.save(customer);
        return customerDTOMapper.toCustomerDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public BankAccountDTO getBankAccountDTO(String bankAccountId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NotFoundException("Bank Account: " + bankAccountId + " NOT FOUND"));
        if (bankAccount instanceof CurrentAccount)
            return bankAccountDTOMapper.toCurrentBankAccountDTO((CurrentAccount) bankAccount);
        else
            return bankAccountDTOMapper.toSavingBankAccountDTO((SavingAccount) bankAccount);
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal overDraft) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException("customer " + customerId + " NOT FOUND"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setBalance((balance.setScale(2, RoundingMode.HALF_UP)));
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCreatedAt(LocalDateTime.now());
        currentAccount.setType(
                currentAccount.getClass()
                        .getSimpleName());
        currentAccount.setCustomer(customer);
        bankAccountRepository.save(currentAccount);

        return bankAccountDTOMapper.toCurrentBankAccountDTO(currentAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(BigDecimal balance, String type, Long customerId, BigDecimal interestRate) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer: " + customerId + " NOT FOUND"));
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setBalance((balance.setScale(2, RoundingMode.HALF_UP)));
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCreatedAt(LocalDateTime.now());
        savingAccount.setType(
                savingAccount.getClass()
                        .getSimpleName());
        savingAccount.setCustomer(customer);
        bankAccountRepository.save(savingAccount);

        return bankAccountDTOMapper.toSavingBankAccountDTO(savingAccount);
    }

    @Override
    public BankAccount getBankAccountById(String id) {
        BankAccount bankAccount = bankAccountRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("bank account: " + id + " NOT FOUND"));
        return bankAccount;
    }

    @Override
    public void withdraw(String accountId, BigDecimal amount, String description) {
        BankAccountDTO bankAccountDTO = getBankAccountDTO(accountId);
        if (bankAccountDTO.getBalance().compareTo(amount) == -1)
            throw new InsufficientBalanceException("Balance insufficient ");
        else {
            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setType(OperationType.WITHDRAW);
            accountOperation.setAmount(
                    amount.setScale(
                            2,
                            RoundingMode.HALF_UP));
            accountOperation.setDate(LocalDateTime.now());
            accountOperation.setDescription(description);
            if (bankAccountDTO.getType().equals("SavingAccount"))
                accountOperation.setBankAccount(
                        bankAccountDTOMapper.toSavingBankAccount(
                                (SavingAccountDTO) bankAccountDTO));
            else
                accountOperation.setBankAccount(
                        bankAccountDTOMapper.toCurrentBankAccount(
                                (CurrentAccountDTO) bankAccountDTO));
            accountOperationRepository.save(accountOperation);
//
            bankAccountDTO.setBalance(bankAccountDTO.getBalance().subtract(amount));
            bankAccountRepository.save(bankAccountDTOMapper.toBankAccount(bankAccountDTO));
        }
    }

    @Override
    public void deposit(String accountId, BigDecimal amount, String description) {
        BankAccount bankAccount = getBankAccountById(accountId);
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
        deposit(accountIdSource,
                amount,
                "Transfer money to : " + accountIdDestination);
        withdraw(accountIdDestination,
                amount,
                "Transfer money from: " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> getAllBankAccounts() {
        List<BankAccountDTO> bankAccountDTOS = bankAccountRepository
                .findAll()
                .stream()
                .map(bankAccount -> {
                    if (bankAccount instanceof CurrentAccount)
                        return bankAccountDTOMapper.toCurrentBankAccountDTO((CurrentAccount) bankAccount);
                    else
                        return bankAccountDTOMapper.toSavingBankAccountDTO((SavingAccount) bankAccount);
                }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public List<AccountOperationDTO> getAccountOperationsByBankAccountId(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository
                .findByBankAccountId(accountId);
        return accountOperationDTOMapper.toAccountOperationDTOList((Page<AccountOperation>) accountOperations);
    }

    @Override
    public AccountHistoryDTO getAccountOperationHistory(String accountId, int page, int size) {
        BankAccount bankAccount = getBankAccountById(accountId);
        Page<AccountOperation> accountOperations = accountOperationRepository
                .findByBankAccountId(
                        accountId,
                        PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperationDTOMapper.toAccountOperationDTOList(accountOperations);
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }
}
