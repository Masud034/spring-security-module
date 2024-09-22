package com.example.springsecurity.models.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginResponseModel {
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String tokenType = "Bearer";
}
