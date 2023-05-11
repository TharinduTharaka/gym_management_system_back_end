package com.kiu.gym_management_system.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InOutTimeEntity {

    private  int id;
    private String time;
    private String date;
}
