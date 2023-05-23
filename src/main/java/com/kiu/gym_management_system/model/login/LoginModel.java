package com.kiu.gym_management_system.model.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {

    @JsonProperty("id")
    private int id;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("user_name")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private String role;
    @JsonProperty("ability")
    private LoginAbilityModel ability;
}
