package com.kiu.gym_management_system.model.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationModel {

    @JsonProperty("id")
    private int id;
    @JsonProperty("emp_code")
    private String empCode;
    @JsonProperty("title")
    private String title;
    @JsonProperty("reservation_type")
    private int reservationType;
    @JsonProperty("status")
    private int status;
    @JsonProperty("start")
    private String start;
    @JsonProperty("end")
    private String end;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("instructor_id")
    private int instructorId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("extendedProps")
    private ExtendedReservationForGetReservation extendedProps;
}
