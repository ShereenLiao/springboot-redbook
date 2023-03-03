package com.chuwa.redbook.payload.security;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {
    private String account;
    private String password;
}
