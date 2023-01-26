package com.example.digitalbanking.mappers;


import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class CustomerDTOMapper {

    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

}
