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
public class MaintenanceModel {
    @JsonProperty("id")
    private int id;
    @JsonProperty("emp_code")
    private String empCode;
    @JsonProperty("title")
    private String title;
    @JsonProperty("date")
    private String date;
    @JsonProperty("status")
    private int status;
    @JsonProperty("category")
    private int category;
    @JsonProperty("task_id")
    private int taskID;
    @JsonProperty("taskTitle")
    private String taskTitle;
    @JsonProperty("description")
    private String description;

}
