package com.kiu.gym_management_system.model.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleModel {
    @JsonProperty("id")
    private int id;
    @JsonProperty("emp_code")
    private String empCode;
    @JsonProperty("title")
    private String title;
    @JsonProperty("week_day")
    private int weekDay;
    @JsonProperty("status")
    private int status;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("instructor_name")
    private String instructorName;
}
