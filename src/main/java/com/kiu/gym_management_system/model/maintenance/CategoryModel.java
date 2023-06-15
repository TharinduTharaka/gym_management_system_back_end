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

    @JsonProperty("count")
    private int count;

    @JsonProperty("frequency")
    private String frequency;
    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private int status;

    @JsonProperty("description")
    private String description;
}
