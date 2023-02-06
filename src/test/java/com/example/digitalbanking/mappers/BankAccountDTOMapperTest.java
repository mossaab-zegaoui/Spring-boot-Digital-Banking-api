package com.example.digitalbanking.mappers;

import com.example.digitalbanking.DTO.BankAccountDTO;
import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.DTO.SavingAccountDTO;
import com.example.digitalbanking.entities.AccountStatus;
import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.Customer;
import com.example.digitalbanking.entities.SavingAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BankAccountDTOMapperTest {
    @InjectMocks
    BankAccountDTOMapper bankAccountDTOMapper;
    @Mock
    CustomerDTOMapper customerDTOMapper;

    @Test
    void toBankAccount() {
    }

    @Test
    void toBankAccountDTO_ShouldReturnBankAccountDTO() {
    }

    @Test
    void toCurrentBankAccountDTO() {
    }

    @Test
    void toSavingBankAccountDTO() {
    }

    @Test
    void toCurrentBankAccount() {
    }

    @Test
    void toSavingAccount() {
    }
}