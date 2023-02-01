package com.example.digitalbanking.mappers;

import com.example.digitalbanking.DTO.AccountOperationDTO;
import com.example.digitalbanking.entities.AccountOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountOperationDTOMapper {

    public AccountOperationDTO toAccountOperationDTO(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation toAccountOperation(AccountOperationDTO accountOperationDTO) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO, accountOperation);
        return accountOperation;
    }

    public List<AccountOperationDTO> toAccountOperationDTOList(Page<AccountOperation> accountOperations) {
        List<AccountOperationDTO> accountOperationDTOS = accountOperations
                .getContent()
                .stream()
                .map(this::toAccountOperationDTO)
                .collect(Collectors.toList());
        return accountOperationDTOS;
    }

}
