package com.kaim.kaar.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDTO {
    private String userName;
    private String email;
    private String role;
}
