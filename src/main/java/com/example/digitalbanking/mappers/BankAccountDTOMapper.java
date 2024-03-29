package com.example.digitalbanking.mappers;

import com.example.digitalbanking.DTO.BankAccountDTO;
import com.example.digitalbanking.DTO.CurrentAccountDTO;
import com.example.digitalbanking.DTO.SavingAccountDTO;
import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.CurrentAccount;
import com.example.digitalbanking.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountDTOMapper {
    private final CustomerDTOMapper customerDTOMapper;

    public BankAccountDTOMapper(CustomerDTOMapper customerDTOMapper) {
        this.customerDTOMapper = customerDTOMapper;
    }

    public BankAccount toBankAccount(BankAccountDTO bankAccountDTO) {
        if (bankAccountDTO.getType().equals("SavingAccount"))
            return toSavingBankAccount((SavingAccountDTO) bankAccountDTO);
        else
            return toCurrentBankAccount((CurrentAccountDTO) bankAccountDTO);
    }

    public BankAccountDTO toBankAccountDTO(BankAccount bankAccount) {
        if (bankAccount.getType().equals("SavingAccount"))
            return toSavingBankAccountDTO((SavingAccount) bankAccount);
        else
            return toCurrentBankAccountDTO((CurrentAccount) bankAccount);
    }

    public CurrentAccountDTO toCurrentBankAccountDTO(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomerDTO(customerDTOMapper
                .toCustomerDTO(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount
                .getClass()
                .getSimpleName());
        return currentAccountDTO;
    }

    public SavingAccountDTO toSavingBankAccountDTO(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        savingAccountDTO.setCustomerDTO(customerDTOMapper
                .toCustomerDTO(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount
                .getClass()
                .getSimpleName());

        return savingAccountDTO;
    }

    public CurrentAccount toCurrentBankAccount(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(customerDTOMapper
                .toCustomer(currentAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public SavingAccount toSavingBankAccount(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount);
        savingAccount.setCustomer(
                customerDTOMapper.toCustomer(
                        savingAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

}
