package com.example.digitalbanking.services;

import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.entities.*;
import com.example.digitalbanking.mappers.AccountOperationDTOMapper;
import com.example.digitalbanking.mappers.BankAccountDTOMapper;
import com.example.digitalbanking.mappers.CustomerDTOMapper;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BankServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    public BankAccountDTOMapper bankAccountDTOMapper;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private CustomerDTOMapper customerDTOMapper;
    @Mock
    private AccountOperationRepository accountOperationRepository;
    @Mock
    private AccountOperationDTOMapper accountOperationDTOMapper;
    @InjectMocks
    private BankServiceImpl bankService;


    @Test
    public void getCustomerById_ShouldReturnCustomerDTO() {
        Long Id = 5L;
        Customer customer = Customer.builder()
                .id(Id)
                .name("amine")
                .email("amine@gmail.com")
                .build();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(Id)
                .name("amine")
                .email("amine@gmail.com")
                .build();
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerDTOMapper.toCustomerDTO(customer)).thenReturn(customerDTO);

        CustomerDTO expectedCustomerDTO = bankService.getCustomerById(Id);

        Assertions.assertThat(expectedCustomerDTO).isNotNull();
        Assertions.assertThat(expectedCustomerDTO.getId()).isEqualTo(customer.getId());
    }

    @Test
    public void getAllCustomers_ShouldReturnMoreThanOneCustomer() {
        Customer customer = Customer.builder()
                .name("amine")
                .email("amine@gmail.com")
                .build();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("amine")
                .email("amine@gmail.com")
                .build();
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(customer);
        customers.add(customer);

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerDTOMapper.toCustomerDTO(customer)).thenReturn(customerDTO);
        List<CustomerDTO> expectedCustomers = bankService.getAllCustomers();

        Assertions.assertThat(expectedCustomers).isNotNull();
        assertEquals(expectedCustomers.size(), 2);
    }

    @Test
    public void saveCustomer_ShouldReturnSavedCustomerDTO() {
        Customer customer = Customer.builder()
                .name("amine")
                .email("amine@gmail.com")
                .build();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("amine")
                .email("amine@gmail.com")
                .build();
        when(customerDTOMapper.toCustomer(customerDTO)).thenReturn(customer);
        when(customerDTOMapper.toCustomerDTO(customer)).thenReturn(customerDTO);

        CustomerDTO expetectedCustomerDTO = bankService.saveCustomer(customerDTO);

        Assertions.assertThat(expetectedCustomerDTO).isNotNull();
        Assertions.assertThat(expetectedCustomerDTO.getId()).isEqualTo(customerDTO.getId());
        Assertions.assertThat(expetectedCustomerDTO.getName()).isEqualTo(customerDTO.getName());
    }

    @Test
    public void updateCustomer_ShouldReturnUpdatedCustomerDTO() {
        Customer customer = Customer.builder()
                .name("amine")
                .email("amine@gmail.com")
                .build();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("amine")
                .email("amine@gmail.com")
                .build();
        when(customerDTOMapper.toCustomer(customerDTO)).thenReturn(customer);
        when(customerDTOMapper.toCustomerDTO(customer)).thenReturn(customerDTO);

        CustomerDTO expectedCustomerDTO = bankService.updateCustomer(customerDTO);

        Assertions.assertThat(expectedCustomerDTO).isNotNull();
        Assertions.assertThat(expectedCustomerDTO.getId()).isEqualTo(customerDTO.getId());
        Assertions.assertThat(expectedCustomerDTO.getName()).isEqualTo(customerDTO.getName());
    }

    @Test
    public void deleteCustomer_ShouldReturnNull() {
        Customer customer = Customer.builder()
                .name("amine")
                .email("amine@gmail.com")
                .build();
        assertAll(() -> bankService.deleteCustomer(customer.getId()));

    }

    @Test
    public void getBankAccountById_ShouldReturnBankAccount() {
        String Id = "cd2222";
        BankAccount bankAccount = BankAccount.builder()
                .id(Id)
                .currency("$")
                .balance(BigDecimal.valueOf(Math.random() * 10000))
                .status(AccountStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .type(BankAccount.class.getSimpleName())
                .build();

        when(bankAccountRepository.findById(Id)).thenReturn(Optional.of(bankAccount));

        BankAccount expectedBankAccount = bankService.getBankAccountById(Id);
        Assertions.assertThat(expectedBankAccount).isNotNull();
        Assertions.assertThat(expectedBankAccount.getId()).isEqualTo(bankAccount.getId());
    }

//    @Test
//    public void getAccountOperationsByBankAccountId_ShouldReturnListOfOperations() {
//        String Id = "cd2222";
//        AccountOperation accountOperation = AccountOperation.builder()
//                .id(5L)
//                .type(OperationType.WITHDRAW)
//                .amount(BigDecimal.valueOf(Math.random() * 10000))
//                .description("this is a withdraw")
//                .date(LocalDateTime.now())
//                .build();
//        AccountOperationDTO accountOperationDTO = AccountOperationDTO.builder()
//                .id(5L)
//                .type(OperationType.WITHDRAW)
//                .amount(BigDecimal.valueOf(Math.random() * 10000))
//                .description("this is a withdraw")
//                .date(LocalDateTime.now())
//                .build();
//        Page<AccountOperation> accountOperations = Mockito.mock(Page.class);
//
//        when(accountOperationDTOMapper.toAccountOperationDTOList(accountOperations)).thenReturn(Arrays.asList(accountOperationDTO));
//
//        List<AccountOperationDTO> expectedAccountsOperations = bankService.getAccountOperationsByBankAccountId(Id);
//        Assertions.assertThat(expectedAccountsOperations).isNotNull();
//    }
}