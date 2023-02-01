package com.example.digitalbanking.DTO;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    @NotNull(message = "name can't be null")
    private String name;
    @NotNull(message = "email can't be null")
    private String email;

}
