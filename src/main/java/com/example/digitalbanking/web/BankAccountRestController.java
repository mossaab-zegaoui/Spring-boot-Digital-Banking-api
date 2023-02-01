package com.example.digitalbanking.web;

import com.example.digitalbanking.DTO.AccountHistoryDTO;
import com.example.digitalbanking.DTO.BankAccountDTO;
import com.example.digitalbanking.services.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bankAccounts")
public class BankAccountRestController {
    private final BankService bankService;

    public BankAccountRestController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankService.getAllBankAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable String id) {
        return ResponseEntity.ok(bankService.getBankAccountDTO(id));
    }

    @GetMapping("/{id}/operations")
    public ResponseEntity<AccountHistoryDTO> getHistory(
            @PathVariable String id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(bankService.getAccountOperationHistory(id, page, size));
    }


}
