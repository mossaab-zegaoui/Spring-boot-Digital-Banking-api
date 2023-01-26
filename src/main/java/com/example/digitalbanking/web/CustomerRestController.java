package com.example.digitalbanking.web;

import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.services.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerRestController {
    private final BankService bankService;

    public CustomerRestController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/")
    public List<CustomerDTO> customers() {
        return bankService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return bankService.getCustomerDTO(id);
    }

    @PostMapping("/")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return bankService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        bankService.deleteCustomer(id);
    }

}
