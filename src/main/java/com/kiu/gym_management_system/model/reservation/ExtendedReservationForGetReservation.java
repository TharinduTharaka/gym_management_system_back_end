package com.kiu.gym_management_system.model.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtendedReservationForGetReservation {

    @JsonProperty("calendar")
    private String calendar;

    @JsonProperty("description")
    private String description;


}
