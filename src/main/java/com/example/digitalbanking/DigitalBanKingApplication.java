package com.example.digitalbanking;

import com.example.digitalbanking.DTO.CurrentAccountDTO;
import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.DTO.SavingAccountDTO;
import com.example.digitalbanking.entities.Role;
import com.example.digitalbanking.entities.UserEntity;
import com.example.digitalbanking.repositories.RoleRepository;
import com.example.digitalbanking.services.AuthenticationService;
import com.example.digitalbanking.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.math.BigDecimal;
import java.util.stream.Stream;

@SpringBootApplication
@Configuration
@EnableJpaRepositories(basePackages = {"com.example.digitalbanking.repositories"})
public class DigitalBanKingApplication {
    private final RoleRepository roleRepository;

    public DigitalBanKingApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DigitalBanKingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankService bankService,
                                        AuthenticationService authenticationService) {
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
//            Create fake users
            authenticationService.saveUser(new UserEntity(null, "user", "user123", null));
            authenticationService.saveUser(new UserEntity(null, "admin", "admin123", null));
            authenticationService.saveRole(new Role(null, "ADMIN"));
            authenticationService.saveRole(new Role(null, "USER"));
            authenticationService.assignRoleToUSer("ADMIN", "admin");
            authenticationService.assignRoleToUSer("USER", "user");

        };
    }
}
