package com.kiu.gym_management_system.model.attendance;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceModel {
    @JsonProperty("id")
    private int id;
    @JsonProperty("emp_name")
    private String empName;

    @JsonProperty("date")
    private String date;
    @JsonProperty("in_time")
    private String inTime;
    @JsonProperty("out_time")
    private String outTime;
    @JsonProperty("work_duration")
    private String workDuration;
    @JsonProperty("emp_code")
    private String empCode;
    @JsonProperty("role")
    private String role;
    @JsonProperty("status")
    private String status;


}
