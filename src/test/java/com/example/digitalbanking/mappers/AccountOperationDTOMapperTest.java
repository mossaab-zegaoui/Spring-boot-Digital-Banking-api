package com.example.digitalbanking.mappers;

import com.example.digitalbanking.DTO.AccountOperationDTO;
import com.example.digitalbanking.entities.AccountOperation;
import com.example.digitalbanking.entities.OperationType;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class AccountOperationDTOMapperTest {
    @InjectMocks
    private AccountOperationDTOMapper accountOperationDTOMapper;
    @Mock
    private AccountOperationRepository accountOperationRepository;

    @Test
    public void toAccountOperationDTO_ShouldReturnAccountOperationDTO() {
        AccountOperation accountOperation = AccountOperation.builder()
                .type(OperationType.WITHDRAW)
                .amount(BigDecimal.valueOf(Math.random() * 10000))
                .description("this is a withdraw")
                .date(LocalDateTime.now())
                .build();

        AccountOperationDTO expectedaccountOperationDTO = accountOperationDTOMapper.toAccountOperationDTO(accountOperation);

        Assertions.assertThat(expectedaccountOperationDTO).isNotNull();
        Assertions.assertThat(expectedaccountOperationDTO.getId()).isEqualTo(accountOperation.getId());
    }

    @Test
    void toAccountOperation_ShouldReturnAccountOperation() {
        AccountOperationDTO accountOperationDTO = AccountOperationDTO.builder()
                .id(5L)
                .type(OperationType.WITHDRAW)
                .amount(BigDecimal.valueOf(Math.random() * 10000))
                .description("this is a withdraw")
                .date(LocalDateTime.now())
                .build();
        AccountOperation expectedAccountOperation = accountOperationDTOMapper.toAccountOperation(accountOperationDTO);

        Assertions.assertThat(expectedAccountOperation).isNotNull();
        Assertions.assertThat(expectedAccountOperation.getId()).isEqualTo(accountOperationDTO.getId());
        Assertions.assertThat(expectedAccountOperation.getAmount()).isEqualTo(accountOperationDTO.getAmount());
    }

    @Test
    void toAccountOperationDTOList_ShouldReturnMoreThanOneAccountOperation() {
        Page<AccountOperation> accountOperations = Mockito.mock(Page.class);

        List<AccountOperationDTO> expectedAccountOperationsDTO = accountOperationDTOMapper
                .toAccountOperationDTOList(accountOperations);

        Assertions.assertThat(expectedAccountOperationsDTO).isNotNull();
        Assertions.assertThat(expectedAccountOperationsDTO.size()).isEqualTo(accountOperations.getSize());

    }

}