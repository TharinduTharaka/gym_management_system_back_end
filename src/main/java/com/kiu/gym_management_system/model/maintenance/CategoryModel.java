package com.kiu.gym_management_system.model.maintenance;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
