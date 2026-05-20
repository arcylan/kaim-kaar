package com.kaim.kaar.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private Long userId;
    private String userName;
    private String role;
    private String token;
}
