package com.kiu.gym_management_system.model.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class LoginAbilityModel {
    @JsonProperty("action")
    private String action;
    @JsonProperty("subject")
    private String subject;
}
