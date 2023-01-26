package com.example.digitalbanking;

import com.example.digitalbanking.DTO.CurrentAccountDTO;
import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.DTO.SavingAccountDTO;
import com.example.digitalbanking.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBanKingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DigitalBanKingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankService bankService) {
        return args -> {
            Stream.of("Ahmed", "Amine", "Mossaab").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankService.saveCustomer(customer);
            });
            bankService.getAllCustomers()
                    .forEach(customer -> {
                        for (int i = 1; i < 10; i++) {
                            bankService.saveSavingBankAccount(
                                    BigDecimal.valueOf(Math.random() * 10000),
                                    "SAVING",
                                    customer.getId(),
                                    BigDecimal.valueOf(6));
                            bankService.saveCurrentBankAccount(BigDecimal.valueOf(Math.random() * 10000),
                                    "CURRENT",
                                    customer.getId(),
                                    BigDecimal.valueOf(Math.random() * 100));
                        }
                    });
            bankService.getAllBankAccounts()
                    .forEach(bankAccount -> {
                        String bankAccountId;
                        if (bankAccount instanceof CurrentAccountDTO)
                            bankAccountId = ((CurrentAccountDTO) bankAccount).getId();
                        else
                            bankAccountId = ((SavingAccountDTO) bankAccount).getId();
                        bankService.deposit(bankAccountId,
                                BigDecimal.valueOf(Math.random() * 10000 + 5000),
                                "DEPOSIT");
                        bankService.withdraw(bankAccountId,
                                BigDecimal.valueOf(Math.random() * 1000 + 500),
                                "WITHDRAW");
                    });
        };
    }
}
