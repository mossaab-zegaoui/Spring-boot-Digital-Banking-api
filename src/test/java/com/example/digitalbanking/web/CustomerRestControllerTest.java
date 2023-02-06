package com.example.digitalbanking.web;

import com.example.digitalbanking.DTO.CustomerDTO;
import com.example.digitalbanking.services.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(CustomerRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CustomerRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankService bankService;
    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void getAllCustomers_ShouldReturnListOfCustomersDTO() throws Exception {
//        CustomerDTO customerDTO = CustomerDTO.builder()
//                .id(1L)
//                .name("mossaab")
//                .email("mossaab@mail.com")
//                .build();
//        List<CustomerDTO> customerDTOS = Arrays.asList(customerDTO);
//        given(bankService.getAllCustomers()).willReturn(customerDTOS);
//        mockMvc.perform(get("/api/v1/customers")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].name", Matchers.is("mossaab")));
//    }

}